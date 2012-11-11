import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
import akka.util.duration._
import com.ning.http.client.{Response, Request}

class OutstandingRequestLimitingFilterSimulation extends Simulation {

  def apply = {

    val urlBase = "http://localhost:8080"

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
      .repeat(10) {
      chain
        .exec(
        http("examples - create")
          .post("/sample-webapp/examples/")
          .headers(headers_json)
          .body("{}")
      )
    }

    val overloadScenario = scenario("Request overload")
      .loop(
      chain
        .exec(
        http("homepage")
          .get("/sample-webapp/")
          .headers(headers_standard)
          .check(status.in(List(200, 503))))
        /* .pause(0, 50, MILLISECONDS) */
        .pauseExp(100 milliseconds)

        .exec(
        http("examples - list")
          .get("/sample-webapp/examples/")
          .headers(headers_json)
          .check(status.in(List(200, 503))))
    ).during(1, MINUTES)

    List(
      setupScenario.configure.users(1).ramp(1).protocolConfig(httpConf)
      , overloadScenario.configure.users(100).ramp(10).protocolConfig(httpConf)
    )
  }
}
