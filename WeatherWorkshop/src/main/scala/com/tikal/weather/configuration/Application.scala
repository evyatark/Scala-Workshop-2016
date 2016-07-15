package com.tikal.weather.configuration

import javax.annotation.{PostConstruct, PreDestroy}
import org.slf4j.LoggerFactory
import org.springframework.boot._
import org.springframework.boot.actuate.system.ApplicationPidFileWriter
import org.springframework.boot.autoconfigure._
import org.springframework.boot.context.web.SpringBootServletInitializer
import org.springframework.context.annotation._

/**
  * Created by Haim.Turkel on 7/15/2015.
  */
@Configuration
@ComponentScan(basePackages = Array("com.tikal.weather.configuration", "com.tikal.weather"))
@SpringBootApplication
class Application extends SpringBootServletInitializer {

  val log = LoggerFactory.getLogger(this.getClass.getSimpleName)

  @PostConstruct
  def init(): Unit = {
    log.info("server started")
  }

  @PreDestroy
  def shutdown(): Unit = {
    log.info("server shutdown")
  }

}

object Application {
  def main(args: Array[String]) {
    val configuration: Array[Object] = Array(classOf[Application])
    val springApplication = new SpringApplication(configuration:_*)
    springApplication.addListeners(new ApplicationPidFileWriter())
    springApplication.run(args:_*)
  }
}

