//This / coverageEnabled := false

This / libraryDependencies += "org.hex" % "core_3" % "0.0.0"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "com.typesafe" % "config" % "1.4.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.6"
)
libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31"

This / Compile / mainClass := Some("service.GuiRestService")

scalaVersion := "3.2.1"
