package pl.sao.smallproxyjetty

import play.api.libs.json.JsValue

class ParallelReposRequester(val org: String, val overList: Seq[String])
  extends SmallProxyJettyRequest[String] {

  override def extract(d: JsValue): String = (d \ "name").get.as[String]
  override def url(org: String, overList: Seq[String], i: Int): String =
    s"https://api.github.com/orgs/$org/repos"

  def getResponse: Seq[String] =
    collectData(org, overList)(extract,url)
}
