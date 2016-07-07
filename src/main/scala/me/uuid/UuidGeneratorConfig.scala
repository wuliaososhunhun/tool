package me.uuid

import me.ConfigBase
import me.common.validator.IntValidator

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UuidGeneratorConfig(number: Int) extends ConfigBase[UuidGenerator] {
  override def validate() = {
    IntValidator("number")(number)(_ > 0).validate.map(_ => UuidGenerator(number))
  }
}

object UuidGeneratorConfig {
  val toolName = "UuidGenerator"
}
