package pl.sao.smallproxyjetty

import play.api.libs.json.JsValue

class ParallelContributorsRequester(val org: String, val overList: Seq[String])
  extends SmallProxyJettyRequest[(String,Int)] {

  def extract(d: JsValue): (String,Int) = {
    val login = (d \ "login").get
    val contributions = (d \ "contributions").get
    login.as[String] -> contributions.as[Int]
  }

  def url(org: String, overList: Seq[String], i: Int): String =
    s"https://api.github.com/repos/$org/${overList(i)}/contributors"

  def getResponse: Seq[(String, Int)] = {
    collectData(org, overList)(extract,url)
      .groupBy(_._1)
      .map { contr =>
        contr._1 -> contr._2.map(x => x._2).sum
      }
      .toSeq
      .sortWith(_._2 > _._2)
  }
}
