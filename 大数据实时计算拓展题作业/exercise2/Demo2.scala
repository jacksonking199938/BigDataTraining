import java.util.{Properties, UUID}

import scala.util.parsing.json.{JSON}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.util.Collector




object Demo2 {
  /**
   * 输入的主题名称
   */
  val inputTopic = "mn_buy_ticket_demo2"
  /**
   * kafka地址
   */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val kafkaConsumer = new FlinkKafkaConsumer010[String](inputTopic,
      new SimpleStringSchema, kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)

    inputKafkaStream.filter(x=>x.contains("{") && x.contains("destination"))
        .map{
        x =>
          val j = JSON.parseFull(x) match{
            case Some(map:Map[String,Any]) => map
          }
         WordWithCount(j("destination").toString,1)
      }
      .keyBy(0)
      .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(30)))
      .process(new ProcessAllWindowFunction[WordWithCount,WordWithCount,TimeWindow] {
        override def process(context: Context, elements: Iterable[WordWithCount], out: Collector[WordWithCount]): Unit = {
            val sortedList = elements.toList.sortBy(_.count)
            sortedList.take(5).foreach(println)
        }
      })
    env.execute()

  }
  case class WordWithCount(word: String, count: Long)
}