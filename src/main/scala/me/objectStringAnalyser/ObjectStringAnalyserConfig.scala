package objectStringAnalyser

import me.ConfigBase
import me.common.validator.{CollectionSizeValidator, StringEmptyValidator}

import scalaz.-\/

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
case class ObjectStringAnalyserConfig(mode: Mode.Value, objStrings: List[String]) extends ConfigBase[Printer] {
  private val objStringsValidatorTemplate = CollectionSizeValidator("objStrings")(objStrings)(_)
  private val objStringValidatorTemplate = StringEmptyValidator("objString")(_)

  override def validate() = {
    mode match {
      case Mode.Compare => (objStringsValidatorTemplate(2) ->
        objStrings.map(objStringValidatorTemplate)).validate().map(_ => CompareModePrinter(objStrings.head, objStrings.last))

      case Mode.Print =>
        defaultPrintValidator.validate().map(_ => PrintModePrinter(objStrings.head))

      case Mode.PrintWithIndex =>
        defaultPrintValidator.validate().map(_ => PrintWithIndexModePrinter(objStrings.head))

      case m => -\/(s"Unsupport Mode: $m")
    }
  }

  private def defaultPrintValidator =
    objStringsValidatorTemplate(1) ->
      objStrings.map(objStringValidatorTemplate)
}

object ObjectStringAnalyserConfig {
  val toolName = "ObjectStringAnalyser"
  implicit val modeRead: scopt.Read[Mode.Value] = scopt.Read.reads(Mode.withName)
}
