package me.common.validator

import me.common.validator.Validator._

import scalaz.{-\/, \/, \/-}

/**
  * Author: yanyang.wang
  * Date: 15/06/2016
  */
sealed trait Validator {
  def validate(): Result
}

object Validator {
  type Result = \/[String, Unit]

  implicit def validatorToChain(v: Validator): ValidatorChain = ValidatorChain(List(v))

  implicit class ValidatorChain(val validators: List[Validator]) extends AnyVal {
    def ~>(v: Validator): ValidatorChain = ValidatorChain(validators :+ v)

    def ~>(vs: ValidatorChain): ValidatorChain = ValidatorChain(validators ++ vs.validators)

    def validate(): Validator.Result = validators.foldLeft(\/.right[String, Unit]())((a, b) => a.flatMap(_ => b.validate()))
  }
}

case class ObjectValidator[T](objName: String)(obj: T)(cond: T => Boolean) extends Validator {
  override def validate(): Result = {
    if (cond(obj)) \/-()
    else -\/(s"$objName $obj does not meet $cond")
  }
}
