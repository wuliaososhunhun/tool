name := "tool"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.scalaz" %% "scalaz-core" % "7.2.3",
  "com.chuusai" %% "shapeless" % "2.3.1"
)

val ivyLocal = Resolver.file("local", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns)

val resolvers = Seq(
  ivyLocal,
  Resolver.sonatypeRepo("public")
)
