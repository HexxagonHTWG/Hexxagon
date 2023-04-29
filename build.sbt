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
ThisBuild / fork := true

/* =====================================================================================================================
 * GitHub Packages Settings
 * ===================================================================================================================== */
ThisBuild / resolvers += "GitHub HexxagonHTWG Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon"
ThisBuild / publishTo := Some("GitHub HexxagonHTWG Apache Maven Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon")
ThisBuild / publishMavenStyle := true
ThisBuild / credentials ++= {
  val mvnCredentials = Path.userHome / ".sbt" / ".credentials"
  val dockerCredentials = Path.userHome / ".sbt" / "docker.credentials"
  val defaultRealm = "GitHub Package Registry"
  val defaultUser = "HexxagonHTWG"
  val defaultToken = System.getenv("GITHUB_TOKEN")
  Seq(
    mvnCredentials match {
      case credFile if credFile.exists() => Credentials(credFile)
      case _ => Credentials(
        defaultRealm,
        "maven.pkg.github.com",
        defaultUser,
        System.getenv("GITHUB_TOKEN")
      )
    },
    dockerCredentials match {
      case credFile if credFile.exists() => Credentials(credFile)
      case _ => Credentials(
        defaultRealm,
        "ghcr.io",
        defaultUser,
        System.getenv("GITHUB_TOKEN")
      )
    })
}

/* =====================================================================================================================
 * Docker Settings
 * ===================================================================================================================== */
ThisBuild / Compile / discoveredMainClasses := Seq()
ThisBuild / dockerUpdateLatest := true
ThisBuild / dockerBaseImage := "openjdk:17-jdk"
ThisBuild / Docker / dockerRepository := Some("docker.io")
ThisBuild / Docker / dockerUsername := Some("ostabo")

/* =====================================================================================================================
 * Common Dependencies
 * ===================================================================================================================== */
lazy val http4sVersion = "1.0.0-M39"
lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion
)
lazy val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "com.typesafe" % "config" % "1.4.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.4.6"
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
  .enablePlugins(DockerPlugin, JavaAppPackaging)

lazy val tui = project
  .settings(
    name := "tui",
    description := "TUI for Hexxagon",
    libraryDependencies ++= commonDependencies,
  )
  .dependsOn(core)
  .enablePlugins(DockerPlugin, JavaAppPackaging)

lazy val core = project
  .settings(
    name := "core",
    description := "Core Package for Hexxagon - contains controller",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= http4sDependencies,
  )
  .dependsOn(persistence)
  .enablePlugins(DockerPlugin, JavaAppPackaging)

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
    ),
  )
  .dependsOn(provider)
  .enablePlugins(DockerPlugin, JavaAppPackaging)

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
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0",
  )

lazy val root = project
  .in(file("./"))
  .settings(
    name := "Hexxagon",
    libraryDependencies ++= commonDependencies,
    publishArtifact := false,
    run := {
      (gui / Compile / run).evaluated
      //(tui / Compile / run).evaluated
      (core / Compile / run).evaluated
      (persistence / Compile / run).evaluated
    }
  )
  .aggregate(
    gui,
    tui,
    core,
    provider,
    persistence,
    utils
  )
