//package com.tikal.weather.dao
//
//import org.springframework.data.repository.CrudRepository
//import com.tikal.weather.model.RealTimeData
//import org.springframework.stereotype.Component
//import org.springframework.stereotype.Repository
//
//
//trait RealTimeDataDao extends CrudRepository[RealTimeData, java.lang.Long] {
//  
//  def findByStationId(stationId : String) : java.util.ArrayList[RealTimeData] ;
//}