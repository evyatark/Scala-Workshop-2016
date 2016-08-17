package com.tikal.weather.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import scala.collection.JavaConversions.asScalaBuffer
import org.springframework.data.repository.Repository
import org.springframework.stereotype.Component

@Component
class MongoTemplateDao {
  @Autowired
  val mongoTemplate : MongoTemplate = null ;
  
  def findDistinctStationIds() : List[String] = {
    val list = mongoTemplate.getCollection("realTimeDataMongo").distinct("stationId");
    val x : List[Any] = asScalaBuffer(list).toList
    x.map { _.toString() }
  }
}