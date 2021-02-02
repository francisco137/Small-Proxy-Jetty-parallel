name         := "SmallProxyJetty_parallel"
version      := "0.1.0-SNAPSHOT"
organization := "com.example"
scalaVersion := "2.12.4"

val VersionScala       = "2.12.4"

val VersionScalatra    = "2.6.1"
val VersionLogback     = "1.2.3"
val VersionJson4s      = "3.5.3"
val VersionServletApi  = "4.0.0"
val VersionJettyWebapp = "9.4.7.v20170914"

resolvers += Classpaths.typesafeReleases

lazy val small_proxy = (project in file("."))
 .settings(
    libraryDependencies += "org.scalatra"      %% "scalatra"           % VersionScalatra,
    libraryDependencies += "ch.qos.logback"     % "logback-classic"    % VersionLogback     % "runtime",
    libraryDependencies += "org.scalatra"      %% "scalatra-json"      % VersionScalatra,
    libraryDependencies += "org.json4s"        %% "json4s-jackson"     % VersionJson4s,
    libraryDependencies += "org.json4s"        %% "json4s-ext"         % VersionJson4s,
    libraryDependencies += "javax.servlet"      % "javax.servlet-api"  % VersionServletApi  % "provided",
    libraryDependencies += "org.eclipse.jetty"  % "jetty-server"       % VersionJettyWebapp % "container;compile;test",
    libraryDependencies += "org.eclipse.jetty"  % "jetty-webapp"       % VersionJettyWebapp % "container",
    libraryDependencies += "com.lihaoyi"       %% "requests"           % "0.5.1",
    libraryDependencies += "com.typesafe.play" %% "play-json"          % "2.6.9",
    libraryDependencies += "org.scalatest"     %% "scalatest"          % "3.0.5"            % Test,
  )

//libraryDependencies += "com.eed3si9n"      %% "gigahorse-okhttp"   % "0.3.1",

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
enablePlugins(JettyPlugin)

containerPort in Jetty := 8081

