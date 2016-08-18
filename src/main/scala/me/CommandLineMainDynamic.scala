package me

import me.common.validator.ObjectValidator
import me.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.random.RandomPickerConfig
import me.urlDecoder.UrlDecoderConfig
import me.uuid.UuidGeneratorConfig

import scalaz.{-\/, \/-}

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMainDynamic {
  val defaultConfigMap: Map[String, _ <: ConfigBase[_ <: ToolBase]] = Map(
    ObjectStringAnalyserConfig.toolName -> ObjectStringAnalyserConfig(Mode.Print, Nil),
    UuidGeneratorConfig.toolName -> UuidGeneratorConfig(1),
    RandomPickerConfig.toolName -> RandomPickerConfig(10, 100, Nil),
    UrlDecoderConfig.toolName -> UrlDecoderConfig("")
  )

  def main(args: Array[String]): Unit = {
    (ObjectValidator("Input args")(args)(_.nonEmpty) ~>
      ObjectValidator("support tools")(defaultConfigMap.keys)(_.exists(_ == args.head.toLowerCase))).validate() match {
      case -\/(error) => println("Error: " + error)
      case \/-(()) => dynamicParseArgs(args, defaultConfigMap(args.head.toLowerCase)).map(_.validate().map(_.run()))
    }
  }

  private def dynamicParseArgs[T <: ConfigBase[_ <: ToolBase]](args: Array[String], defaultConfig: T): Option[_ <: ConfigBase[_ <: ToolBase]] = {
    defaultConfig match {
      case dc: ObjectStringAnalyserConfig => ObjectStringAnalyserConfig.parser.parse(args, dc)
      case dc: UuidGeneratorConfig => UuidGeneratorConfig.parser.parse(args, dc)
      case dc: RandomPickerConfig => RandomPickerConfig.parser.parse(args, dc)
      case dc: UrlDecoderConfig => UrlDecoderConfig.parser.parse(args, dc)
    }
  }
}
