package validator

import validator.Validator._

import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import scalaz.{-\/, \/, \/-}

/**
  * Author: yanyang.wang
  * Date: 15/06/2016
  */
class Validator[T](obj: T, cond: T => Boolean, errorMsg: String) {
  def validate(): Result = {
    if (cond(obj)) \/-()
    else -\/(errorMsg)
  }
}

object Validator {
  type Result = \/[String, Unit]

  implicit def validatorToChain(v: Validator[_]): ValidatorChain = ValidatorChain(List(v))

  implicit class ValidatorChain(val validators: List[Validator[_]]) extends AnyVal {
    def ~>(v: Validator[_]): ValidatorChain = ValidatorChain(validators :+ v)

    def ~>(vs: ValidatorChain): ValidatorChain = ValidatorChain(validators ++ vs.validators)

    def validate(): Validator.Result = validators.foldLeft(\/.right[String, Unit]())((a, b) => a.flatMap(_ => b.validate()))
  }

  def apply[T](obj: T, cond: T => Boolean): Validator[T] = macro validatorMacro[T]

  def validatorMacro[T: c.WeakTypeTag](c: whitebox.Context)(obj: c.Tree, cond: c.Tree) = {
    import c.universe._

    def polishConditionCode(objCode: String, condCode: String) = {
      println("+++")
      println(objCode)
      println(condCode)

      val pattern = "^(\\()\\((x\\$\\d+)(:.*? => )(.*)$".r
      pattern findFirstIn condCode match {
        case Some(pattern(g1, g2, _, g5)) =>
          (g1 + g5).replace(g2, objCode)
        case None => condCode
      }
    }

    val objCode =  showCode(obj).split('.').last
    val condCode = polishConditionCode(objCode, showCode(cond))

    val msgTemplate = s"Object $objCode(%s) does not meet condition$condCode"
    q"new Validator($obj, $cond, $msgTemplate.format($obj))"
  }
}
