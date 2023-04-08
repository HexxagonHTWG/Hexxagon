/* =====================================================================================================================
 * General Settings
 * ===================================================================================================================== */
ThisBuild / version := "0.1.1-SNAPSHOT"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "org.hex"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / target := {
  baseDirectory.value / "target" / "scala-3.2.2"
}

/* =====================================================================================================================
 * GitHub Packages Settings
 * ===================================================================================================================== */
ThisBuild / resolvers += "GitHub HexxagonHTWG Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon"
ThisBuild / publishTo := Some("GitHub HexxagonHTWG Apache Maven Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon")
ThisBuild / publishMavenStyle := true
ThisBuild / credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
ThisBuild / publish / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "HexxagonHTWG",
  System.getenv("GITHUB_TOKEN")
)

/* =====================================================================================================================
 * Jacoco Settings
 * ===================================================================================================================== */
ThisBuild / jacocoReportSettings := JacocoReportSettings(
  "Jacoco Coverage Report",
  None,
  JacocoThresholds(
    instruction = 0,
    method = 0,
    branch = 0,
    complexity = 0,
    line = 0,
    clazz = 0
  ),
  Seq(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML),
  "utf-8"
)

/* =====================================================================================================================
 * Project Settings
 * ===================================================================================================================== */
lazy val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test"
)

lazy val gui = project
  .settings(
    name := "gui",
    description := "GUI for Hexxagon",
    libraryDependencies ++= commonDependencies,
    libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24",
    libraryDependencies ++= {
      // Determine OS version of JavaFX binaries
      lazy val osName = System.getProperty("os.name") match {
        case n if n.startsWith("Linux") => "linux"
        case n if n.startsWith("Mac") => "mac"
        case n if n.startsWith("Windows") => "win"
        case _ => throw new Exception("Unknown platform!")
      }
      Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
        .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
    }
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
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(persistence)

lazy val persistence = project
  .settings(
    name := "persistence",
    description := "Persistence Package for Hexxagon - contains FileIO",
    libraryDependencies ++= commonDependencies,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0", // XML
      "com.lihaoyi" %% "upickle" % "3.0.0", // JSON
      "com.typesafe.play" %% "play-json" % "2.9.3" cross CrossVersion.for3Use2_13 // JSON
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
