package me.uuid

import me.{ConfigBase, ParserTemplate}
import validator.Validator

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UuidGeneratorConfig(number: Int) extends ConfigBase[UuidGenerator] {
  override def validate() = {
    Validator[Int](number, _ > 0).validate().map(_ => UuidGenerator(number))
  }
}

object UuidGeneratorConfig {
  val toolName = "Uuid".toLowerCase

  val parser: ParserTemplate[UuidGeneratorConfig] = new ParserTemplate[UuidGeneratorConfig] {
    wrap(
      cmd(toolName)
        .text("Generate list of UUID")
        .children(
          opt[Int]('n', "number")
            .action((x, c) => c.copy(number = x))
            .text("number of UUID requested")
        )
    )
  }
}
