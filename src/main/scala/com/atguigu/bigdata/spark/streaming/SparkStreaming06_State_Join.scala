package com.atguigu.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming06_State_Join {

    def main(args: Array[String]): Unit = {

        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
        val ssc = new StreamingContext(sparkConf, Seconds(5))

        // todo 两个数据流join
        val data9999 = ssc.socketTextStream("localhost", 9999)
        val data8888 = ssc.socketTextStream("localhost", 8888)

        val map9999: DStream[(String, Int)] = data9999.map((_,9))
        val map8888: DStream[(String, Int)] = data8888.map((_,8))

        // 所谓的DStream的Join操作，其实就是两个RDD的join
        val joinDS: DStream[(String, (Int, Int))] = map9999.join(map8888)

        joinDS.print()

        ssc.start()
        ssc.awaitTermination()
    }

}

// cmd: nc -lp 9999
// cmd: nc -lp 8888
// 启动程序  然后再两个cmd中输入一样的string