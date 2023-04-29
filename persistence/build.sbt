This / libraryDependencies += "org.hex" %% "provider" % version.value

This / Compile / run / mainClass := Some("service.PersistenceRestService")

ThisBuild / Docker / dockerExposedPorts := Seq(9091)
ThisBuild / Docker / packageName := "persistence"
