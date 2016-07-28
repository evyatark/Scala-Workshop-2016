package com.tikal.weather.service

import org.springframework.stereotype.Service
import com.tikal.weather.model.RealTimeData
import com.tikal.weather.dao.RealTimeDataDao
import org.springframework.beans.factory.annotation.Autowired
import scala.collection.JavaConversions.asScalaBuffer
import org.slf4j.{Logger, LoggerFactory}

@Service
class RealTimeTemperature {

  private val logger: Logger = LoggerFactory.getLogger(classOf[RealTimeTemperature])

  @Autowired
  val dao : RealTimeDataDao = null;

  def minMaxTemperature(stationId : String): String = {

    val all: List[RealTimeData] = asScalaBuffer(dao.findByStationId(stationId)).toList
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
}