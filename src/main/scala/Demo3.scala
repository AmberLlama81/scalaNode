import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.{document, window}
import japgolly.scalajs.react.callback.CallbackTo
import scala.scalajs.js

object Demo3:
    trait Alcohol:
        val name: String
        val proof: Int
        val amount: Int
    case object Beer500 extends Alcohol:
        val name = "Cheap American Beer"
        val proof = 13
        val amount = 500
    case class Soju(degree: Int) extends Alcohol:
        val name = "Wonsoju"
        val proof = degree * 2
        val amount = 48

    val Button =
        ScalaComponent.builder[(String, Callback)]
            .render_P((name, callback) => <.button(
                s"$name",
                ^.cls := "mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent", 
                ^.onClick --> callback))
            .build

    case class Drink(when: Long, alcohol: Alcohol)
    case class State(secondsElapsed: Long, drinks: List[Drink])

    class Backend($: BackendScope[Unit, State]) {
        var interval: js.UndefOr[js.timers.SetIntervalHandle] =
            js.undefined

        def tick = 
            $.modState(s => 
                State(s.secondsElapsed + 1, s.drinks)
        )
        
        def start = Callback {
            interval = js.timers.setInterval(1000)(tick.runNow())
        }

        def clear = Callback {
            interval foreach js.timers.clearInterval
            interval = js.undefined
        }

        def render(s: State) =
            <.div(
                 scala.collection.immutable.List(Beer500, Soju(21)).map(a => Button(a.name, $.modState(s => s.copy(drinks = s.drinks :+ Drink(s.secondsElapsed, a))))).toTagMod,
                <.div("Seconds elapsed: ", s.secondsElapsed / 100),
                <.div("Drinks already drunken: ", s.drinks.toString)
            )
    }

    val Timer = ScalaComponent.builder[Unit]
                .initialState(State(0, List.empty))
                .renderBackend[Backend]
                .componentDidMount(_.backend.start)
                .componentWillUnmount(_.backend.clear)
                .build
    
    // def main(as: Array[String]): Unit =
        // Timer().renderIntoDOM(document.getElementById("msg"))