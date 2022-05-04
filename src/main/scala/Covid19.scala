import org.scalajs.dom.*
import org.http4s.implicits.uri
import scala.scalajs.js
import js.annotation.*
import js.JSConverters.*

@js.native
@JSGlobal("c3")
object C3 extends js.Object:
    def generate(data: js.Dynamic): js.Object = js.native
object Covid19:
    val regex = raw"<createDt>(\d{4}-\d{2}-\d{2}).+?</createDt><deathCnt>(\d+)</deathCnt><decideCnt>(\d+)</decideCnt>".r
    def main(as: Array[String]): Unit =
        val _datakey = "Xxlk0cO8cBxM+E1llGT1UhKwq4Wx5u0KRoxZaem9gzCQvQ/AbFfoorokyJMlBGPIOxj76kOVwQ/ezpFYvnJBJQ=="
        val u = uri"http://openapi.data.go.kr/openapi/service/rest/Covid19/getCovid19InfStateJson"
                .withQueryParam("serviceKey", _datakey)
                .withQueryParam("startCreateDt", "20220401")
                .withQueryParam("endCreateDt", "20220504")

        val xhr = XMLHttpRequest()
        xhr.open("GET", u.toString)
        xhr.onload = {(e: Event) =>
            val t3s = regex.findAllIn(xhr.responseText).map {
                case regex(date, dcount, ccount) => (date, dcount.toInt, ccount.toInt)
            }.toList.reverse.sliding(2, 1).map(t3list => 
                (t3list(0)._1, t3list(1)._2 - t3list(0)._2, t3list(1)._3 - t3list(0)._3)    
            ).toList

            val dates = "Date" +: t3s.map(_._1).toVector
            val dcounts = "Death Count" +: t3s.map(_._2).toVector
            val ccounts = "Confirmed Count" +: t3s.map(_._3).toVector

            val columns = Seq(dates.toJSArray, dcounts.toJSArray, ccounts.toJSArray).toJSArray

            val data = js.Dynamic.literal(x = "Date", columns = columns)
            val axis = js.Dynamic.literal(x = js.Dynamic.literal(`type` = "timeseries", tick = js.Dynamic.literal(format = "%Y-%m-%d")))

            C3.generate(js.Dynamic.literal(bindto = "#msg", data = data, axis = axis))
        }
        xhr.send()