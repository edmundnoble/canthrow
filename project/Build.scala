import sbt._
import Keys._
import org.scalajs.sbtplugin.{OptimizerOptions, ScalaJSPlugin}
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

object CanThrowBuild extends Build {

  val deps = libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test"
  )

  val otherSettings: Seq[sbt.Def.Setting[_]] = Seq(
    name := "canthrow",
    version := "0.0.1",
    scalaVersion := "2.11.7",
    persistLauncher in Compile := true,
    persistLauncher in Test := false,
    relativeSourceMaps := true
  )

  val sets: Seq[Def.Setting[_]] = (Defaults.projectCore ++ otherSettings) :+ deps

  lazy val root = Project(id = "root", base = file("."))
    .settings(scalaVersion := "2.11.7")
    .settings(sets)

  lazy val core = Project(id = "shared", base = file("shared"))
    .settings(sets)

}