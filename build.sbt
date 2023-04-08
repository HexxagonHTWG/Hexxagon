ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.2.2"
ThisBuild / organization := "org.hex"
ThisBuild / target := {
  baseDirectory.value / "target" / "scala-3.2.2"
}

// publish to github packages settings
ThisBuild / publishTo := Some("GitHub HexxagonHTWG Apache Maven Packages" at "https://maven.pkg.github.com/HexxagonHTWG/Hexxagon")
ThisBuild / publishMavenStyle := true
ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "HexxagonHTWG",
  System.getenv("GITHUB_TOKEN")
)

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
ThisBuild / jacocoExcludes := Seq(
  "**.Gui*",
  "**.GUI*",
  "**.TuiService*"
)

lazy val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test"
)

lazy val gui = project
  .settings(
    name := "gui",
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
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(core)

lazy val core = project
  .settings(
    name := "core",
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(persistence)

lazy val persistence = project
  .settings(
    name := "persistence",
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
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(utils)

lazy val utils = project
  .settings(
    name := "utils",
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
