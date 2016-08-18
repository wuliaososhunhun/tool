package me.uuid

import me.common.validator.ObjectValidator
import me.{ConfigBase, ParserTemplate}

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UuidGeneratorConfig(number: Int) extends ConfigBase[UuidGenerator] {
  override def validate() = {
    ObjectValidator("number")(number)(_ > 0).validate().map(_ => UuidGenerator(number))
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
