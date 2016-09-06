package com.tikal.weather.dao

import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import com.tikal.weather.model.RealTimeDataMongo
import com.tikal.weather.model.HistoricalDataMongo


trait HistoricalDataDao extends MongoRepository[HistoricalDataMongo, java.lang.String] {
  
  def findByStationId(stationId : String) : java.util.ArrayList[HistoricalDataMongo] ;

  def findByDate(date : String) : java.util.ArrayList[HistoricalDataMongo] ;
  def findByDateAndStationId(date : String, stationId : String) : java.util.ArrayList[HistoricalDataMongo] ;

  def findByDateTimeBetween(from : Long, to : Long) : java.util.ArrayList[HistoricalDataMongo] ;
  def findByStationIdAndDateTimeBetween(stationId : String, from : Long, to : Long) : java.util.ArrayList[HistoricalDataMongo] ;
  
  def findByStationIdAndMonthAndYear(stationId : String, month : Int, year : Int) : java.util.ArrayList[HistoricalDataMongo] ;
}