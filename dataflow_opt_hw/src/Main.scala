import java.lang

import com.bingocloud.auth.BasicAWSCredentials
import com.bingocloud.services.s3.AmazonS3Client
import com.bingocloud.{ClientConfiguration, Protocol}

import scala.io.StdIn
import java.util.{Properties, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor

import scala.util.parsing.json.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import org.apache.flink.util.Collector





object Main {
  // s3 参数
  var accessKey = "08C49C332E0DEBEE4151"
  var secretKey = "W0Y0MTUwNjY0Q0I1RjM0MkQ2QzA1NTUyQUMyNzM2QzFFMEVGRjEyQTBd"
  var endpoint = "scuts3.depts.bingosoft.net:29999"
  var bucket = "jacksonkim"
  //要读取的文件
  var file = "daas.txt"
  var key = "destination"
  //kafka参数
  var topic = bucket+file+"2"
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  //上传文件的路径前缀
  val keyPrefix = "upload/"
  //上传数据间隔 单位毫秒
  val period = 15000

  def main(args: Array[String]): Unit = {
    // 配置参数
    val choice = StdIn.readLine("use default S3 config?(or else type in your own config) [y/n]:")
    if(choice.equals("n") || choice.equals("N")){
      accessKey = StdIn.readLine("please type in accessKey:")
      secretKey = StdIn.readLine("please type in secretKey:")
      endpoint = StdIn.readLine("please type in endpoint:")
      bucket = StdIn.readLine("please type in bucketName:")
    }
    file = StdIn.readLine("please choose the input file:")
    key = StdIn.readLine("please choose the classification key:")
    topic = bucket+file+"2"

    //定义一个kafka生产者，接入s3的数据
    val choice2 = StdIn.readLine("produce data from S3 to kafka ? [y/n]:")
    if(choice2.equals("y")||choice2.equals("Y")) {
      println("定义一个kafka生产者，接入s3的数据")
      val credentials = new BasicAWSCredentials(accessKey, secretKey)
      val clientConfiguration = new ClientConfiguration()
      clientConfiguration.setProtocol(Protocol.HTTP)
      val amazonS3 = new AmazonS3Client(credentials, clientConfiguration)
      amazonS3.setEndpoint(endpoint)
      var myKafkaProducer = new MyKafkaProducer(amazonS3, bucket, file, bootstrapServers, topic)
      // 生产数据到kafka
      myKafkaProducer.produceToKafka()
    }

    // 对接kafaka的队列消息
    val choice3 = StdIn.readLine("use flink to process data from kafka and classify them with "+key+"?[y/n]:")
    if(choice3.equals("y")||choice3.equals("Y")){
      println("对接kafka的队列消息")
      val env = StreamExecutionEnvironment.getExecutionEnvironment
      env.setParallelism(1)
      val kafkaProperties = new Properties()
      kafkaProperties.put("bootstrap.servers", bootstrapServers)
      kafkaProperties.put("group.id", UUID.randomUUID().toString)
      kafkaProperties.put("auto.offset.reset", "earliest")
      kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      val kafkaConsumer = new FlinkKafkaConsumer010[String](topic,
        new SimpleStringSchema, kafkaProperties)
      kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
      val inputKafkaStream = env.addSource(kafkaConsumer)
      inputKafkaStream
        .map {
          x =>
            val j = JSON.parseFull(x) match {
              case Some(map: Map[String, Any]) => map
              case other => Map("destination" -> "error")
            }
            (j("destination").toString, x)
        }
        .keyBy(_._1)
        .timeWindow(Time.seconds(15))
        .apply(new WindowFunction[(String, String), String, String, TimeWindow] {
          override def apply(key: String, window: TimeWindow, input: Iterable[(String, String)], out: Collector[String]): Unit = {
            var result = new StringBuilder()
            for (element <- input) {
              result.append(element._2 + "\n")
            }
            result.append("//\n")
            println(result.toString())
            out.collect(result.toString())
          }
        })
        .writeUsingOutputFormat(new S3Writer(accessKey, secretKey, endpoint, bucket, keyPrefix, period))
      env.execute()
    }

  }


}
