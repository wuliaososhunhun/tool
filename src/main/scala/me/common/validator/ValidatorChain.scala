/*package me.common.validator

import scalaz.\/

/**
  * Author: yanyang.wang
  * Date: 16/06/2016
  */
case class ValidatorChain(validators: List[Validator]) extends AnyVal {
  def ->(v: Validator): ValidatorChain = ValidatorChain(validators :+ v)

  def ->(vs: List[Validator]): ValidatorChain = ValidatorChain(validators ++ vs)

  def validate(): Validator.Result = validators.foldLeft(\/.right[String, Unit]())((a, b) => a.flatMap(_ => b.validate()))
}*/
