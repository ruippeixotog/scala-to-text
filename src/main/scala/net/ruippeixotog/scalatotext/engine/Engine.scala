package net.ruippeixotog.scalatotext.engine

import net.ruippeixotog.scalatotext.engine.RenderUtils._

import scala.reflect.runtime.universe._

object Engine {
  type TreeRenderer = PartialFunction[Tree, String]

  implicit class RichTree(val tree: Tree) extends AnyVal {
    def toText = renderTree(tree)
  }

  val classDefRenderer: TreeRenderer = {
    case q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$earlydefns } with ..$parents { $self => ..$stats }" =>

      val parentsStr = renderSeq(
        parents.filterNot(_.toString.contains("AnyRef")).map { s => unCamelCase(s.toString) },
        "extends %s", "doesn't extend any class or trait")

      val tparamsStr = renderSeq(tparams.map(renderTree),
        "It receives type parameters %s.", "")

      val paramssStr = renderSeq(paramss.head.map(renderTree),
        "It receives parameters %s.", "It receives no parameters.")

      s"The class ${unCamelCase(tpname.toString)} $parentsStr. $tparamsStr $paramssStr"
  }

  val valDefRenderer: TreeRenderer = {
    case q"$mods val $tname: $tpt = $expr" =>
      s"${unCamelCase(tname.toString)} of type ${tpt.toText}"
  }

  val typeDefRenderer: TreeRenderer = {
    case TypeDef(mods: Modifiers, name: TypeName, tparams: List[TypeDef], rhs: Tree) =>

      renderSeq(tparams.map(renderTree),
        "${unCamelCase(name.toString)} of %s", unCamelCase(name.toString))
  }

  val appliedTypeTreeRenderer: TreeRenderer = {
    case tq"$tpt[..$tpts]" =>
      renderSeq(tpts.map(renderTree), s"${tpt.toText} of %s", tpt.toText)
  }

  val identRenderer: TreeRenderer = {
    case Ident(name) => unCamelCase(name.toString)
  }

  val errorRenderer: TreeRenderer = {
    case cd => throw new Exception(
      s"Cannot render text for $cd (with type ${cd.getClass})")
  }

  val finalTreeRenderer =
    List(identRenderer, classDefRenderer, valDefRenderer, typeDefRenderer, appliedTypeTreeRenderer,
      errorRenderer).reduce(_.orElse(_))

  @inline final def renderTree(expr: Tree): String = finalTreeRenderer(expr)
}
