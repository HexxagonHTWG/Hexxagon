lazy val commonDependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
)

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "3.2.2",
  organization := "org.hex",

  target := {
    baseDirectory.value / "target" / "scala-3.2.2"
  },

  libraryDependencies ++= commonDependencies,

  jacocoReportSettings := JacocoReportSettings(
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
    "utf-8"),

  jacocoExcludes := Seq(
  ),
)

lazy val gui = (project in file("gui"))
  .settings(
    name := "gui",
    commonSettings,
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
    },
  )
  .dependsOn(core, provider, persistence, utils)

lazy val tui = (project in file("tui"))
  .settings(
    name := "tui",
    commonSettings,
  )
  .dependsOn(core, provider, persistence, utils)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    commonSettings
  )
  .dependsOn(provider, persistence, utils)

lazy val provider = (project in file("provider"))
  .settings(
    name := "provider",
    commonSettings
  )
  .dependsOn(utils)

lazy val persistence = (project in file("persistence"))
  .settings(
    name := "persistence",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-xml" % "2.1.0", // XML
      "com.lihaoyi" %% "upickle" % "3.0.0", // JSON
      ("com.typesafe.play" %% "play-json" % "2.9.3").cross(CrossVersion.for3Use2_13), // JSON
    )
  )
  .dependsOn(provider, utils)

lazy val utils = (project in file("utils"))
  .settings(
    name := "utils",
    commonSettings
  )

lazy val root = project
  .in(file("./"))
  .settings(
    name := "Hexxagon",
    commonSettings
  )
  .aggregate(
    gui,
    tui,
    core,
    provider,
    persistence,
    utils
  )
