/**
 * StandardHeaders provides a number of standard HTTP headers for use in simulations.
 */
object StandardHeaders {

  val HTML = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7",
    "Accept-Encoding" -> "gzip,deflate",
    "Host" -> "localhost:8080")

  val JSON = Map(
    "Accept" -> "application/json",
    "Accept-Charset" -> "ISO-8859-1,utf-8;q=0.7,*;q=0.7",
    "Accept-Encoding" -> "gzip,deflate",
    "Host" -> "localhost:8080")

}

