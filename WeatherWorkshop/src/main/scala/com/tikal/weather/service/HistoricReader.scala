package com.tikal.weather.service

import org.springframework.stereotype.Component
import com.tikal.weather.dao.HistoricalDataDao
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.tikal.weather.model.HistoricalDataMongo
import scala.io.Codec
import scala.io.Source
import scala.collection.JavaConversions.asJavaIterable


@Component
class HistoricReader {
   private val logger: Logger = LoggerFactory.getLogger(classOf[HistoricReader])

  @Autowired
  val dao : HistoricalDataDao = null;
 
   
  def readDataFromFile(fileName: String) = {
    val x = getClass().getClassLoader().getResource(fileName)
    val codec: Codec = Codec.string2codec("Windows-1255");
    // required because of Hebrew in the data file
    val linesAllFiles = Source.fromURL(x)(codec).getLines().toList.drop(1)
    loadLinesToMongo(linesAllFiles)
  }
   
  
  
  
case class WeatherData(
                          val stationName: String,
                          val date: String,
                          val time: String,
                          val temp: String,
                          val maxTemp: String,
                          val minTemp: String,
                          val nearGroundTemp: String,
                          val humidity: String,
                          val rain: String
                        );
  
  def toTime(date: String, time: String): Long = {
    // date is 01-06-2016
    // time is 00:20

    val format = new java.text.SimpleDateFormat("MM-dd-yyyy HH:mm")
    format.parse(date + " " + time).getTime
  }
  
  def loadLinesToMongo(linesToLoad: List[String]): Unit = {
    val lines: List[WeatherData] = linesToLoad.map(line => {
      //logger.info(line);
      val p = line.split(",");
      logger.info("" + p(1) + ", " + p(2) + ", " + p(3) + ", " + p(4));
      val data = new WeatherData(p(1), p(2), "00:00", "-", p(3), p(4), "-", "-", "-");
      data
    })
    def f(arg : (String, Option[String])) : Boolean = {
      arg match {
      case (_, None) => true ;
      case _ => false ;
      }
    }
//    val unknownStations = lines.map ( _.stationName.trim() ).distinct.map { x => (x, mapStationNames.get(x)) }.filter(f).map((arg : (String, Option[String])) => arg._1)
//    if (!unknownStations.isEmpty) logger.warn("unidentified station names: " + unknownStations.mkString(","))
    
    val rtAllData: List[HistoricalDataMongo] = for (line <- lines) yield {
      val rtData: HistoricalDataMongo = new HistoricalDataMongo();
      //logger.info(line.stationName);
      rtData.stationId = line.stationName
      rtData.date = line.date;
      rtData.id = rtData.stationId + "-" + rtData.date;
      rtData.maxTemperature = line.maxTemp;
      rtData.minTemperature = line.minTemp;
      rtData.rain = line.rain;
      rtData.dateTime = toTime(rtData.date, "00:00");
      
      val dayMonthYear = rtData.date.split("-") ;
      rtData.month = dayMonthYear(1).toInt;
      rtData.year = dayMonthYear(2).toInt;
      rtData

    }
    logger.info(s"saving all data");
    val dataByDay = rtAllData.groupBy { item => item.year } .toSeq.sortBy(_._1) ;
    for ((k,v) <- dataByDay) {
      logger.info(s"saving all ${v.size} data items for year $k");
      dao.save(asJavaIterable(v));
    }
  }  
}