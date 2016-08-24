import sbt.Keys._

name := "tool"

val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers ++= Seq(
    ivyLocal,
    Resolver.sonatypeRepo("public")
  )
)

lazy val core = (project in file("core")).settings(commonSettings: _*).dependsOn(validator)

lazy val validator = (project in file("validator")).settings(commonSettings: _*)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(mainClass in(Compile, run) := Some("me.CommandLineMainDynamic"))
  .aggregate(core).dependsOn(core)
  .aggregate(validator).dependsOn(validator)
