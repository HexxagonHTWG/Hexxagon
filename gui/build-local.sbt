This / libraryDependencies += "org.hex" %% "core" % sys.env.getOrElse("LATEST_VERSION", "0.0.0")

This / libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "com.typesafe" % "config" % "1.4.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.6"
)
This / libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31"

This / Compile / mainClass := Some("service.GuiRestService")

This / scalaVersion := "3.2.1"
