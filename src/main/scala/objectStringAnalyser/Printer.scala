package objectStringAnalyser

import scala.annotation.tailrec

/**
  * Author: yanyang.wang
  * Date: 23/05/2016
  */
object Printer {
  def main(args: Array[String]) {
    val s = "ContainerDeliveredToTransitionLocationEventV1(HeaderV1(2016-05-23T14:00:53.194Z[UTC],1b568fa6-dd74-46df-b41f-a48df067d84f,None),DecantZoneAmbient,ContainerV1(List(PositionFullContentV1(VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),0,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List()), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),1,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(KnownInventoryBatchV1(IB-121431,55555,2020-01-01T12:00Z[UTC],Some(2020-08-15T12:00Z[UTC]),Some(PurchaseOrder#1),KNOWN,QuantityV1(5,EACH))),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000001)),Some(DL00000001),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),2,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000002)),Some(DL00000002),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),3,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000003)),Some(DL00000003),Some(Map()))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,FREEZER_TOTE,None,Some(FT4060330)),Some(FT4060330),Some(Map(reservable -> false)))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,RAINBOW_TOTE,None,Some(RT1234567)),Some(RT1234567),Some(Map(isLined -> false, reservable -> false))),52506182404407,ContainerDeliveredToTransitionLocationEvent)"
    Tree.prettyPrintNodes(parseNode(s.toCharArray.toSeq, 0 ,StringBuilder.newBuilder, List.empty), 0)
  }

  @tailrec
  def unwrap(s: Seq[Char], value: StringBuilder): (String, Seq[Char]) = {
    s match {
      case Nil => (value.toString, Nil)
      case Tree.leftBracket +: tail => (value.toString, tail.dropRight(1))
      case c +: tail => unwrap(tail, value.append(c))
    }
  }

 // @tailrec
  def parseNode(string: Seq[Char], unclosedBracket: Int, builder: StringBuilder, result: List[Tree]): List[Tree] = {
    def buildTreeNode(): Tree = {
      val (value, children) = unwrap(builder.toString.toCharArray, StringBuilder.newBuilder)
      Tree(value, parseNode(children, 0, StringBuilder.newBuilder, List.empty))
    }

    string match {
      case Nil if builder.isEmpty => result
      case Nil => parseNode(Nil, 0, StringBuilder.newBuilder, result :+ buildTreeNode())
      case Tree.leftBracket +: tail => parseNode(tail, unclosedBracket + 1, builder.append(Tree.leftBracket), result)
      case Tree.rightBracket +: tail => parseNode(tail,unclosedBracket - 1, builder.append(Tree.rightBracket), result)
      case Tree.comma +: tail if unclosedBracket == 0 => parseNode(tail, 0, StringBuilder.newBuilder, result :+ buildTreeNode())
      case c +: tail => parseNode(tail, unclosedBracket, builder.append(c), result)
    }
  }
}

