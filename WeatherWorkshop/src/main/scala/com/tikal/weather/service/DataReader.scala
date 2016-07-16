package com.tikal.weather.service

import scala.io.Codec
import scala.io.Source
import com.tikal.weather.model.RealTimeData
import org.springframework.beans.factory.annotation.Autowired
import com.tikal.weather.dao.RealTimeDataDao
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DataReader {

  @Autowired
  val dao : RealTimeDataDao = null;

  @PostConstruct
  def init() = {
    readDataFromFile("C:\\work\\scala\\data\\weather\\BeitJamal\\jamal_june_2016_temp.csv")
  }
  
  def readDataFromFile(fileName: String) = {
    val codec: Codec = Codec.string2codec("Windows-1255"); // required because of Hebrew in the data file
    val linesAllFiles = Source.fromFile(fileName)(codec).getLines().toList.drop(1)
      
    val lines : List[WeatherData] =
      for (line <- linesAllFiles) yield {
        val p = line.split(",") ;
        val data = new WeatherData(p(1), p(2), p(4), p(5)) ;
        data
      }
    val rtAllData : List[RealTimeData] = for (line <- lines) yield {
      val rtData : RealTimeData = new RealTimeData() ;
      rtData.stationId = "7151" ;
      rtData.date = line.date ;
      rtData.time = line.time ;
      rtData.maxTemperature = line.maxTemp ;
      rtData.minTemperature = line.minTemp ;
      rtData
    }
    
    rtAllData.foreach { rt : RealTimeData => dao.save(rt) }
  }
  
  
  case class WeatherData(
    val date : String, 
    val time : String,
    val maxTemp : String, 
    val minTemp : String
    );

}