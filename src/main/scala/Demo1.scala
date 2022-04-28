import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.document
import japgolly.scalajs.react.callback.CallbackTo

object Demo1:
    val Button =
        ScalaComponent.builder[String]
            .render_P(name => <.button(
                s"Hi? $name",
                ^.cls := "mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent", 
                ^.onClick --> Callback {println("Have a good one!")}))
            .build
    
    val MTrack =
        def mmove(e: ReactMouseEvent): Callback =
            Callback{println(s"${e.clientX}, ${e.clientY}")}
        ScalaComponent.builder[Unit]
            .renderStatic(
                <.div(
                    "Hello, world!",
                    ^.width := "500px",
                    ^.height := "500px",
                    ^.background := "blue",
                    ^.border := "1px solid red",
                    ^.onClick --> Callback {println("Clicked")},
                    ^.onMouseMove ==> mmove
                )
            )
            .build

    case class CellProps(str: String, cb: Callback)
    case class CellState(bgcolor: String)
    class CellBackend($: BackendScope[CellProps, CellState]):
        def menter = $.modState(s => s.copy(bgcolor =  "white"))
        def mleave = $.modState(s => s.copy(bgcolor = "yellow"))
        def render(p: CellProps, s: CellState): VdomElement =
            <.div(
                p.str,
                ^.width := "50px",
                ^.height := "50px",
                ^.background := s.bgcolor,
                ^.border := "1px solid black",
                ^.onMouseEnter --> menter,
                ^.onMouseLeave --> mleave,
                ^.onClick --> p.cb
            )
    val Cell =
        ScalaComponent.builder[CellProps]
            .initialState(CellState("yellow"))
            .renderBackend[CellBackend]
            .build

    case class TableState(msg: String)
    class TableBackend($: BackendScope[Unit, TableState]):
        def render(s: TableState) = 
            <.div(
                (2 to 9).map(r =>
                    <.div(
                        ^.display := "flex",
                        (2 to 9).map(c => Cell(CellProps(s"$r X $c", $.modState(s => s.copy(msg = s"$r X $c = ${r * c}"))))).toTagMod
                    )
                ).toTagMod,
                <.div(s.msg)
            )
    val Table =
        ScalaComponent.builder[Unit]
            .initialState(TableState("click!"))
            .renderBackend[TableBackend]
            .build

    def main(as: Array[String]): Unit = 
        println("Hello, World!")
        /*
        val names = List("Nathan", "Stella", "Clue")
        <.div(
            names.map(name => Button(name)).toTagMod
        ).renderIntoDOM(document.getElementById("msg"))
        */
        MTrack().renderIntoDOM(document.getElementById("msg"))
        Table().renderIntoDOM(document.getElementById("msg"))