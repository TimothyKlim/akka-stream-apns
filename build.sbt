import Dependencies._

lazy val commonSettings = Seq(
  organization := "com.reactivehub",
  scalaVersion := crossScalaVersions.value.head,
  crossScalaVersions := Seq("2.11.8", "2.12.1"),
  scalacOptions := Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Xfuture",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  ),
  licenses := Seq(
    ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),
  scmInfo := Some(
    ScmInfo(url("https://github.com/reactive-hub/akka-stream-apns"),
            "git@github.com:reactive-hub/akka-stream-apns.git")),
  bintrayOrganization := Some("timothyklim"),
  publishMavenStyle := true,
  pomIncludeRepository := (_ ⇒ false),
  pomExtra := (
    <developers>
      <developer>
        <id>marcelmojzis</id>
        <name>Marcel Mojzis</name>
      </developer>
    </developers>
  ),
  releaseTagComment := s"Release ${(version in ThisBuild).value}",
  releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val `akka-stream-apns` = (project in file("."))
  .aggregate(connector, examples, benchmarks)
  .settings(moduleName := "akka-stream-apns-root")
  .settings(commonSettings)
  .settings(noPublishSettings)

lazy val connector = (project in file("connector"))
  .settings(moduleName := "akka-stream-apns")
  .settings(commonSettings)
  .settings(libraryDependencies ++= connectorDeps(scalaVersion.value))

lazy val examples = (project in file("examples"))
  .dependsOn(connector)
  .settings(moduleName := "akka-stream-apns-examples")
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(libraryDependencies ++= examplesDeps)
  .settings(Seq(
    fork := true,
    javaOptions ++= (for {
      file <- (managedClasspath in Runtime).value.map(_.data)
      path = file.getAbsolutePath
      if path.contains("alpn-boot")
    } yield s"-Xbootclasspath/p:$path")
  ))

lazy val benchmarks = (project in file("benchmarks"))
  .enablePlugins(JmhPlugin)
  .dependsOn(connector % "compile;compile->test")
  .settings(moduleName := "akka-stream-apns-benchmarks")
  .settings(commonSettings)
  .settings(noPublishSettings)
