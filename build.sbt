name := "tool"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
)

val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

val resolvers = Seq(
  ivyLocal,
  "TypeSafe Releases" at "http://maven/nexus/content/repositories/typesafe-releases/",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)
