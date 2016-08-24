package me.urlDecoder

import me.{ConfigBase, ParserTemplate}
import validator.Validator

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UrlDecoderConfig(value: String) extends ConfigBase[UrlDecoder] {
  override def validate() = {
    Validator[String](value, _.nonEmpty).validate().map(_ => UrlDecoder(value))
  }
}

object UrlDecoderConfig {
  val toolName = "Url".toLowerCase

  val parser: ParserTemplate[UrlDecoderConfig] = new ParserTemplate[UrlDecoderConfig] {
    wrap(
      cmd(toolName)
        .text("Decode and print url")
        .children(
          opt[String]('v', "value")
            .action((x, c) => c.copy(value = x))
            .text("full url")
        )
    )
  }
}
