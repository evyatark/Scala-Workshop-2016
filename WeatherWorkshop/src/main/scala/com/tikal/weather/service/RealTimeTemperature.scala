package com.tikal.weather.service

import org.springframework.stereotype.Service
//import com.tikal.weather.model.RealTimeData
//import com.tikal.weather.dao.RealTimeDataDao
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConversions.asScalaBuffer
import org.slf4j.{Logger, LoggerFactory}
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
   * Implement this service correctly!
   */
  def temperatureForOneDay(stationId : String, day : String, month : String, year : String): String = {
    // one option is to retrieve all data for that month:
    //val all: List[RealTimeDataMongo] = rtService.findByStationAndMonthYear(stationId, month.toInt, year.toInt)
    // and work from there
    
    // THE RESULT OF THIS SERVICE IS A STRING THAT looks like this:
    "[ ['Time', 'Temperature']," +
    "['00:10', 21.0]," +
    "['00:20', 21.4]," +
    "['00:30', 22.0]," +
    "['00:40', 22.5]," +
    "['00:50', 23.0]," +
    "['01:00', 24.0]" +
    // ...
    "]"
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
      case _:Throwable => None
    }
  }

  def getMonthlyData(stationId: String, month : Int) : String = {
    val data: List[RealTimeDataMongo] = rtService.findByStationAndMonthNoon(stationId, month)
    val dataStr = data
      .filter(_.temperature != "-")
      //Group by year. We suppose to get 30~ noons temperatures
      .groupBy(d => d.month + "/" + d.year)
      //Per year/month get the open, close, min and max for that month
      .map(monthTemps => {
        val open = monthTemps._2.head.temperature.toDouble
        val close = monthTemps._2.reverse.head.temperature.toDouble
        val temperatures: List[Double] = monthTemps._2.map(rt => rt.temperature.toDouble)
        (monthTemps._1, (temperatures.min, open, close, temperatures.max))
      })
      .map{case (k,v) => s"['$k', ${v._1}, ${v._2}, ${v._3}, ${v._4}]"}
      .mkString(",")
    "[ " + dataStr + " ]"
  }

}