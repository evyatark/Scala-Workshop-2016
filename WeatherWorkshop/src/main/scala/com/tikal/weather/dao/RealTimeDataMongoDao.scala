package com.tikal.weather.dao

import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import com.tikal.weather.model.RealTimeDataMongo


trait RealTimeDataMongoDao extends MongoRepository[RealTimeDataMongo, java.lang.String] {
  
  def findByStationId(stationId : String) : java.util.ArrayList[RealTimeDataMongo] ;

  def findByDate(date : String) : java.util.ArrayList[RealTimeDataMongo] ;

}