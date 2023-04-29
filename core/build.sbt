This / libraryDependencies += "org.hex" %% "persistence" % version.value

This / Compile / mainClass := Some("service.CoreRestService")

ThisBuild / Docker / dockerExposedPorts := Seq(9090)
