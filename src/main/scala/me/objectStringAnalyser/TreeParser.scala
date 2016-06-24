package objectStringAnalyser

import scala.annotation.tailrec

/**
  * Author: yanyang.wang
  * Date: 23/05/2016
  */
object TreeParser {
  def parseNode(s: String): List[Tree]= parseNodeRec(s.toCharArray.toVector, 0 ,StringBuilder.newBuilder, List.empty)

  @tailrec
  private def unwrap(s: Vector[Char], value: StringBuilder): (String, Vector[Char]) = {
    s match {
      case IndexedSeq() => (value.toString, Vector.empty)
      case Tree.leftBracket +: tail => (value.toString, tail.dropRight(1))
      case c +: tail => unwrap(tail, value.append(c))
    }
  }

  private def parseNodeRec(string: Vector[Char], unclosedBracket: Int, builder: StringBuilder, result: List[Tree]): List[Tree] = {
    def buildTreeNode(): Tree = {
      val (value, children) = unwrap(builder.toString.toCharArray.toVector, StringBuilder.newBuilder)
      Tree(value, parseNodeRec(children, 0, StringBuilder.newBuilder, List.empty))
    }

    string match {
      case IndexedSeq() if builder.isEmpty => result
      case IndexedSeq() => parseNodeRec(Vector.empty, 0, StringBuilder.newBuilder, result :+ buildTreeNode())
      case Tree.leftBracket +: tail => parseNodeRec(tail, unclosedBracket + 1, builder.append(Tree.leftBracket), result)
      case Tree.rightBracket +: tail => parseNodeRec(tail,unclosedBracket - 1, builder.append(Tree.rightBracket), result)
      case Tree.comma +: tail if unclosedBracket == 0 => parseNodeRec(tail, 0, StringBuilder.newBuilder, result :+ buildTreeNode())
      case c +: tail => parseNodeRec(tail, unclosedBracket, builder.append(c), result)
    }
  }
}

