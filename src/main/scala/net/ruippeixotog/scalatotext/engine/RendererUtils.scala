package net.ruippeixotog.scalatotext.engine

import scala.reflect.runtime.universe._

trait RendererUtils { renderer: Renderer =>

  implicit class RichAny[A](self: A) {
    def unCamelCase: String = unCamelCase()

    def unCamelCase(fullName: Boolean = false): String = {
      val s0 = if(fullName) self.toString else self.toString.split('.').last
      "[A-Z\\d]".r.replaceAllIn(s0, " " + _.group(0)).trim
    }
  }

  implicit class RichSeq[A](self: Seq[A]) {

    def renderStrSeq(whenNonEmpty: String = "%s", whenEmpty: String = "")(
      implicit ev: A =:= String): String = renderSeq(identity(_), whenNonEmpty, whenEmpty)

    def renderTreeSeq(whenNonEmpty: String = "%s", whenEmpty: String = "")(
      implicit ev: A <:< Tree): String = renderSeq(render(_), whenNonEmpty, whenEmpty)

    def renderSeq(f: A => String, whenNonEmpty: String = "%s", whenEmpty: String = ""): String =
      self.length match {
        case 0 => whenEmpty
        case 1 => whenNonEmpty.format(f(self.head))
        case n => whenNonEmpty.format(self.map(f).dropRight(1).mkString(", ") + " and " + f(self.last))
      }
  }

  implicit class RichTree(val tree: Tree) {
    def render = renderer.render(tree)
  }
}
