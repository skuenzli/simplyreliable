import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import akka.util.duration._
import com.ning.http.client.{Response, Request}

class OutstandingRequestLimitingFilterSimulation extends Simulation {

  def apply = {

    val urlBase = "http://localhost:8080"
    val NUM_SAMPLES = 100

    val httpConf = httpConfig.baseURL(urlBase)
      .requestInfoExtractor((request: Request) => {
      List[String](request.getUrl)
    })
      .responseInfoExtractor((response: Response) => {
      List[String](response.getStatusCode.toString())
    })

    val headers_standard = Map(
      "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
      "Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7",
      "Accept-Encoding" -> "gzip,deflate",
      "Host" -> "localhost:8080")

    val headers_json = Map(
      "Accept" -> "application/json",
      "Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7",
      "Accept-Encoding" -> "gzip,deflate",
      "Host" -> "localhost:8080")

    val setupScenario = scenario("Setup")
      .repeat(NUM_SAMPLES) {
      chain
        .exec(
        http("examples - create")
          .post("/sample-webapp/examples/")
          .headers(headers_json)
          .body("{}")
      )
    }

    val randomIntegerFeeder = new Feeder {

      import scala.util.Random

      private val RNG = new Random

      // always return true as this feeder can be polled infinitely
      override def hasNext = true

      override def next: Map[String, String] = {
        Map("randomInt" -> (RNG.nextInt(NUM_SAMPLES) + 1).toString)
      }
    }

    val expectedStatuses: List[Int] = List(200, 503)
    val overloadScenario = scenario("Request overload")
      .loop(
      chain
        .exec(
        http("homepage")
          .get("/sample-webapp/")
          .headers(headers_standard)
          .check(status.in(expectedStatuses)))

        .exec(
        http("examples - list")
          .get("/sample-webapp/examples/")
          .headers(headers_json)
          .check(status.in(expectedStatuses))
          .check(responseTimeInMillis.lessThan(100)))

        .feed(randomIntegerFeeder)

        .loop(
        chain
          .exec(
          http("examples - get")
            .get("/sample-webapp/examples/${randomInt}")
            .headers(headers_json)
            .check(status.in(expectedStatuses))
            .check(responseTimeInMillis.lessThan(100))
          )
        ).times(3)

        .pauseExp(100 milliseconds)

    ).during(60, SECONDS)

    List(
      setupScenario.configure.users(1).protocolConfig(httpConf)
      , overloadScenario.configure.users(100).ramp(10).protocolConfig(httpConf).delay(10, SECONDS)
    )
  }
}
