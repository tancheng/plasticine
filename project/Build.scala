import sbt._
import Keys._
import java.io.File

object PlasticineBuild extends Build {

  if (System.getProperty("showSuppressedErrors") == null) System.setProperty("showSuppressedErrors", "false")

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := "stanford-ppl",
    publishArtifact in (Compile, packageDoc) := false,

    scalaVersion := "2.11.5",
    scalaSource in Compile <<= baseDirectory(_/"src"),
    scalaSource in Test <<= baseDirectory(_/"tests"),

    libraryDependencies += "org.scala-lang" % "scala-library" % "2.11.5",
    libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.5",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.2",
    libraryDependencies += "edu.berkeley.cs" %% "chisel" % "latest.release",

    retrieveManaged := true,
    scalacOptions += "-Yno-generic-signatures",

    parallelExecution in Test := false,
    concurrentRestrictions in Global := (Tags.limitAll(1) :: Nil)
  )

  lazy val plasticine = Project("plasticine", file("."), settings = buildSettings)
}
