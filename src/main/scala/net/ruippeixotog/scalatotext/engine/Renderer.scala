package net.ruippeixotog.scalatotext.engine

import scala.reflect.runtime.universe._

abstract class Renderer extends RendererUtils {
  type TreeRenderer = PartialFunction[Tree, String]

  private[this] var finalTreeRenderer: TreeRenderer = Map.empty
  private[this] var fallbackRenderer: Tree => String = { cd =>
    throw new Exception(s"Cannot render text for $cd (with type ${cd.getClass})")
  }

  def on(renderer: TreeRenderer): Unit = {
    finalTreeRenderer = finalTreeRenderer.orElse(renderer)
  }

  def default(renderer: Tree => String): Unit = {
    fallbackRenderer = renderer
  }

  @inline final def render(expr: Tree): String =
    finalTreeRenderer.lift(expr).getOrElse(fallbackRenderer(expr))
}
