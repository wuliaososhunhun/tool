package me

import me.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.uuid.UuidGeneratorConfig

import scalaz.{Lens, -\/, \/}

/**
  * Author: yanyang.wang
  * Date: 08/07/2016
  */
case class SimpleRootConfig(chosenTool: String = "",
                            objStringAnalyser: ObjectStringAnalyserConfig = ObjectStringAnalyserConfig(Mode.Print, Nil),
                            uuidGenerator: UuidGeneratorConfig = UuidGeneratorConfig(1)) {
  def validate(): \/[String, Any] = {
    chosenTool match {
      case ObjectStringAnalyserConfig.toolName => objStringAnalyser.validate()
      case UuidGeneratorConfig.toolName => uuidGenerator.validate()
      case _ => -\/(s"Tool $chosenTool is not supported")
    }
  }
}

object SimpleRootConfig {
  // ideally generate this by macro: e.g. https://github.com/gseitz/Lensed
  val objectStringAnalyserConfig = Lens.lensu[SimpleRootConfig, ObjectStringAnalyserConfig](
    (a, value) => a.copy(objStringAnalyser = value),
    _.objStringAnalyser
  )

  val osaMode = Lens.lensu[ObjectStringAnalyserConfig, Mode.Value](
    (a, value) => a.copy(mode = value),
    _.mode
  )

  val osaObjStrings = Lens.lensu[ObjectStringAnalyserConfig, List[String]](
    (a, value) => a.copy(objStrings = value),
    _.objStrings
  )

  val uuidGeneratorConfig = Lens.lensu[SimpleRootConfig, UuidGeneratorConfig](
    (a, value) => a.copy(uuidGenerator = value),
    _.uuidGenerator
  )

  val ugNumber = Lens.lensu[UuidGeneratorConfig, Int](
    (a, value) => a.copy(number = value),
    _.number
  )
}
