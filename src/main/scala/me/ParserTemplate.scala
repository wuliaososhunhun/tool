package me

import scopt.{OptionDef, OptionParser}

/**
  * Author: yanyang.wang
  * Date: 28/07/2016
  */
abstract class ParserTemplate[T <: ConfigBase[_ <: ToolBase]] extends OptionParser[T]("tool") {
  def wrap(version: String, embed: => OptionDef[Unit, T]) = {
    start(version)
    embed
    end()
  }

  private def start(version: String) = {
    head(ParserTemplate.title, version)
    help("help").text("prints this usage text")
    note("")
  }

  private def end() = {
    checkConfig(c => c.validate().map { _ => }.toEither)
  }
}

object ParserTemplate {
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