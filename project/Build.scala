import sbt._
import Keys._

object ModuleBuild extends Build {

  lazy val buildVersion  =  "1.0-SNAPSHOT"
  lazy val playVersion   =  "2.0"

  lazy val moduleName    = "play2-job"

  lazy val releases      = "C:/project/maven-repo/release"
  lazy val snapshot      = "C:/project/maven-repo/snapshots"
  lazy val repo          = if (buildVersion.endsWith("SNAPSHOT")) snapshot else releases

  lazy val play =  "play" %% "play" % playVersion

  lazy val root = Project(id = moduleName, base = file("."), settings = Project.defaultSettings).settings(
    version := buildVersion,
    publishTo := Some(Resolver.file("maven-repo", file(repo))),
    resolvers ++= Seq(
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "hina0118 Maven Repository" at "https://github.com/hina0118/maven-repo/raw/master/release"
    ),
    javacOptions ++= Seq("-encoding", "utf8"),
    libraryDependencies += play,
    crossPaths := false,
    libraryDependencies += "cron4j" % "cron4j" % "2.2.5"
  )
}
