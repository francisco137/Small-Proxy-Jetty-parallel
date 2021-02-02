
import org.scalatest._

class PlentyOfTestsSpec extends FunSuite with DiagrammedAssertions {
    test("Here we have plenty of tests...") {
        assert("Small Proxy Jetty".startsWith("S"))
    }
}
