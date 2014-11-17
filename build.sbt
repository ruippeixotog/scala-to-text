name := "scala-to-text"

organization := "net.ruippeixotog"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scala-lang"    % "scala-reflect"      % scalaVersion.value,
  "org.scala-lang"    % "scala-compiler"     % scalaVersion.value)
