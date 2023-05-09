name := "protohackers"

version := "1.0"

scalaVersion := "2.13.10"

lazy val akkaVersion = "2.8.1"
lazy val logbackVersion = "1.4.6"
lazy val scalaTestVersion = "3.2.15"

fork := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)
