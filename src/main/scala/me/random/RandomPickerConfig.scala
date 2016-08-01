package me.random

import me.common.validator.IntValidator
import me.{ConfigBase, ParserTemplate}

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class RandomPickerConfig(number: Int, maxInt: Int, values: List[String]) extends ConfigBase[RandomPicker] {
  override def validate() = {
    (IntValidator("number")(number)(_ > 0) ->
      IntValidator("maxInt")(maxInt)(_ > 0))
      .validate().map(_ => RandomPicker(number, maxInt, values))
  }
}

object RandomPickerConfig {
  val toolName = "Random".toLowerCase

  val parser: ParserTemplate[RandomPickerConfig] = new ParserTemplate[RandomPickerConfig] {
    wrap(
      cmd(toolName)
        .text("Pick a random value")
        .children(
          opt[Int]('n', "number")
            .action((x, c) => c.copy(number = x))
            .text("number of value required"),
          opt[Int]('m', "max")
            .action((x, c) => c.copy(maxInt = x))
            .text("pick random from [0, m) when no values input"),
          opt[Seq[String]]('v', "values")
            .action((x, c) => c.copy(values = x.toList))
            .text("pick random value from comma separated values input")
        )
    )
  }
}
