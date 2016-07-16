package com.tikal.weather.controller

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.dao.RealTimeDataDao
import com.tikal.weather.model.RealTimeData
import javax.annotation.PostConstruct
import org.springframework.stereotype.Controller
import java.lang.Iterable
import scala.collection.JavaConversions.asScalaBuffer

/**
  * Created by Evyatar on 1/7/2016.
  */
@RestController
class WeatherController { //@Autowired()(private val dao: RealTimeDataDao) {
  private val logger: Logger = LoggerFactory.getLogger(classOf[WeatherController])

  
  @Autowired
  val dao : RealTimeDataDao = null;


  @CrossOrigin
  @RequestMapping(value = Array("/data"), method = Array(RequestMethod.GET))
  def data(
      @RequestParam(value="month", defaultValue="june") month : String,
      @RequestParam(value="year", defaultValue="2016") year : String
      ):  String = {
      logger.warn(s"data, month=$month, year=$year")
      """
      [ ['Date', 'Min Temperature', 'Max Temperature'],
[1, 20.7, 34.8],[2, 20.2, 33.4],[3, 21.7, 39.1],[4, 26.9, 41.7],[5, 20.8, 35.6],[6, 19.3, 32.2],[7, 21.1, 35.4],[8, 20.5, 37.6],[9, 18.9, 28.6],[10, 13.2, 28.5],[11, 17.3, 29.2],[12, 17.2, 31.5],[13, 21.4, 37.4],[14, 20.2, 37.4],[15, 20.6, 35.9],[16, 19.3, 30.3],[17, 19.8, 31.9],[18, 20.0, 34.0],[19, 19.5, 35.7],[20, 22.4, 38.0],[21, 23.9, 37.2],[22, 22.6, 35.9],[23, 21.8, 35.9],[24, 22.1, 36.0],[25, 22.2, 36.8],[26, 20.5, 35.0],[27, 22.3, 33.3],[28, 21.0, 31.2],[29, 22.6, 33.0],[30, 21.6, 33.2]
      ]  """
  }
  
  @PostConstruct
  def init() = {
//    logger.info("postConstruct");
//    val x : RealTimeData = new RealTimeData();
//    //x.id = 111 // no use to set id, it is a generated value!!!
//    x.stationId = "111" ;
//    x.date = "01-01-1930"
//    x.maxTemperature = "27.1" ;
//    x.minTemperature = "12.2";
//    dao.save(x)
  }
  
  @RequestMapping(value = Array("/test"), method = Array(RequestMethod.GET))
  def test():  String = {
      logger.warn(s"test")
      val y  = dao.findAll() ;
      val all : List[RealTimeData] = asScalaBuffer(y.asInstanceOf[java.util.ArrayList[RealTimeData]]).toList
      logger.info("size="+all.size);
//      val z = y.iterator().next()
//      logger.info("z="+z)
//      logger.info("z.id="+z.id)
      val x : RealTimeData = dao.findOne(all(0).id)
      s"${x.stationId} ${x.date} ${x.time} ${x.maxTemperature}"
      //"1"
  }
}
