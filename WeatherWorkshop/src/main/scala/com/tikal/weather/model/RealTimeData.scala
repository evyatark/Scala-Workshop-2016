//package com.tikal.weather.model
//
//import scala.beans.BeanProperty
//import javax.persistence.Entity
//import javax.persistence.Id
//import javax.persistence.GeneratedValue
//import org.hibernate.annotations.GenericGenerator
//
//@Entity
//class RealTimeData {
//  
//  @Id
//  @GeneratedValue
//  @BeanProperty var id : java.lang.Long = _ ;
//  
//  @BeanProperty var stationId : String = _
//  @BeanProperty  var date : String = _
//  @BeanProperty  var time : String = _
//  @BeanProperty  var dateTime : java.lang.Long = _
//  @BeanProperty  var temperature : String = _
//  @BeanProperty  var maxTemperature : String = _
//  @BeanProperty  var minTemperature : String = _
//
//  @BeanProperty  var nearGroundTemperature : String = _
//  @BeanProperty  var humidity : String = _
//  @BeanProperty  var rain : String = _
//
//}