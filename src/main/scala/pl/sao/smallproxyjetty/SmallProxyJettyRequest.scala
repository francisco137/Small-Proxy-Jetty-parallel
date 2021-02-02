package pl.sao.smallproxyjetty

import pl.sao.smallproxyjetty.util.Paging
import play.api.libs.json.{JsArray, JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

trait SmallProxyJettyRequest[T] {

  val org: String
  val overList: Seq[String]

  def extract(d: JsValue): T
  def url(org: String, overList: Seq[String], i: Int): String

  val paging = new Paging(overList.size)

  // GH_TOKEN has to be the GitHub authorization token written as environment variable:
  val gh_token: String = sys.env.getOrElse("GH_TOKEN", "")

  // GH_GH_PER_PAGE - how many records per one request:
  val per_page: Int = sys.env.getOrElse("GH_PER_PAGE", "50").toInt

  val collectors: Int = sys.env.getOrElse("SPJ_COLLECTORS", "4").toInt

  def collectData[T](org: String, overList: Seq[String])(
    implicit extract: JsValue => T, url: (String,Seq[String],Int) => String): Seq[T] = {
    val futures = for {
      collector <- 1 to collectors
    } yield Future { getOneCollector(collector, org, overList)(extract,url) }

    futures
      .map(Await.result(_, Duration.Inf))
      .reduce(_ ++ _)
  }

  def getOneCollector[T](collector: Int, org: String, overList: Seq[String])(
    implicit extract: JsValue => T, url: (String,Seq[String],Int) => String
  ): Seq[T] = {

    var result: Seq[T] = Nil
    var (item,page) = paging.getNextItemWithPage(collector)
    paging.whileNextRequest(page > 0 && item < overList.size) {
      paging.saveLastItemPage(collector, item, page, collector)

      val finalUrl = url(org, overList, item) + s"?per_page=$per_page&page=$page"

      val resp = requests.get(
        finalUrl,
        headers = Map("Authorization" -> s"token $gh_token"),
        check = false)

      val wynik = Json.parse(resp.data.toString()) match {
        case JsArray(value) => (resp.statusCode, resp.statusMessage, value.map(extract))
        case _ => (resp.statusCode, resp.statusMessage, Nil)
      }

      if (wynik._3.isEmpty || wynik._3.size < per_page) paging.saveLastItemPage(collector, item, 0, -1)

      val pair = paging.getNextItemWithPage(collector)
      item = pair._1
      page = pair._2

      result ++= wynik._3
    }
    result
  }
}
