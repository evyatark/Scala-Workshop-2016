package com.tikal.weather.service

import com.tikal.weather.dao.RealTimeDataMongoDao
import com.tikal.weather.model.RealTimeDataMongo
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import scala.io.{Codec, Source}

@Component
class MongoReader {

  private val logger: Logger = LoggerFactory.getLogger(classOf[MongoDisplayService])

  @Autowired
  val d: RealTimeDataMongoDao = null;

  //@PostConstruct
  //  def init() = {
  //    logger.info("init - read file and save to Mongo")
  //    readDataFromFile("data/jamal_june_2016_temp.csv")
  //    logger.info("done");
  //  }

  def readDataFromFile(fileName: String) = {
    var data: RealTimeDataMongo = new RealTimeDataMongo();
    data.dateTime = System.currentTimeMillis();

    val x = getClass().getClassLoader().getResource(fileName)
    val codec: Codec = Codec.string2codec("Windows-1255");
    // required because of Hebrew in the data file
    val linesAllFiles = Source.fromURL(x)(codec).getLines().toList.drop(1)
    loadLinesToMongo(linesAllFiles)
  }

  def loadLinesToMongo(linesToLoad: List[String]): Unit = {
    val lines: List[WeatherData] = linesToLoad.map(line => {
      val p = line.split(",");
      val data = new WeatherData(p(1), p(2), p(4), p(5));
      data
    })
    val rtAllData: List[RealTimeDataMongo] = for (line <- lines) yield {
      val rtData: RealTimeDataMongo = new RealTimeDataMongo();
      rtData.stationId = "7151";
      rtData.date = line.date;
      rtData.time = line.time;
      rtData.maxTemperature = line.maxTemp;
      rtData.minTemperature = line.minTemp;
      rtData.humidity = "-";
      rtData.rain = "-";
      rtData.nearGroundTemperature = "-";
      rtData.temperature = "-";
      rtData.dateTime = toTime(rtData.date, rtData.time);
      rtData

    }
    rtAllData.foreach { rt: RealTimeDataMongo => {
      if (rt.time.equalsIgnoreCase("00:00")) {
        logger.info(s"${rt.date} ${rt.time}")
      }
      d.save(rt);
    }}
  }

  def toTime(date: String, time: String): Long = {
    // date is 01-06-2016
    // time is 00:20

    val format = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm")
    format.parse(date + " " + time).getTime
  }

  case class WeatherData(
                          val date: String,
                          val time: String,
                          val maxTemp: String,
                          val minTemp: String
                        );
}