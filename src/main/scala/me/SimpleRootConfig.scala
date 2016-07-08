package me

import me.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.uuid.UuidGeneratorConfig

import scalaz.{-\/, \/}

/**
  * Author: yanyang.wang
  * Date: 08/07/2016
  */
case class SimpleRootConfig(choosenTool: String = "",
                            objStringAnalyser: ObjectStringAnalyserConfig = ObjectStringAnalyserConfig(Mode.Print, Nil),
                            uuidGenerator: UuidGeneratorConfig = UuidGeneratorConfig(1)) {
  def validate(): \/[String, Any] = {
    choosenTool match {
      case ObjectStringAnalyserConfig.toolName => objStringAnalyser.validate()
      case UuidGeneratorConfig.toolName => uuidGenerator.validate()
      case _ => -\/(s"Tool $choosenTool is not supported")
    }
  }
}
