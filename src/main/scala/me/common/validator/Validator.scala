package me.common.validator

import me.common.validator.Validator._

import scalaz.{-\/, \/, \/-}

/**
  * Author: yanyang.wang
  * Date: 15/06/2016
  */
sealed trait Validator {
  def validate(): Result
  def ->(v: Validator): ValidatorChain = List(this, v)
  def ->(vs: List[Validator]): ValidatorChain = this +: vs
}

object Validator {
  type Result = \/[String, Unit]

  //implicit def validatorListToChain(vs: List[Validator]): ValidatorChain = ValidatorChain(vs)
  implicit class ValidatorChain(val validators: List[Validator]) extends AnyVal {
    def ->(v: Validator): ValidatorChain = ValidatorChain(validators :+ v)

    def ->(vs: List[Validator]): ValidatorChain = ValidatorChain(validators ++ vs)

    def validate(): Validator.Result = validators.foldLeft(\/.right[String, Unit]())((a, b) => a.flatMap(_ => b.validate()))
  }
}

// todo: auto parse objName
case class CollectionSizeValidator(objName: String)(col: Traversable[_])(size: Int) extends Validator {
  override def validate(): Result = {
    if (col.size == size) \/-() else -\/(s"expect $size elements but found ${col.size} in $objName")
  }
}

case class CollectionValidator(objName: String)(col: Traversable[_])(conds: (Traversable[_] => Boolean)*) extends Validator {
  override def validate(): Result = {
    if (conds.forall(_(col))) \/-()
    else -\/(s"Collection $objName does not meet all condition $conds")
  }
}

case class StringEmptyValidator(objName: String)(s: String) extends Validator {
  override def validate(): Result = {
    if (s.isEmpty) -\/(s"String $objName should not be empty ") else \/-()
  }
}

case class IntValidator(objName: String)(i: Int)(conds: (Int => Boolean)*) extends Validator {
  override def validate: Result = {
    if (conds.forall(_(i))) \/-()
    else -\/(s"Int $i does not meet all condition $conds")
  }
}
