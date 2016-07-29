package me

import me.common.validator.CollectionValidator
import me.objectStringAnalyser.ObjectStringAnalyserConfig._
import me.objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}
import me.uuid.UuidGeneratorConfig

import scalaz.{-\/, \/-}

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMainDynamic {
  val toolVersion = "dynamic"
  val defaultConfigMap: Map[String, _ <: ConfigBase[_ <: ToolBase]] = Map(
    ObjectStringAnalyserConfig.toolName -> ObjectStringAnalyserConfig(Mode.Print, Nil),
    UuidGeneratorConfig.toolName -> UuidGeneratorConfig(1)
  )

  def main(args: Array[String]): Unit = {
    (CollectionValidator("Input args")(args)(_.nonEmpty) ->
      CollectionValidator("support tools")(defaultConfigMap.keys)(_.exists(_ == args.head))).validate() match {
      case -\/(error) => println("Error: " + error)
      case \/-(()) => dynamicParseArgs(args, defaultConfigMap(args.head)).map(_.validate().map(_.run()))
    }
  }

  private def dynamicParseArgs[T](args: Array[String], defaultConfig: T): Option[_ <: ConfigBase[_ <: ToolBase]] = {
    defaultConfig match {
      case dc: ObjectStringAnalyserConfig =>
        new ParserTemplate[ObjectStringAnalyserConfig] {
          wrap(
            toolVersion,
            cmd(ObjectStringAnalyserConfig.toolName)
              .text("Print Scala Object String pretty or Compare two of them")
              .children(
                opt[Mode.Value]('m', "mode").required()
                  .action((x, c) => c.copy(mode = x))
                  .text("mode"),
                opt[String]('t', "text").minOccurs(1).unbounded()
                  .action((x, c) => c.copy(objStrings = c.objStrings :+ x))
                  .text("input object strings")
              )
          )
        }.parse(args, dc)

      case dc: UuidGeneratorConfig =>
        new ParserTemplate[UuidGeneratorConfig] {
          wrap(
            toolVersion,
            cmd(UuidGeneratorConfig.toolName)
              .text("Generate list of UUID")
              .children(
                opt[Int]('n', "number")
                  .action((x, c) => c.copy(number = x))
                  .text("number of UUID requested")
              )
          )
        }.parse(args, dc)
    }
  }
}

/*object CommandLineMain {
  def dynamicParseArgsImpl[T: c.WeakTypeTag](c: Context)
                                            (args: c.Expr[Array[String]], defaultConfig: c.Expr[T]): c.Expr[Option[T]] = {
    import c.universe._
    val commandParser = defaultConfig.tree.tpe match {
      case ObjectStringAnalyserConfig =>
        reify {
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
        }.tree

    }

    reify {
      val title =
        """ _____ ______       ___    ___      _________  ________  ________  ___       ________
          ||\   _ \  _   \    |\  \  /  /|    |\___   ___\\   __  \|\   __  \|\  \     |\   ____\
          |\ \  \\\__\ \  \   \ \  \/  / /    \|___ \  \_\ \  \|\  \ \  \|\  \ \  \    \ \  \___|_
          | \ \  \\|__| \  \   \ \    / /          \ \  \ \ \  \\\  \ \  \\\  \ \  \    \ \_____  \
          |  \ \  \    \ \  \   \/  /  /            \ \  \ \ \  \\\  \ \  \\\  \ \  \____\|____|\  \
          |   \ \__\    \ \__\__/  / /               \ \__\ \ \_______\ \_______\ \_______\____\_\  \
          |    \|__|     \|__|\___/ /                 \|__|  \|_______|\|_______|\|_______|\_________\
          |                  \|___|/                                                      \|_________|""".stripMargin

    }
  }
}*/
