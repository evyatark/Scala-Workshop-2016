package com.tikal.weather.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.dao.RealTimeDataMongoDao
import com.tikal.weather.model.RealTimeDataMongo
import scala.collection.JavaConversions.asScalaBuffer
import org.springframework.stereotype.Service
import com.tikal.weather.dao.MongoTemplateDao

@Service
class RTDataService {
  
  private val logger: Logger = LoggerFactory.getLogger(classOf[RTDataService])

  @Autowired
  val dao : RealTimeDataMongoDao = null;
  
  @Autowired
  val templateDao : MongoTemplateDao = null ;

  def findDataByMonth(stationId : String, month : String, year : String): List[RealTimeDataMongo] = {
    val all : List[RealTimeDataMongo] = asScalaBuffer(dao.findByStationId(stationId)).toList ;
    val ofThisMonth = all.filter { x : RealTimeDataMongo => x.date.endsWith(s"$month-$year") } ;
    ofThisMonth
  }
  
  def findAllStationIds(): List[String] = {
    templateDao.findDistinctStationIds();
  }
  
  def findByDateRange(from : String, to : String) : List[RealTimeDataMongo] = {
    val fromLong = dateAsLong(from)
    val toLong = dateAsLong(to)
    val x = dao.findByDateTimeBetween(fromLong-1, toLong)
    asScalaBuffer(x).toList
  }
  
  def findByStationAndMonthYear(stationId : String, month : Int, year : Int) : List[RealTimeDataMongo] = {
    
    val x = time{dao.findByStationIdAndMonthAndYear(stationId, month, year)}
    
    asScalaBuffer(x).toList
  }
  
   def time[R](block: => R): R = {  
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()
    logger.info("Elapsed time: " + (t1 - t0)/1000000 + "ms")
    result
  }
  
  def findByStationAndDateRange(stationId : String, from : String, to : String) : List[RealTimeDataMongo] = {
    val fromLong = dateAsLong(from)
    val toLong = dateAsLong(to)
    val x = dao.findByStationIdAndDateTimeBetween(stationId, fromLong-1, toLong)
    asScalaBuffer(x).toList
  }
  
  def dateAsLong(date: String): Long = {
    // date is 01-06-2016

    val format = new java.text.SimpleDateFormat("MM-dd-yyyy")
    format.parse(date).getTime
  }
}