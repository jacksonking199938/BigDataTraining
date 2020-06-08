

import java.sql.{Connection, DriverManager}
import java.util.Properties

import com.bingocloud.{ClientConfiguration, Protocol}
import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.services.s3.AmazonS3Client
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.nlpcn.commons.lang.util.IOUtil

object MysqlToKafka {
  // mysql参数
  val username = "user29"
  val password = "pass@bingo29"
  val driver = "com.mysql.jdbc.Driver"
  val url = "jdbc:mysql://bigdata28.depts.bingosoft.net:23307/user29_db"
  var connection: Connection = null

  //kafka参数
  val topic = "jacksonkim"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val sqlContent = readSQL()
    produceToKafka(sqlContent)
  }

  /**
   * 从mysql中读取文件内容
   *
   * @return  mysql数据库的内容
   */
  def readSQL():String = {
    //在spark中如果不写会出错
//    classOf[com.mysql.jdbc.Driver]
    Class.forName("com.mysql.jdbc.Driver")
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery("select * from ajrw")
    val metadata = resultSet.getMetaData();
    var columnnum = metadata.getColumnCount();
    val builder = new StringBuilder()
    while(resultSet.next()){
      var temp = ""
      var index = 1
      while(index <= columnnum) {
        temp += resultSet.getString(index) + "\t"
        index += 1
      }
      builder.append(temp+"\n")
    }
    return builder.toString()
  }


  /**
   * 把数据写入到kafka中
   *
   * @param content 要写入的内容
   */
  def produceToKafka(content: String): Unit = {
    val props = new Properties
    props.put("bootstrap.servers", bootstrapServers)
    props.put("acks", "all")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    val dataArr = content.split("\n")
    for (s <- dataArr) {
      if (!s.trim.isEmpty) {
        val record = new ProducerRecord[String, String](topic, null, s)
        println("开始生产数据：" + s)
        producer.send(record)
      }
    }
    producer.flush()
    producer.close()
  }


}