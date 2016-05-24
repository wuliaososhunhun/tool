package objectStringAnalyser

import scala.annotation.tailrec

/**
  * Author: yanyang.wang
  * Date: 23/05/2016
  */
case class Tree(value: String, nodes: List[Tree]) {
  def prettyPrint(level: Int): Unit = {
    Tree.printSpace(2 * level)
    print(value)
    if (nodes.nonEmpty) {
      Tree.printLeftBracket()
      Tree.prettyPrintNodes(nodes, level + 1)
      Tree.printNewLine()
      Tree.printSpace(2 * level)
      Tree.printRightBracket()
    }
  }
}

object Tree {
  val leftBracket = '('
  val rightBracket = ')'
  val comma = ','

  def printLeftBracket(): Unit = print(leftBracket)

  def printRightBracket(): Unit = print(rightBracket)

  def printComma(): Unit = print(comma)

  def printSpace(number: Int): Unit = print(" " * number)

  def printNewLine(): Unit = println()

  @tailrec
  def prettyPrintNodes(nodes: List[Tree], level: Int): Unit = {
    nodes match {
      case Nil => // do nothing
      case head :: Nil =>
        Tree.printNewLine()
        head.prettyPrint(level)
      case head :: tail =>
        Tree.printNewLine()
        head.prettyPrint(level)
        Tree.printComma()
        prettyPrintNodes(tail, level)
    }
  }
}

