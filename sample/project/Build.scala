import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "sample"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "play2-job" % "play2-job" % "1.0-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    resolvers += "Local Maven Repository" at "file:///C:/project/maven-repo/snapshots",
    )

}
