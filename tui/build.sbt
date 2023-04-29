This / libraryDependencies += "org.hex" %% "core" % version.value

This / Compile / run / mainClass := Some("service.TuiRestService")

ThisBuild / Docker / packageName := "tui"
