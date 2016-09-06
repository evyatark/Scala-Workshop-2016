package com.tikal.weather.controller

import com.tikal.weather.service.{ MongoDisplayService, MongoReader }
import org.slf4j.{ Logger, LoggerFactory }
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

import scala.io.{ Codec, Source }
import com.tikal.weather.service.HistoricReader

/**
 * Created by Evyatar on 1/7/2016.
 */
@RestController
class MongoController {
  private val logger: Logger = LoggerFactory.getLogger(classOf[MongoController])

  @Autowired
  val mongoDisplayService: MongoDisplayService = null;

  @Autowired
  val mongoReader: MongoReader = null
  
  @Autowired
  val historicReader: HistoricReader = null

  @RequestMapping(value = Array("/mongoDisplay"), method = Array(RequestMethod.GET))
  def mongoDisplay(): String = {
    mongoDisplayService.displayAllRtData()
  }

  @RequestMapping(value = Array("/mongoByDate"), method = Array(RequestMethod.GET))
  def mongoDisplayByDate(@RequestParam("date") date: String): String = {
    mongoDisplayService.displayDayRtData(date)
  }

  @RequestMapping(value = Array("/mongoByDate2/{date}"), method = Array(RequestMethod.GET))
  def mongoDisplayByDate2(@PathVariable("date") date: String): String = {
    mongoDisplayService.displayDayRtData(date)
  }

  @RequestMapping(value = Array("/historic/{date}"), method = Array(RequestMethod.GET))
  def mongoDisplayHistoricDataByDate(@PathVariable("date") date: String): String = {
    mongoDisplayService.displayHistoricDayData(date)
  }

  @RequestMapping(value = Array("/historic/{date}/{station}"), method = Array(RequestMethod.GET))
  def mongoDisplayHistoricDataByDate(@PathVariable("date") date: String, @PathVariable("station") station: String): String = {
    mongoDisplayService.displayHistoricDayData(station, date)
  }

  @RequestMapping(value = Array("/historic/{station}/{month}/{year}"), method = Array(RequestMethod.GET))
  def mongoDisplayHistoricDataByDate(
      @PathVariable("month") month: String, 
      @PathVariable("year") year: String, 
      @PathVariable("station") station: String): String = {
    mongoDisplayService.displayHistoricDayData(station, month, year)
  }
  
  @RequestMapping(value = Array("/mongoUploadFile"), method = Array(RequestMethod.POST))
  def mongoUploadFile(@RequestBody() file: MultipartFile): String = {
    if (!file.isEmpty) {
      val lines = Source.fromInputStream(file.getInputStream)(Codec.string2codec("Windows-1255"))
        .getLines().toList.drop(1)
      mongoReader.loadLinesToMongo(lines)
      return "Successfully loaded file to DB"
    }
    "File could not be read"
  }

  
  @RequestMapping(value = Array("/uploadHistoricTemperatureFile"), method = Array(RequestMethod.POST))
  def uploadHistoricTemperatureFile(@RequestBody() file: MultipartFile): String = {
    if (!file.isEmpty) {
      val lines = Source.fromInputStream(file.getInputStream)(Codec.string2codec("Windows-1255"))
        .getLines().toList.drop(1)
      historicReader.loadLinesToMongo(lines)
      return "Successfully loaded file to DB"
    }
    "File could not be read"
  }

}
