package com.tikal.weather.service

import scala.collection.JavaConversions.asScalaBuffer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import com.tikal.weather.dao.RealTimeDataMongoDao
import com.tikal.weather.model.RealTimeDataMongo

@Service
class RealTimeTemperature {

  private val logger: Logger = LoggerFactory.getLogger(classOf[RealTimeTemperature])

  @Autowired
  val dao : RealTimeDataMongoDao = null;

  @Autowired
  val rtService : RTDataService = null ;

  
  /**
   * 
   * This is the final format that the HTML expects to receive
   * 
   * [ ['Time', 'Temperature'], 
    "['00:10', 21.0],
    "['00:20', 21.4],
    "['00:30', 22.0],
    "['00:40', 22.5],
    "['00:50', 23.0],
    "['01:00', 24.0]
    "]
   */
  def temperatureForOneDay(stationId : String, day : String, month : String, year : String): String = {
   
		val dateStr = day + "-" + month + "-" + year
    logger.info("Starting to process temperature data for: stationId: " + stationId + " and date: " + dateStr);
    
    val realTimeDataList = rtService.findByStationAndMonthYear(stationId, month.toInt, year.toInt)
    
    //create a list of tuples ('time', temperature)
    val timeTemperatureTuplesList = realTimeDataList
      .filter (realTimeData => {realTimeData.date == dateStr})
        .map (realTimeData => ("'" + realTimeData.time + "'", realTimeData.temperature))
          .toList
    
    //prepend header
    val timeTemperatureTuplesListWithHeaders = ("'Time'", "'Temperature'") :: timeTemperatureTuplesList
    
    //map tuples to string
    val normalizedStringsList = timeTemperatureTuplesListWithHeaders
    .map{ case (time, temperature) =>  s"[$time,$temperature]"}.toList
    
    //normalize string
    val normalizedStr = normalizedStringsList mkString ","
    return "[" + normalizedStr + "]";
  }

  def minMaxTemperature_old(stationId : String): String = {

    val all: List[RealTimeDataMongo] = asScalaBuffer(dao.findByStationId(stationId)).toList
    val allData = all.map { x => (x.date.split("-")(0), x.minTemperature, x.maxTemperature) }
    val maxMinPerDay =
      for (i <- (1 to 30)) yield {
        (i, allData.filter(t => t._1.toInt == i).map(t => t._3.toDouble).max, allData.filter(t => t._1.toInt == i).map(t => t._2.toDouble).min)
      }
    //val xx = all.map { x => s"[${x.date.split("-")(0)},${x.minTemperature},${x.maxTemperature}]" }
    val xx = maxMinPerDay.map { x => s"[${x._1},${x._3},${x._2}]" }
    logger.info(xx.mkString(","))
    "[ ['Date', 'Min Temperature', 'Max Temperature']," + xx.mkString(",") + "]"
    //"[ [\"Date\", \"Min Temperature\", \"Max Temperature\"]," + xx.mkString(",") + "]"

  }
  
  
  def minMaxTemperature(stationId : String, month : String, year : String): String = {
    logger.info("started");
    val all: List[RealTimeDataMongo] = rtService.findByStationAndMonthYear(stationId, month.toInt, year.toInt)
    //val all: List[RealTimeDataMongo] = rtService.findByStationAndDateRange(stationId, s"01-$month-$year", s"31-$month-$year")
    logger.info("retrieved " + all.size + " items");
    val allData = all.map { x => (x.date.split("-")(0), x.minTemperature, x.maxTemperature) }
    val maxMinPerDay =
      for (i <- (1 to 30)) yield {
        logger.info("working on day " + i);
        val mmax = allData.filter(t => t._1.toInt == i).map(t => toDouble(t._3)).max
        val mmin = allData.filter(t => t._1.toInt == i).map(t => toDouble(t._2)).min
        (i, mmax, mmin)
      }
    //val xx = all.map { x => s"[${x.date.split("-")(0)},${x.minTemperature},${x.maxTemperature}]" }
    val xx = maxMinPerDay.map { x => s"[${x._1},${x._3.getOrElse(0.0)},${x._2.getOrElse(0.0)}]" }
    logger.info(xx.mkString(","))
    "[ ['Date', 'Min Temperature', 'Max Temperature']," + xx.mkString(",") + "]"
    //"[ [\"Date\", \"Min Temperature\", \"Max Temperature\"]," + xx.mkString(",") + "]"

  }
  

  def toDouble(x : String) : Option[Double] = {
    try {
      Some(x.toDouble)
    }
    catch {
      case _ => None 
    }
  }

}