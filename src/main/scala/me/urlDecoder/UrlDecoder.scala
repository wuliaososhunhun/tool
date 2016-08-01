package me.urlDecoder

import java.net.URLDecoder

import me.ToolBase

/**
  * Author: yanyang.wang
  * Date: 02/07/2016
  */
case class UrlDecoder(value: String) extends ToolBase {
  private val indent = "  "
  override def run(): Unit = {
    URLDecoder.decode(value, "UTF-8").split("\\?").toList match {
      case Nil => println("Empty input string")
      case domain :: params =>
        println(domain)
        params.foreach(extractParams(_).foreach(t => println(indent + t._1 + " = " + t._2.sorted.mkString(","))))
    }
  }

  private def extractParams(value: String): Map[String, List[String]] = {
    value.split('&').foldLeft(Map[String, List[String]]()){ (map, pair) =>
      val head :: tail = pair.split("=", 2).toList
      map + (head -> (map.getOrElse(head, List.empty) ++ tail))
    }
  }
}
