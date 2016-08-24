package me.uuid

import me.ToolBase

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UuidGenerator(number: Int) extends ToolBase{
  override def run(): Unit = {
    (1 to number).foreach(_ => println(java.util.UUID.randomUUID.toString))
  }
}
