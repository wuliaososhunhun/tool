package me.common.validator

import scalaz.\/

/**
  * Author: yanyang.wang
  * Date: 16/06/2016
  */
case class ValidatorChain(validators: List[Validator]) {
  def ->(v: Validator): ValidatorChain = ValidatorChain(validators :+ v)

  def validate(): Validator.Result = validators.foldLeft(\/.right[String, Unit]())((a, b) => a.flatMap(_ => b.validate()))
}
