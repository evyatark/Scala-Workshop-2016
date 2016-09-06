package com.tikal.weather.controller

import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation._
import org.springframework.ui.Model
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.service.RealTimeTemperature


@Controller
@RequestMapping(value = Array("/ui"))
class UIController {
  private val logger: Logger = LoggerFactory.getLogger(classOf[UIController])

  @Autowired
  val rtTemperatureService : RealTimeTemperature = null ;

  @RequestMapping(value = Array("fullMonth"), method = Array(RequestMethod.GET))
  def ui(model : Model):  String = {
    model.addAttribute("month", "July")
    model.addAttribute("year", "2016")
    model.addAttribute("data", rtTemperatureService.minMaxTemperature("7151", "05", "2016"));
    "FullMonthTemperatureGraph"
  }


  @RequestMapping(value = Array("oneDay"), method = Array(RequestMethod.GET))
  def oneDay(model : Model):  String = {
    model.addAttribute("day", "01")
    model.addAttribute("month", "07")
    model.addAttribute("year", "2016")
    val data = rtTemperatureService.temperatureForOneDay("7151", "01", "07", "2016") ;
    model.addAttribute("data", data);
    "OneDayTemperatureGraph"
  }

  @RequestMapping(value = Array("anom"), method = Array(RequestMethod.GET))
  def temperatureAnomalies(model : Model):  String = {
    val data = rtTemperatureService.getMonthlyData("7151", 7)
    model.addAttribute("data", data)
    "TemperatureAnomaliesGraph"
  }
}
