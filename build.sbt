enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)


name := "Scala.js Tutorial"
scalaVersion := "3.1.2"

scalaJSUseMainModuleInitializer := true

libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "2.1.1"

Compile / npmDependencies ++= Seq(
    "react" -> "17.0.2",
    "react-dom" -> "17.0.2")

webpackEmitSourceMaps := false