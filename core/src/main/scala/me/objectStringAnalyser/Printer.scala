package me.objectStringAnalyser

import me.ToolBase

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
sealed trait Printer extends ToolBase

object Printer {
  type HeadGenerator = (Int, Int) => String

  def genLine(line: String, index: Int, indexStringWidth: Int, generator: HeadGenerator): String = generator(index, indexStringWidth) + line

  val genIndex: HeadGenerator = (index, indexStringWidth) => " " * (indexStringWidth - index.toString.length) + index + Tree.indent

  val genArrow: HeadGenerator = (index, indexStringWidth) => "=" * (indexStringWidth - index.toString.length - 1) + ">" + index + Tree.indent
}

private[objectStringAnalyser] case class PrintModePrinter(objString: String) extends Printer {
  // "ContainerDeliveredToTransitionLocationEventV1(HeaderV1(2016-05-23T14:00:53.194Z[UTC],1b568fa6-dd74-46df-b41f-a48df067d84f,None),DecantZoneAmbient,ContainerV1(List(PositionFullContentV1(VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),0,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List()), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),1,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(KnownInventoryBatchV1(IB-121431,55555,2020-01-01T12:00Z[UTC],Some(2020-08-15T12:00Z[UTC]),Some(PurchaseOrder#1),KNOWN,QuantityV1(5,EACH))),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000001)),Some(DL00000001),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),2,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000002)),Some(DL00000002),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),3,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000003)),Some(DL00000003),Some(Map()))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,FREEZER_TOTE,None,Some(FT4060330)),Some(FT4060330),Some(Map(reservable -> false)))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,RAINBOW_TOTE,None,Some(RT1234567)),Some(RT1234567),Some(Map(isLined -> false, reservable -> false))),52506182404407,ContainerDeliveredToTransitionLocationEvent)"
  override def run(): Unit = {
    Tree.showTreeNodes(TreeParser.parseNode(objString), 0).foreach(println)
  }
}

private[objectStringAnalyser] case class PrintWithIndexModePrinter(objString: String) extends Printer {
  override def run(): Unit = {
    val rawTree = Tree.showTreeNodes(TreeParser.parseNode(objString), 0)
    val indexStringWidth = rawTree.length.toString.length
    rawTree.zipWithIndex.map(sAndI => Printer.genLine(sAndI._1, sAndI._2 + 1, indexStringWidth, Printer.genIndex)).foreach(println)
  }
}

private[objectStringAnalyser] case class CompareModePrinter(objString1: String, objString2: String) extends Printer {
  override def run(): Unit = {
    val rawTree1 = Tree.showTreeNodes(TreeParser.parseNode(objString1), 0)
    val rawTree2 = Tree.showTreeNodes(TreeParser.parseNode(objString2), 0)

    val maxLength = Math.max(rawTree1.length, rawTree2.length)
    val indexStringWidth = maxLength.toString.length + 2

    val (tree1, tree2) = rawTree1.zipAll(rawTree2, "", "").zipWithIndex.map {
      ssAndI =>
        val t1 = ssAndI._1._1
        val t2 = ssAndI._1._2
        val index = ssAndI._2 + 1
        if (areTupleValuesEqual(ssAndI._1)) {
          (Printer.genLine(t1, index, indexStringWidth, Printer.genIndex), Printer.genLine(t2, index, indexStringWidth, Printer.genIndex))
        } else {
          (Printer.genLine(t1, index, indexStringWidth, Printer.genArrow), Printer.genLine(t2, index, indexStringWidth, Printer.genArrow))
        }
    }.unzip
    tree1.foreach(println)
    println("=" * 10)
    tree2.foreach(println)
  }

  private def areTupleValuesEqual(t: (String, String)): Boolean = t._1 == t._2
}
