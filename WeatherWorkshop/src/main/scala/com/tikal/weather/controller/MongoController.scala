package com.tikal.weather.controller

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.model.RealTimeData
import javax.annotation.PostConstruct
import org.springframework.stereotype.Controller
import scala.collection.JavaConversions.asScalaBuffer
import com.tikal.weather.service.MongoDisplayService

/**
  * Created by Evyatar on 1/7/2016.
  */
@RestController
class MongoController {
  private val logger: Logger = LoggerFactory.getLogger(classOf[MongoController])

  
  
  @Autowired
  val mongoDisplayService : MongoDisplayService = null ;
  
  
  @RequestMapping(value = Array("/mongoDisplay"), method = Array(RequestMethod.GET))
  def mongoDisplay():  String = {
    mongoDisplayService.displayAllRtData()
  }
  
  @RequestMapping(value = Array("/mongoByDate"), method = Array(RequestMethod.GET))
  def mongoDisplayByDate(@RequestParam("date") date : String):  String = {
    mongoDisplayService.displayDayRtData(date)
  }
  
  @RequestMapping(value = Array("/mongoByDate2/{date}"), method = Array(RequestMethod.GET))
  def mongoDisplayByDate2(@PathVariable("date") date : String):  String = {
    mongoDisplayService.displayDayRtData(date)
  }

}
