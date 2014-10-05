package net.ruippeixotog.scalatotext

import net.ruippeixotog.scalatotext.engine.Engine._

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

object Main extends App {
  if(args.length < 1) {
    println("Usage: code-reader <test-case>")
    sys.exit(0)
  }

  val testCases = Vector(
    "class A(x: Int, y: String)",
    "case class B[T, U](x: Int, y: List[T])",
    "case class B[T, U](x: Int, y: List[T]) { def stuff = y.length + x }")

  val tb = currentMirror.mkToolBox()
  val tree = tb.parse(testCases(args(0).toInt))

  println(tree.toText)
}
