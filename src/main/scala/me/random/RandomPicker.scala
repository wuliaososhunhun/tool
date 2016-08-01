package me.random

import me.ToolBase

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class RandomPicker(number: Int, maxInt: Int, values: List[String]) extends ToolBase {
  override def run(): Unit = {
    val size: Int = values match {
      case Nil => maxInt
      case _ => values.size
    }
    (1 to number).foreach { _ =>
      val rand = (Math.random() * size).toInt
      if (values.isEmpty) {
        println(rand)
      } else {
        println(values(rand))
      }
    }
  }
}
