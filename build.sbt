name := "storm-scala-init"

version := "1.0"

scalaVersion := "2.9.1"

resolvers ++= Seq("clojars" at "http://clojars.org/repo/",
  "clojure-releases" at "http://build.clojure.org/releases")

libraryDependencies += "com.github.velvia" % "scala-storm_2.9.1" % "0.2.2"




