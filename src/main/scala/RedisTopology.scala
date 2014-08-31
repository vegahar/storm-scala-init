import backtype.storm.{LocalCluster, Config}
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.{Fields, Tuple}
import com.redis._
import storm.scala.dsl.StormBolt

class RedisBolt extends StormBolt(List("word", "redis")) {
  var redisClient: RedisClient = _

  setup {
    //this is ip set by vagrant when running redis through boot2docker on windows or osx.
    redisClient = new RedisClient("192.168.59.103",49153)
  }

  override def execute(t: Tuple) = t matchSeq  {

    case Seq(word:String) => {

      redisClient.set(word, 1);
      using anchor t emit (word)
      t ack
    }


  }
}


object RedisTopology {
  def main(args: Array[String]) = {
    val builder = new TopologyBuilder

    builder.setSpout("randsentence", new RandomSentenceSpout)
    builder.setBolt("split", new SplitSentence).shuffleGrouping("randsentence")
    builder.setBolt("redis", new RedisBolt).fieldsGrouping("split", new Fields("word"))

    val conf = new Config
    conf.setDebug(true)
    conf.setMaxTaskParallelism(3)

    val cluster = new LocalCluster
    cluster.submitTopology("redis-bolt", conf, builder.createTopology)
    Thread sleep 10000
    cluster.shutdown
  }
}
