package me

import _root_.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.uuid.UuidGeneratorConfig
import shapeless.HMap

/**
  * Author: yanyang.wang
  * Date: 07/07/2016
  */
class NameMap[K, V]

case class RootConfig private(configs: HMap[NameMap])

object RootConfig {
  implicit val nameToObjectStringAnalyserConfig = new NameMap[String, ObjectStringAnalyserConfig]
  implicit val nameToUuidGeneratorConfig = new NameMap[String, UuidGeneratorConfig]

  def defaultConfig(): RootConfig = {
    val map = HMap[NameMap](
      ObjectStringAnalyserConfig.toolName -> ObjectStringAnalyserConfig(Mode.Print, Nil),
      UuidGeneratorConfig.toolName -> UuidGeneratorConfig(1)
    )
    RootConfig(map)
  }
}
