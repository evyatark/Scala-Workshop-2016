package com.tikal.weather.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.dao.RealTimeDataMongoDao
import scala.collection.JavaConversions.asScalaBuffer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class MongoDisplayService {
  
  private val logger: Logger = LoggerFactory.getLogger(classOf[MongoDisplayService])

    
  @Autowired
  val d : RealTimeDataMongoDao = null ;
  
  def displayAllRtData() : String = {
    logger.info("displayAllRtData")
//    "qqq"
    val s = asScalaBuffer(d.findByStationId("7151")).toList.mkString("\n")
    logger.info(s)
    s
  }
  
  def displayDayRtData(date : String) : String = {
    logger.info(s"displayRtData for ${date}")
    val s = time { asScalaBuffer(d.findByDate(date)).toList.mkString("\n") }
    //logger.info(s)
    s
  }

  
  def time[R](block: => R): R = {  
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    logger.info("Elapsed time: " + (t1 - t0)/1000000 + "ms")
    result
  }
}