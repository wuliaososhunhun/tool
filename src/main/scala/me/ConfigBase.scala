package me

import scopt.OptionDef

import scalaz._
import Scalaz._

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
abstract class ConfigBase[T <: ToolBase](toolName: String) {
  //def parserCmd(): OptionDef
  def validate(): \/[String, T]
}
