import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._

object demo1 {
  val target="b"
  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //Linux or Mac:nc -l 9999
    //Windows:nc -l -p 9999
    val text = env.socketTextStream("localhost", 9999)
    var b_num = 0
    val stream = text.flatMap {
      _.toLowerCase.split("\\W+") filter {
        _.contains(target)
      }
    }.map {
      ("发现目标："+_)
    }
    stream.print()
    env.execute("Window Stream WordCount")
  }
}