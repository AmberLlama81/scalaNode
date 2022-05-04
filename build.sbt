enablePlugins(ScalaJSPlugin)

// enablePlugins(ScalaJSBundlerPlugin)


name := "Scala.js Tutorial"
scalaVersion := "3.1.2"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq(
                    "com.github.japgolly.scalajs-react" %%% "core" % "2.1.1",
                    "org.http4s" %%% "http4s-dom" % "0.2.1",
                    "org.http4s" %%% "http4s-client" % "0.23.11",
)

/*
Compile / npmDependencies ++= Seq(
    "react" -> "17.0.2",
    "react-dom" -> "17.0.2")

webpackEmitSourceMaps := false/
*/