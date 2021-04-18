val ScalatraVersion = "2.7.1"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "erickdev.xyz"

lazy val hello = (project in file("."))
  .settings(
    name := "SV SQL Generator",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.35.v20201120" % "container",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
      "com.lihaoyi" %% "upickle" % "1.3.8",
      "org.mariadb.jdbc" % "mariadb-java-client" % "2.7.2"
    ),
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
