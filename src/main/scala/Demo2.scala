import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.{document, window}
import japgolly.scalajs.react.callback.CallbackTo
import scala.scalajs.js

object Demo2:
    case class State(tenMiliSecondsElapsed: Long, x: Int, y: Int, xDir: Int, yDir: Int)

    class Backend($: BackendScope[Unit, State]) {
        var interval: js.UndefOr[js.timers.SetIntervalHandle] =
            js.undefined

        val bsize = 30

        def tick = 
            $.modState(s => 
                val nx = s.x + s.xDir
                val nxDir = 
                    if nx < 0 || (nx + bsize) > window.innerWidth then
                        -s.xDir
                    else
                        s.xDir
                val ny = s.y + s.yDir
                val nyDir = 
                    if ny < 0 || (ny + bsize) > window.innerHeight then
                        -s.yDir
                    else
                        s.yDir
                State(s.tenMiliSecondsElapsed + 1, nx, ny, nxDir, nyDir))
        
        def start = Callback {
            interval = js.timers.setInterval(10)(tick.runNow())
        }

        def clear = Callback {
            interval foreach js.timers.clearInterval
            interval = js.undefined
        }

        def render(s: State) =
            <.div(
                <.div("Seconds elapsed: ", s.tenMiliSecondsElapsed / 100),
                <.div(
                    ^.position.fixed,
                    ^.top := s"${(s.y)}px",
                    ^.left := s"${(s.x)}px",
                    ^.width := bsize.toString + "px",
                    ^.height := bsize.toString + "px",
                    ^.background := "green",
                )
            )
    }

    val Timer = ScalaComponent.builder[Unit]
                .initialState(State(0, 50, 50, 1, 1))
                .renderBackend[Backend]
                .componentDidMount(_.backend.start)
                .componentWillUnmount(_.backend.clear)
                .build

    /*
    def main(as: Array[String]): Unit =
        Timer().renderIntoDOM(document.getElementById("msg"))
    */