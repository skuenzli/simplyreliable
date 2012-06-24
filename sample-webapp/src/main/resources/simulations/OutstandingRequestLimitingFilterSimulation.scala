import com.excilys.ebi.gatling.core.Predef._
import com.excilys.ebi.gatling.http.Predef._
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
			"Host" -> "localhost:8080",
			"User-Agent" -> "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.17) Gecko/20110422 Ubuntu/9.10 (karmic) Firefox/3.6.17")

		val scn = scenario("Request overload")
      .loop(
      chain
        .exec(
        http("homepage")
          .get("/sample-webapp/")
          .headers(headers_standard)
          .check(status.in(List(200, 503))))
          /* .pause(0, 50, MILLISECONDS) */
          .pauseExp(100, MILLISECONDS)
    ).during(1, MINUTES)

    List(scn.configure.users(100).ramp(10).protocolConfig(httpConf))
	}
}
