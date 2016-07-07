import me.uuid.UuidGeneratorConfig
import objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import ObjectStringAnalyserConfig._

import scalaz.{-\/, \/}

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMain {

  def main(args: Array[String]): Unit = {
    parseArgs(args).map(config => config.choosenTool match {
      case ObjectStringAnalyserConfig.toolName => config.objStringAnalyser.validate().map(_.run())
      case UuidGeneratorConfig.toolName => config.uuidGenerator.validate().map(_.run())
    })
  }

  private def parseArgs(args: Array[String]): Option[SimpleRootConfig] = {
    val title =
      """ _____ ______       ___    ___      _________  ________  ________  ___       ________
        ||\   _ \  _   \    |\  \  /  /|    |\___   ___\\   __  \|\   __  \|\  \     |\   ____\
        |\ \  \\\__\ \  \   \ \  \/  / /    \|___ \  \_\ \  \|\  \ \  \|\  \ \  \    \ \  \___|_
        | \ \  \\|__| \  \   \ \    / /          \ \  \ \ \  \\\  \ \  \\\  \ \  \    \ \_____  \
        |  \ \  \    \ \  \   \/  /  /            \ \  \ \ \  \\\  \ \  \\\  \ \  \____\|____|\  \
        |   \ \__\    \ \__\__/  / /               \ \__\ \ \_______\ \_______\ \_______\____\_\  \
        |    \|__|     \|__|\___/ /                 \|__|  \|_______|\|_______|\|_______|\_________\
        |                  \|___|/                                                      \|_________|""".stripMargin
    val parser = new scopt.OptionParser[SimpleRootConfig]("Tools") {
      head(title)
      help("help").text("prints this usage text")
      note("")
      cmd(ObjectStringAnalyserConfig.toolName)
        .action((x, c) => c.copy(choosenTool = ObjectStringAnalyserConfig.toolName))
        .text("Print Scala Object String pretty or Compare two of them")
        .children(
          opt[Mode.Value]('m', "mode").required()
            .action((x, c) => c.copy(objStringAnalyser = c.objStringAnalyser.copy(mode = x))).text("mode"),
          opt[String]('t', "text").minOccurs(1).unbounded()
            .action { (x, c) =>
              val config = c.objStringAnalyser
              c.copy(objStringAnalyser = config.copy(objStrings = config.objStrings :+ x))
            }.text("input object strings")
        )
      note("")
      cmd(UuidGeneratorConfig.toolName)
        .action((x, c) => c.copy(choosenTool = UuidGeneratorConfig.toolName))
        .text("Generate list of UUID")
        .children(
          opt[Int]('n', "number")
            .action((x, c) => c.copy(uuidGenerator = c.uuidGenerator.copy(number = x))).text("number of UUID requested")
        )
      checkConfig(c => c.validate().map { _ => }.toEither)
    }

    parser.parse(args, SimpleRootConfig())
  }
}

case class SimpleRootConfig(choosenTool: String = "",
                            objStringAnalyser: ObjectStringAnalyserConfig = ObjectStringAnalyserConfig(Mode.Print, Nil),
                            uuidGenerator: UuidGeneratorConfig = UuidGeneratorConfig(1)) {
  def validate(): \/[String, Any] = {
    choosenTool match {
      case ObjectStringAnalyserConfig.toolName => objStringAnalyser.validate()
      case UuidGeneratorConfig.toolName => uuidGenerator.validate()
      case _ => -\/(s"Tool $choosenTool is not supported")
    }
  }
}
