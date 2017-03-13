import sbt._

object Dependencies {
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.4.17"
  val netty = "io.netty" % "netty-codec-http2" % "4.1.8.Final"

  val circeParser = "io.circe" %% "circe-parser" % "0.7.0"
  val circeGeneric = "io.circe" %% "circe-generic" % "0.7.0"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.17"

  val alpn = "org.mortbay.jetty.alpn" % "alpn-boot" % "8.1.11.v20170118" % "runtime"

  def connectorDeps(scalaVersion: String): Seq[ModuleID] =
    Seq(akkaStream, netty) ++
      Seq(circeParser).map(_ % Provided) ++
      Seq(scalaTest, akkaStreamTestkit, circeGeneric).map(_ % Test)

  // http://www.eclipse.org/jetty/documentation/current/alpn-chapter.html#alpn-versions
  val examplesDeps = Seq(circeParser, alpn)
}
