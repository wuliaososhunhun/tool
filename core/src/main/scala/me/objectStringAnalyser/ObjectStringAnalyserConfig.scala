package me.objectStringAnalyser

import me.{ConfigBase, ParserTemplate}
import validator.Validator
import validator.Validator._

import scalaz.-\/

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
case class ObjectStringAnalyserConfig(mode: Mode.Value, objStrings: List[String]) extends ConfigBase[Printer] {

  override def validate() = {
    mode match {
      case Mode.Compare =>
        (Validator[List[String]](objStrings, _.length == 2) ~>
          objStrings.map(objStr => Validator[String](objStr, _.nonEmpty))
          ).validate().map(_ => CompareModePrinter(objStrings.head, objStrings.last))

      case Mode.Print =>
        (Validator[List[String]](objStrings, _.length == 1) ~>
          objStrings.map(objStr => Validator[String](objStr, _.nonEmpty))
          ).validate().map(_ => PrintModePrinter(objStrings.head))

      case Mode.PrintWithIndex =>
        (Validator[List[String]](objStrings, _.length == 1) ~>
          objStrings.map(objStr => Validator[String](objStr, _.nonEmpty))
          ).validate().map(_ => PrintWithIndexModePrinter(objStrings.head))

      case m => -\/(s"Unsupport Mode: $m")
    }
  }
}

object ObjectStringAnalyserConfig {
  val toolName = "ObjectAnalyser".toLowerCase
  implicit val modeRead: scopt.Read[Mode.Value] = scopt.Read.reads(Mode.withName)

  val parser = new ParserTemplate[ObjectStringAnalyserConfig] {
    wrap(
      cmd(toolName)
        .text("Print Scala Object String pretty or Compare two of them")
        .children(
          opt[Mode.Value]('m', "mode").required()
            .action((x, c) => c.copy(mode = x))
            .text("mode: " + Mode.values),
          opt[String]('t', "text").minOccurs(1).unbounded()
            .action((x, c) => c.copy(objStrings = c.objStrings :+ x))
            .text("input object strings")
        )
    )
  }
}
