package objectStringAnalyser

import me.ToolBase

/**
  * Author: yanyang.wang
  * Date: 12/06/2016
  */
sealed trait Printer extends ToolBase

private[objectStringAnalyser] case class PrintModePrinter(objString: String) extends Printer {
  // "ContainerDeliveredToTransitionLocationEventV1(HeaderV1(2016-05-23T14:00:53.194Z[UTC],1b568fa6-dd74-46df-b41f-a48df067d84f,None),DecantZoneAmbient,ContainerV1(List(PositionFullContentV1(VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),0,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List()), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),1,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(KnownInventoryBatchV1(IB-121431,55555,2020-01-01T12:00Z[UTC],Some(2020-08-15T12:00Z[UTC]),Some(PurchaseOrder#1),KNOWN,QuantityV1(5,EACH))),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000001)),Some(DL00000001),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),2,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000002)),Some(DL00000002),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),3,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000003)),Some(DL00000003),Some(Map()))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,FREEZER_TOTE,None,Some(FT4060330)),Some(FT4060330),Some(Map(reservable -> false)))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,RAINBOW_TOTE,None,Some(RT1234567)),Some(RT1234567),Some(Map(isLined -> false, reservable -> false))),52506182404407,ContainerDeliveredToTransitionLocationEvent)"
  override def run(): Unit = {
    Tree.showTreeNodes(TreeParser.parseNode(objString), 0).foreach(println)
  }
}

private[objectStringAnalyser] case class PrintWithIndexModePrinter(objString: String) extends Printer {
  override def run(): Unit = {
    val rawTree = Tree.showTreeNodes(TreeParser.parseNode(objString), 0)
    val indexWidth = rawTree.length.toString.length
    rawTree.zipWithIndex.foreach(sAndI => println(generateIndex(sAndI._2 + 1, indexWidth) + sAndI._1))
  }

  private def generateIndex(index: Int, indexWidth: Int): String = " " * (indexWidth - index.toString.size) + index + Tree.indent
}
