package com.tikal.weather.dao

import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import com.tikal.weather.model.RealTimeDataMongo


trait RealTimeDataMongoDao extends MongoRepository[RealTimeDataMongo, java.lang.String] {

  def findByStationId(stationId : String) : java.util.ArrayList[RealTimeDataMongo] ;

  def findByDate(date : String) : java.util.ArrayList[RealTimeDataMongo] ;

  def findByDateTimeBetween(from : Long, to : Long) : java.util.ArrayList[RealTimeDataMongo] ;
  def findByStationIdAndDateTimeBetween(stationId : String, from : Long, to : Long) : java.util.ArrayList[RealTimeDataMongo] ;
  
  def findByStationIdAndMonthAndYear(stationId : String, month : Int, year : Int) : java.util.ArrayList[RealTimeDataMongo] ;
  def findByStationIdAndDate(stationId : String, date : String) : java.util.ArrayList[RealTimeDataMongo] ;

  def findByStationIdAndMonthAndTime(stationId: String, month: Int, time: String): java.util.List[RealTimeDataMongo]
}