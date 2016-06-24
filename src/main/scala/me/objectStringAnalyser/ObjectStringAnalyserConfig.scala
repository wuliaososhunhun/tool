package objectStringAnalyser

import me.ConfigBase
import me.common.validator.{CollectionSizeValidator, StringEmptyValidator}
import scopt.OptionDef

import scalaz.-\/

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
case class ObjectStringAnalyserConfig(mode: Mode.Value, objStrings: List[String]) extends ConfigBase[Printer]("objStringAnalyser") {
  private val objStringsValidatorTemplate = CollectionSizeValidator("objStrings")(objStrings)(_)
  private val objStringValidatorTemplate = StringEmptyValidator("objString")(_)

  //override def parserCmd(): OptionDef = ???
  override def validate() = {
    mode match {
      case Mode.Compare => -\/(s"Unsupport Mode: Compare")

      case Mode.Print =>
        (objStringsValidatorTemplate(1)
          -> objStrings.map(objStringValidatorTemplate)
          ).validate().map(_ => PrintModePrinter(objStrings.head))

      case m => -\/(s"Unsupport Mode: $m")
    }
  }
}

object ObjectStringAnalyserConfig {
  implicit val modeRead: scopt.Read[Mode.Value] = scopt.Read.reads(Mode.withName)
}
