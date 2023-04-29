This / coverageEnabled := false

This / libraryDependencies += "org.hex" %% "core" % version.value

This / Compile / run / mainClass := Some("service.GuiRestService")

ThisBuild / Docker / packageName := "gui"
