package net.ruippeixotog.scalatotext.engine

object RenderUtils {

  def unCamelCase(str: String) = "[A-Z\\d]".r.replaceAllIn(str.capitalize, " " + _.group(0)).
      tail.filter { ch => ch.isLetterOrDigit || ch.isWhitespace }

  def renderSeq(ls: Seq[String], whenNonEmpty: => String = "%s", whenEmpty: => String = "nothing") =
    ls.length match {
      case 0 => whenEmpty
      case 1 => whenNonEmpty.format(ls.head)
      case n => whenNonEmpty.format(ls.dropRight(1).mkString(", ") + " and " + ls.last)
    }
}
