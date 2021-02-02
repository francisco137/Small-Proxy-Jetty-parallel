package pl.sao.smallproxyjetty

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.{ActionResult, BadRequest, Ok, ScalatraServlet, Unauthorized}
import org.scalatra.json.JacksonJsonSupport
import pl.sao.smallproxyjetty.util.SmallProxyLogger
import play.api.libs.json.{JsArray, JsObject, Json}

class PerformRequest extends ScalatraServlet with JacksonJsonSupport {

  private[this] val per_page: Int = sys.env.getOrElse("GH_PER_PAGE", "50").toInt
  private[this] val collectors: Int = sys.env.getOrElse("SPJ_COLLECTORS", "4").toInt

  protected implicit val jsonFormats: Formats = DefaultFormats

  val hint: JsObject = Json.obj(
    "Form of the legal path" -> "/org/:organization/contributors",
    "Allowed characters in path" -> "[a-zA-Z0-9_,-]",
    "Start variables" -> Json.obj(
      "GH_TOKEN" -> "Github authorization token as env variable - mandatory",
      "GH_PER_PAGE" -> "Github per_page get argument - default 50",
      "SPJ_COLLECTORS" -> "Simple Proxy collectors - default 4"
    )
  )

  val getBadRequest: ActionResult = {
    BadRequest(Json.obj(
      "message" -> s"Illegal path!",
      "hint" -> hint
    ))
  }

  val getGeneralInformation: ActionResult = {
    Ok(Json.obj(
      "message" -> "General Information",
      "hint" -> hint
    ))
  }

  val noTokenWarning: ActionResult = {
    Unauthorized(Json.obj(
      "message" -> "No authorization token!",
      "hint" -> hint
    ))
  }

  def getContributors(org: String, asc_desc: String = "asc_desc"): ActionResult = {
    val t0 = System.nanoTime()
    val reposRequester = new ParallelReposRequester(org, Seq(org))
    val reposNames = reposRequester.getResponse

    val contributorsRequester = new ParallelContributorsRequester(org, reposNames)
    val contributors = JsArray(
      contributorsRequester
        .getResponse
        .map( contr => Json.obj(contr._1 -> contr._2 ))
    )
    val t1 = System.nanoTime()
    SmallProxyLogger.logger.info(s"time = ${(t1-t0)/1000000} ms (collectors = $collectors, per_page = $per_page)")
    ActionResult(200, contributors, Map.empty)
  }
}
