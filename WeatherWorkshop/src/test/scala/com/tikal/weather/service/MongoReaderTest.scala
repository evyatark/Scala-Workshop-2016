package com.tikal.weather.service

import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import com.tikal.weather.Application
import org.springframework.beans.factory.annotation.Autowired
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(Array(classOf[Application]))
class MongoReaderTest {
  private val logger: Logger = LoggerFactory.getLogger(classOf[MongoReaderTest])

  @Autowired
//  val rtDataService : RTDataService = null ;
  val mongoReader : MongoReader = null;
  
  @Test
  def readBeitJamalJuneTest(): Unit = {
    mongoReader.readDataFromFile("data/jamal_june_2016_all.csv")
  }

}