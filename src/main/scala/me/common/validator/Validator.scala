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
}

object Validator {
  type Result = \/[String, Unit]

  implicit def validatorListToChain(vs: List[Validator]): ValidatorChain = ValidatorChain(vs)

}

// todo: auto parse objName
case class CollectionSizeValidator(col: Traversable[_], size: Int)(objName: String) extends Validator {
  override def validate(): Result = {
    if (col.size == size) \/-() else -\/(s"expect $size but found ${col.size} in $objName")
  }
}

case class StringEmptyValidator(s: String)(objName: String) extends Validator {
  override def validate(): Result = {
    if (s.isEmpty) \/-() else -\/(s"String $objName should not be empty ")
  }
}
