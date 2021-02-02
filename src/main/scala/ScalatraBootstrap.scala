import javax.servlet.ServletContext
import org.scalatra.{LifeCycle, ScalatraServlet}
import pl.sao.smallproxyjetty._

class ScalatraBootstrap extends LifeCycle {

	override def init(context: ServletContext) {
		try {
			context.mount(new SmallProxyJetty, "/*")
		} catch {
			case e: Throwable => e.printStackTrace()
		}
	}
}

class ResourcesApp extends ScalatraServlet {
	before() {
		response.headers += ("Access-Control-Allow-Origin" -> "*")
		}

	protected def buildFullUrl(path: String) = if (path.startsWith("http")) path else {
		val port = request.getServerPort
		val h    = request.getServerName
		val prot = if (port == 443) "https" else "http"
		val (proto, host) = if (port != 80 && port != 443) ("http", h + ":" + port.toString) else (prot, h)

		"%s://%s%s%s".format( proto, host, request.getContextPath, path )
		}
}
