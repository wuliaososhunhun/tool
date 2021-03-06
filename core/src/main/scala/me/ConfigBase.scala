package me

import scalaz.\/

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
trait ConfigBase[T <: ToolBase] {
  def validate(): \/[String, T]
}
