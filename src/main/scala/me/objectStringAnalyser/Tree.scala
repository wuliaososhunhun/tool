package objectStringAnalyser

import scala.annotation.tailrec

/**
  * Author: yanyang.wang
  * Date: 23/05/2016
  */
case class Tree(value: String, nodes: List[Tree]) {
  def show(level: Int): List[String] = {
    val indent = Tree.indent * level
    val firstLine = indent + value
    if (nodes.nonEmpty) {
      (firstLine + Tree.leftBracket) +: Tree.showTreeNodes(nodes, level + 1) :+ (indent + Tree.rightBracket)
    } else {
      firstLine :: Nil
    }
  }
}

object Tree {
  val leftBracket = '('
  val rightBracket = ')'
  val comma = ','
  val indent = " " * 2

  def showTreeNodes(nodes: List[Tree], level: Int): List[String] = {
    @tailrec
    def showTreeNodesRec(n: List[Tree], l: Int, result: List[String]): List[String] = {
      n match {
        case Nil => result
        case head :: Nil => result ++ head.show(level)
        case head :: tail =>
          showTreeNodesRec(tail, l, result ++ appendToLast(head.show(level), Tree.comma.toString))
      }
    }
    showTreeNodesRec(nodes, level, Nil)
  }

  private def appendToLast(list: List[String], string: String): List[String] = {
    @tailrec
    def appendToLastRec(l: List[String], s: String, result: List[String]): List[String] = {
      l match {
        case Nil => result
        case head :: Nil => result :+ (head + s)
        case fst :: snd :: rest => appendToLastRec(snd :: rest, s, result :+ fst)
      }
    }
    appendToLastRec(list, string, Nil)
  }
}

