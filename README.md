# Scala to Text

A playground project for describing arbitrary Scala code in plain English.

```scala
scala> import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.currentMirror

scala> import scala.tools.reflect.ToolBox
import scala.tools.reflect.ToolBox

scala> import net.ruippeixotog.scalatotext.engine.Engine._
import net.ruippeixotog.scalatotext.engine.Engine._

scala> val tree = currentMirror.mkToolBox().parse("case class B[T, U](x: Int, y:
 List[T])")
warning: there was one feature warning; re-run with -feature for details
tree: _1.u.Tree forSome { val _1: scala.tools.reflect.ToolBox[reflect.runtime.un
iverse.type] } =
case class B[T, U] extends scala.Product with scala.Serializable {
  <caseaccessor> <paramaccessor> val x: Int = _;
  <caseaccessor> <paramaccessor> val y: List[T] = _;
  def <init>(x: Int, y: List[T]) = {
    super.<init>();
    ()
  }
}

scala> tree.toText
res0: String = The class B extends Scala Product and Scala Serializable. It rece
ives type parameters T and U. It receives parameters X of type Int and Y of type
 List of T.
```
