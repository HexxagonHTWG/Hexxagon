This / libraryDependencies += "org.hex" %% "provider" % version.value

This / Compile / mainClass := Some("service.PersistenceRestService")

ThisBuild / Docker / dockerExposedPorts := Seq(9091)
