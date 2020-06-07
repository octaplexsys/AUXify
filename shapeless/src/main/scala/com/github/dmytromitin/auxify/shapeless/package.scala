package com.github.dmytromitin.auxify

import _root_.shapeless.Witness
import scala.reflect.macros.whitebox

package object shapeless {
  import scala.language.experimental.macros

  def stringToSymbol[S <: String with Singleton, S1 <: Symbol](s: S)(implicit
                                                                     sts: StringToSymbol.Aux[S, S1],
                                                                     valueOf: Witness.Aux[S1]): S1 = valueOf.value
  def symbolToStringHlp[S <: Symbol, S1 <: String with Singleton](s: S)(implicit
                                                                        sts: SymbolToString.Aux[S, S1],
                                                                        valueOf: /*ValueOf*/Witness.Aux[S1]): S1 = valueOf.value
  def symbolToString(s: Symbol): String = macro symbolToStringImpl
  def symbolToStringImpl(c: whitebox.Context)(s: c.Tree): c.Tree = {
    import c.universe._
    q"""
      import _root_.shapeless.syntax.singleton._
      _root_.com.github.dmytromitin.auxify.shapeless.`package`.symbolToStringHlp($s.narrow)
    """
  }
}
