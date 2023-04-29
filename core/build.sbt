This / libraryDependencies += "org.hex" %% "persistence" % version.value

This / Compile / run / mainClass := Some("service.CoreRestService")

ThisBuild / Docker / dockerExposedPorts := Seq(9090)
ThisBuild / Docker / packageName := "core"
