import objectStringAnalyser.ObjectStringAnalyserConfig._
import objectStringAnalyser.{Mode, ObjectStringAnalyserConfig}

/**
  * Author: yanyang.wang
  * Date: 10/06/2016
  */
object CommandLineMain {
  def main(args: Array[String]): Unit = {
    //val config = ObjectStringAnalyserConfig(Mode.Print, List("ContainerDeliveredToTransitionLocationEventV1(HeaderV1(2016-05-23T14:00:53.194Z[UTC],1b568fa6-dd74-46df-b41f-a48df067d84f,None),DecantZoneAmbient,ContainerV1(List(PositionFullContentV1(VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),0,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List()), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),1,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(KnownInventoryBatchV1(IB-121431,55555,2020-01-01T12:00Z[UTC],Some(2020-08-15T12:00Z[UTC]),Some(PurchaseOrder#1),KNOWN,QuantityV1(5,EACH))),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000001)),Some(DL00000001),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),2,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000002)),Some(DL00000002),Some(Map())))), PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),3,List(),List(ContainerV1(List(PositionFullContentV1(VersionV1(1,2020-08-15T12:00Z[UTC]),0,List(),List())),VersionV1(1,2020-08-15T12:00Z[UTC]),Set(),ContainerDescriptorV1(None,None,FREEZER_BAG,None,Some(DL00000003)),Some(DL00000003),Some(Map()))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,FREEZER_TOTE,None,Some(FT4060330)),Some(FT4060330),Some(Map(reservable -> false)))))),VersionV1(0,2014-07-05T21:24:53.557Z[UTC]),Set(MOVABLE),ContainerDescriptorV1(None,None,RAINBOW_TOTE,None,Some(RT1234567)),Some(RT1234567),Some(Map(isLined -> false, reservable -> false))),52506182404407,ContainerDeliveredToTransitionLocationEvent)"))
    //config.validate().map(_.run())

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
