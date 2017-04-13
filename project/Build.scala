import sbt._
import Keys._
import java.io.File

object PIRBuild extends Build {
	if (System.getProperty("showSuppressedErrors") == null) System.setProperty("showSuppressedErrors", "false")

	val bldSettings = Defaults.defaultSettings ++ Seq (
     //organization := "stanford-ppl",
		publishArtifact in (Compile, packageDoc) := false,

		scalaSource in Compile <<= baseDirectory(_ / "src"),
		scalaSource in Test <<= baseDirectory(_ / "test"),
    logBuffered in Test := false,

    retrieveManaged := true,
    javaOptions in (Test) += "-Xdebug",
    javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005",
    //scalacOptions += "-Yno-generic-signatures",
    scalacOptions += "-feature",
    scalacOptions += "-unchecked",
    scalacOptions += "-deprecation",
    autoAPIMappings := true,
    scalacOptions in (Compile, doc) ++= Seq(
      "-doc-root-content", 
      baseDirectory.value+"/root-doc.txt",
      "-diagrams", // Generate Dot Diagram in Scalar Doc for Inheritance Hierarchy
      "-diagrams-debug",
      //"-diagrams-dot-timeout", "20", "-diagrams-debug",
      "-doc-title", name.value
    ),

    parallelExecution in Test := false,
    concurrentRestrictions in Global := (Tags.limitAll(1) :: Nil)
  )

	lazy val plasticine = Project("plasticine", file("."), settings = bldSettings)

  lazy val apps = Project("apps", file("apps"), 
        settings = bldSettings, 
        dependencies=Seq(plasticine % "compile->compile;test->test") // Allow ScalaTest of apps accesss ScalaTest of plasticine
      )
}

