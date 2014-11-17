package net.ruippeixotog.scalatotext

import net.ruippeixotog.scalatotext.engine.Renderer

import scala.reflect.runtime.universe._

class ScalaTextRenderer extends Renderer {

  // Ident(name: Name)
  on { case Ident(name) => name.unCamelCase }

  // Select(ref: Tree, tpname: Name)
  // ---
  // e.g. scala.collection.immutable.List
  // ---
  on { case tq"$ref.$tpname" => tpname.unCamelCase }

  // ValDef(mods: Modifiers, tname: TermName, tpt: Tree, expr: Tree)
  // ---
  // e.g. protected val x: Int = 0
  // ---
  on { case q"$mods val $tname: $tpt = $expr" =>
    s"${tname.unCamelCase} of type ${tpt.render}"
  }

  // TypeDef(mods: Modifiers, name: TypeName, tparams: List[TypeDef], rhs: Tree)
  // ---
  // e.g. type M[A] = List[A]
  // ---
  on { case q"$mods type $tpname[..$tparams] = $tpt" =>
    tpname.unCamelCase + tparams.renderTreeSeq(" of %s")
  }

  // AppliedTypeTree(tpt: Tree, tpts: List[Tree])
  // ---
  // e.g. List[A] (appearing as the type of a parameter)
  // ---
  on { case tq"$tpt[..$tpts]" =>
    tpt.render + tpts.renderTreeSeq(" of %s")
  }

  // ClassDef(mods: Modifiers, tpname: TypeName, tparams: List[TypeDef],
  //   Template(parents: List[Tree], self: ValDef, stats: List[Tree]))
  // ----
  // ** it also extracts from `stats` the primary constructor of the class as:
  //   DefDef(ctorMods: Modifiers, _, _, paramss: List[List[ValDef]], _, _)**
  // ----
  // e.g. final class Foo[A] private(x: List[A]) extends Serializable { thiz => ... }
  //
  on { case q"$mods class $tpname[..$tparams] $ctorMods(...$paramss) extends { ..$_ } with ..$parents { $self => ..$stats }" =>
    val filteredParents = parents.filterNot(_.toString.contains("AnyRef"))

    val parentsStr = filteredParents.renderTreeSeq("extends %s", "doesn't extend any class or trait")
    val tparamsStr = s"It receives ${tparams.renderTreeSeq("type parameters %s", "no type parameters")}."
    val paramssStr = s"It receives ${paramss.head.renderTreeSeq("parameters %s", "no parameters")}."

    s"The class ${tpname.unCamelCase} $parentsStr. $tparamsStr $paramssStr"
  }
}
