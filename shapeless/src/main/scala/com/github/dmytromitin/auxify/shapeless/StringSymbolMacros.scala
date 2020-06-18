package com.github.dmytromitin.auxify.shapeless

import macrocompat.bundle
import shapeless.SingletonTypeUtils

import scala.reflect.macros.whitebox

@bundle
class StringSymbolMacros(val c: whitebox.Context) extends SingletonTypeUtils {
  import c.universe.{Symbol => _, _}

//  def mkStringToSymbolImpl[S <: String /*with Singleton*/ : WeakTypeTag]: Tree = {
//    val typ = weakTypeOf[S]
//    val symbolType = SingletonSymbolType.unrefine(typ) match {
//      case ConstantType(Constant(s: String)) => SingletonSymbolType(s)
//      case _ => c.abort(c.enclosingPosition, s"$typ=${showRaw(typ)} is not string singleton type")
//    }
//    q"""_root_.com.github.dmytromitin.auxify.shapeless.StringToSymbol.instance[$typ, $symbolType]() :
//      _root_.com.github.dmytromitin.auxify.shapeless.StringToSymbol.Aux[$typ, $symbolType]"""
//  }
//
//  def mkSymbolToStringImpl[S <: Symbol : WeakTypeTag]: Tree = {
//    val typ = weakTypeOf[S]
//    val stringType = typ/*.dealias*/ match {
//      case SingletonSymbolType(s) => internal.constantType(Constant(s))
//      case _ => c.abort(c.enclosingPosition, s"$typ=${showRaw(typ)} is not symbol singleton type")
//    }
//    q"""_root_.com.github.dmytromitin.auxify.shapeless.SymbolToString.instance[$typ, $stringType]() :
//      _root_.com.github.dmytromitin.auxify.shapeless.SymbolToString.Aux[$typ, $stringType]"""
//  }

  //workaround for 2.12-
//  def stringToSymbolImpl(s: Tree): Tree = {
//    q"""
//      import _root_.shapeless.syntax.singleton._
//      _root_.com.github.dmytromitin.auxify.shapeless.`package`.stringToSymbolHlp($s.narrow)
//    """
//  }

//  def stringToSymbolImpl(s: Tree): Tree = {
//    q"""
//      _root_.com.github.dmytromitin.auxify.shapeless.`package`.stringToSymbolHlp(
//        _root_.shapeless.syntax.singleton.mkSingletonOps($s).narrow
//      )
//    """
//  }

//  def stringToSymbolImpl(s: Tree): Tree = {
//    val stringType = c.typecheck(
//      q"""
//        import _root_.shapeless.syntax.singleton._
//        $s.narrow
//      """
//    ).tpe
//
//    val sts = c.inferImplicitValue(
//      c.typecheck(tq"_root_.com.github.dmytromitin.auxify.shapeless.StringToSymbol[$stringType]", mode = c.TYPEmode).tpe,
//      silent = false
//    )
//
//    val out = sts.tpe.dealias match {
//      case RefinedType(_, scope) if scope.size == 1 => scope.head.typeSignature
//      case typ => c.abort(c.enclosingPosition, s"stringToSymbol: unexpected type $typ=${showRaw(typ)}")
//    }
//
//    val witness = c.inferImplicitValue(
//      c.typecheck(tq"_root_.shapeless.Witness.Aux[$out]", mode = c.TYPEmode).tpe,
//      silent = false
//    )
////    q"$witness.value.asInstanceOf[_root_.shapeless.Witness.Lt[$out]]"
////    q"$witness.asInstanceOf[_root_.shapeless.Witness.Lt[$out]].value"
////    q"$witness.value.asInstanceOf[$out]"
//    q"$witness.value"
//  }

//  def stringToSymbolImpl(s: Tree): Tree = {
//    val stringType = c.typecheck(
//      q"""
//        import _root_.shapeless.syntax.singleton._
//        $s.narrow
//      """
//    ).tpe
//
//    val sts = c.inferImplicitValue(
//      c.typecheck(tq"_root_.com.github.dmytromitin.auxify.shapeless.StringToSymbol[$stringType]", mode = c.TYPEmode).tpe,
//      silent = false
//    )
//
//    val out = sts.tpe.dealias match {
//      case RefinedType(_, scope) if scope.size == 1 => scope.head.typeSignature
//      case typ => c.abort(c.enclosingPosition, s"stringToSymbol: unexpected type $typ=${showRaw(typ)}")
//    }
//
//    extractSingletonValue(out)
//  }

  def stringToSymbolImpl(s: Tree): Tree = {
    val stringType = c.typecheck(
      q"""
        import _root_.shapeless.syntax.singleton._
        $s.narrow
      """
    ).tpe

    SingletonSymbolType.unrefine(stringType) match {
      case ConstantType(Constant(s: String)) => q"_root_.scala.Symbol.apply($s)"
      case typ => c.abort(c.enclosingPosition, s"stringToSymbol: unexpected type $typ=${showRaw(typ)}")
    }
  }

//  def symbolToStringImpl(s: Tree): Tree = {
//    q"""
//      import _root_.shapeless.syntax.singleton._
//      _root_.com.github.dmytromitin.auxify.shapeless.`package`.symbolToStringHlp($s.narrow)
//    """
//  }

  def symbolToStringImpl(s: Tree): Tree = {
    val symbolType = c.typecheck(
      q"""
        import _root_.shapeless.syntax.singleton._
        $s.narrow
      """
    ).tpe

    /*SingletonSymbolType.unrefine*/(symbolType) match {
      case SingletonSymbolType(s) => q"$s"
      case typ => c.abort(c.enclosingPosition, s"symbolToString: unexpected type $typ=${showRaw(typ)}")
    }
  }

  //workaround for 2.10
//  def symbolToStringHlpImpl[S <: Symbol: WeakTypeTag](s: Tree): Tree = {
//    val sts = c.inferImplicitValue(
//      c.typecheck(tq"_root_.com.github.dmytromitin.auxify.shapeless.SymbolToString[${weakTypeOf[S]}]", mode = c.TYPEmode).tpe,
//      silent = false
//    )
//    val out = sts.tpe.dealias match {
//      case RefinedType(_, scope) => scope.head.typeSignature
//      case typ => c.abort(c.enclosingPosition, s"unexpected type $typ=${showRaw(typ)}")
//    }
//    val witness = c.inferImplicitValue(
//      c.typecheck(tq"_root_.shapeless.Witness.Aux[$out]", mode = c.TYPEmode).tpe,
//      silent = false
//    )
//    q"$witness.value"
//  }

//  def stringToSymbolPolyCseImpl[S <: String: WeakTypeTag, S1 <: Symbol: WeakTypeTag]/*(sts: Tree, witness: Tree)*/: Tree = {
//    val s = c.freshName("s")
//    val typS = weakTypeOf[S]
//    val typS1 = weakTypeOf[S1]
//    q"""
//       _root_.com.github.dmytromitin.auxify.shapeless.stringToSymbolPoly.at[$typS].apply[$typS1](
//         new ($typS => $typS1) {
//           override def apply($s: $typS): $typS1 = _root_.com.github.dmytromitin.auxify.shapeless.`package`.stringToSymbol($s)
//         }
//       ): _root_.com.github.dmytromitin.auxify.shapeless.stringToSymbolPoly.Case.Aux[$typS, $typS1]
//       """
//  }
}