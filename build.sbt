/* =====================================================================================================================
 * General Settings
 * ===================================================================================================================== */
ThisBuild / version := {
  val lv = System.getenv("LATEST_VERSION")
  if (lv == null || lv.isBlank) "0.0.0" else lv
}
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "org.hex"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / Test / fork := true

/* =====================================================================================================================
 * GitHub Packages Settings
 * ===================================================================================================================== */
ThisBuild / resolvers += "GitHub HexxagonHTWG Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon"
ThisBuild / publishTo := Some("GitHub HexxagonHTWG Apache Maven Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon")
ThisBuild / publishMavenStyle := true
ThisBuild / credentials := {
  val credFile = Path.userHome / ".sbt" / ".credentials"
  if (credFile.exists())
    Seq(Credentials(credFile))
  else
    Seq(
      Credentials(
        "GitHub Package Registry",
        "maven.pkg.github.com",
        "HexxagonHTWG",
        System.getenv("GITHUB_TOKEN")
      )
    )
}

/* =====================================================================================================================
 * Common Dependencies
 * ===================================================================================================================== */
lazy val http4sVersion = "1.0.0-M39"
lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "ch.qos.logback" % "logback-classic" % "1.4.6",
  "com.lihaoyi" %% "requests" % "0.8.0",
)
lazy val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "com.typesafe" % "config" % "1.4.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
)

/* =====================================================================================================================
 * Project Settings
 * ===================================================================================================================== */
lazy val gui = project
  .settings(
    name := "gui",
    description := "GUI for Hexxagon",
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.scalafx" %% "scalafx" % "20.0.0-R31",
  )
  .dependsOn(core)

lazy val tui = project
  .settings(
    name := "tui",
    description := "TUI for Hexxagon",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(core)

lazy val core = project
  .settings(
    name := "core",
    description := "Core Package for Hexxagon - contains controller",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= http4sDependencies,
  )
  .dependsOn(persistence)

lazy val persistence = project
  .settings(
    name := "persistence",
    description := "Persistence Package for Hexxagon - contains FileIO",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= http4sDependencies,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0", // XML
      "com.lihaoyi" %% "upickle" % "3.1.0", // JSON
      "com.typesafe.play" %% "play-json" % "2.10.0-RC7", // JSON
    )
  )
  .dependsOn(provider)

lazy val provider = project
  .settings(
    name := "provider",
    description := "Provider Package for Hexxagon - contains model",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(utils)

lazy val utils = project
  .settings(
    name := "utils",
    description := "Utils Package for Hexxagon - contains useful classes and traits",
    libraryDependencies ++= commonDependencies
  )

lazy val root = project
  .in(file("./"))
  .settings(
    name := "Hexxagon",
    libraryDependencies ++= commonDependencies,
    publishArtifact := false
  )
  .aggregate(
    gui,
    tui,
    core,
    provider,
    persistence,
    utils
  )
