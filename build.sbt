lazy val commonDependencies = Seq(
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % "test",
  "org.scalafx" %% "scalafx" % "16.0.0-R24",
  "org.scala-lang.modules" %% "scala-xml" % "2.1.0", // XML
  "com.lihaoyi" %% "upickle" % "3.0.0", // JSON
  ("com.typesafe.play" %% "play-json" % "2.9.3").cross(CrossVersion.for3Use2_13), // JSON
  "org.openjfx" % "javafx-base" % "16" classifier (System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  })
)

lazy val commonSettings = Seq(
  target := {
    baseDirectory.value / "target" / "scala-3.2.2"
  },

  libraryDependencies ++= commonDependencies,

  jacocoReportSettings := JacocoReportSettings(
    "Jacoco Coverage Report",
    None,
    JacocoThresholds(),
    Seq(JacocoReportFormats.ScalaHTML, JacocoReportFormats.XML), // note XML formatter
    "utf-8"),

  jacocoExcludes := Seq(
    "*aview.*",
    "*HexModule.*",
    "*Main.*"
  ),

  jacocoCoverallsServiceName := "github-actions",
  jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
  jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
  jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
)

lazy val gui = (project in file("gui"))
  .settings(
    name := "gui",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val tui = (project in file("tui"))
  .settings(
    name := "tui",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val core = (project in file("core"))
  .settings(
    name := "core",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val provider = (project in file("provider"))
  .settings(
    name := "provider",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val persistence = (project in file("persistence"))
  .settings(
    name := "persistence",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val utils = (project in file("utils"))
  .settings(
    name := "utils",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "3.2.2",
    commonSettings
  )

lazy val root = project
  .in(file("./"))
  .settings(
    name := "Hexxagon",
    version := "0.1.0-SNAPSHOT",
    fork / run := true, // not pretty but fixes error on second startup
    connectInput / run := true, // for fork and TUI
    scalaVersion := "3.2.2",
    commonSettings
  )
  .enablePlugins(JacocoCoverallsPlugin)
