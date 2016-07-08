package me

import me.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.objectStringAnalyser.ObjectStringAnalyserConfig._
import me.uuid.UuidGeneratorConfig

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMain {

  def main(args: Array[String]): Unit = {
    parseArgs(args).map(config => config.chosenTool match {
      case ObjectStringAnalyserConfig.toolName => config.objStringAnalyser.validate().map(_.run())
      case UuidGeneratorConfig.toolName => config.uuidGenerator.validate().map(_.run())
    })
  }

  private def parseArgs(args: Array[String]): Option[SimpleRootConfig] = {
    import SimpleRootConfig._

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
        .action((x, c) => c.copy(chosenTool = ObjectStringAnalyserConfig.toolName))
        .text("Print Scala Object String pretty or Compare two of them")
        .children(
          opt[Mode.Value]('m', "mode").required()
            .action((x, c) => (objectStringAnalyserConfig >=> osaMode).set(c, x))
            .text("mode"),
          opt[String]('t', "text").minOccurs(1).unbounded()
            .action((x, c) => objectStringAnalyserConfig >=> osaObjStrings mod(_ :+ x, c))
            .text("input object strings")
        )
      note("")
      cmd(UuidGeneratorConfig.toolName)
        .action((x, c) => c.copy(chosenTool = UuidGeneratorConfig.toolName))
        .text("Generate list of UUID")
        .children(
          opt[Int]('n', "number")
            .action((x, c) => (uuidGeneratorConfig >=> ugNumber).set(c, x))
            .text("number of UUID requested")
        )
      checkConfig(c => c.validate().map { _ => }.toEither)
    }

    parser.parse(args, SimpleRootConfig())
  }
}
