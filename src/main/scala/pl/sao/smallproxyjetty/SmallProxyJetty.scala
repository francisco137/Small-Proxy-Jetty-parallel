package pl.sao.smallproxyjetty

import org.scalatra._
import org.scalatra.{AsyncResult, FutureSupport}
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

class SmallProxyJetty extends ScalatraServlet with FutureSupport {

  protected implicit def executor: ExecutionContextExecutor = ExecutionContext.global
  private[this] val gh_token: String = sys.env.getOrElse("GH_TOKEN", "")

  before() { contentType = "application/json" }

  val performRequest = new PerformRequest

  get("/") {
    new AsyncResult {
      val is: Future[ActionResult] =
        Future(performRequest.getGeneralInformation)
    }
  }

  get("/*") {
    new AsyncResult {
      val is: Future[ActionResult] =
        Future(performRequest.getBadRequest)
    }
  }

  get("/org/:organization/contributors") {
    new AsyncResult {
      val is: Future[ActionResult] = Future {
        if (gh_token == "")
          performRequest.noTokenWarning
        else
          performRequest.getContributors(params("organization"))
      }
    }
  }
}
