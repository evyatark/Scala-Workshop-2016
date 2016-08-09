package com.tikal.weather.model

import org.springframework.data.annotation.Id
import scala.beans.BeanProperty

class HistoricalDataMongo {
  @Id
  @BeanProperty  var id : java.lang.String = _ ;
  
  @BeanProperty  var stationId : String = _
  @BeanProperty  var date : String = _
  @BeanProperty  var dateTime : java.lang.Long = _
  @BeanProperty  var maxTemperature : String = _
  @BeanProperty  var minTemperature : String = _
  @BeanProperty  var rain : String = _

  override def toString() = {
    s"$stationId $date $maxTemperature $minTemperature $rain"
  }

}