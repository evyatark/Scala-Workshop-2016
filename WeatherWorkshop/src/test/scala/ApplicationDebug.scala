import java.io.File

import org.springframework.util.FileCopyUtils

/**
 * Created by Haim.Turkel on 7/28/2015.
 */
object ApplicationDebug {
  def main(args: Array[String]) {

    for( a <- 1 to 10000) {
      val fileName = java.util.UUID.randomUUID().toString
      FileCopyUtils.copy(new File("C:\\calaisRunner\\generate\\a.xml"), new File("C:\\calaisRunner\\generate\\" + fileName + ".xml"))
    }



    //    val newArgs : Array[String] = args ++ Array("--home.dir=c:\\dev\\workspace\\trunk\\devmain\\DEV5_COMP\\Components\\CMWellImporter\\nrgConsumer\\dist\\ziproot\\")
//    val configuration: Array[Object] = Array(classOf[Application])
//    SpringApplication.run(configuration, args)
  }
}
