import objectStringAnalyser.ObjectStringAnalyserConfig._
import objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMain {
  def main(args: Array[String]): Unit = {
    parseArgs(args).map(_.validate().map(_.run()))
  }

  private def parseArgs(args: Array[String]): Option[ObjectStringAnalyserConfig] = {
    val title =
      """ _____ ______       ___    ___      _________  ________  ________  ___       ________
        ||\   _ \  _   \    |\  \  /  /|    |\___   ___\\   __  \|\   __  \|\  \     |\   ____\
        |\ \  \\\__\ \  \   \ \  \/  / /    \|___ \  \_\ \  \|\  \ \  \|\  \ \  \    \ \  \___|_
        | \ \  \\|__| \  \   \ \    / /          \ \  \ \ \  \\\  \ \  \\\  \ \  \    \ \_____  \
        |  \ \  \    \ \  \   \/  /  /            \ \  \ \ \  \\\  \ \  \\\  \ \  \____\|____|\  \
        |   \ \__\    \ \__\__/  / /               \ \__\ \ \_______\ \_______\ \_______\____\_\  \
        |    \|__|     \|__|\___/ /                 \|__|  \|_______|\|_______|\|_______|\_________\
        |                  \|___|/                                                      \|_________|""".stripMargin
    val parser = new scopt.OptionParser[ObjectStringAnalyserConfig]("Tools") {
      head(title)
      help("help").text("prints this usage text")
      note("")
      cmd("ObjectStringAnalyser")
        .text("Print Scala Object String pretty or Compare two of them")
        .children(
          opt[Mode.Value]('m', "mode").required()
            .action((x, c) => c.copy(mode = x)).text("mode"),
          opt[String]('t', "text").minOccurs(1).unbounded()
            .action((x, c) => c.copy(objStrings = c.objStrings :+ x)).text("input object strings"),
          checkConfig(c => c.validate().map { _ => }.toEither)
        )
    }
    parser.parse(args, ObjectStringAnalyserConfig(Mode.Print, Nil))
  }
}
