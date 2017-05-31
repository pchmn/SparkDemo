package com.capgemini.sparktest;

import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;


public class SparkDemo implements Serializable {

    private JavaSparkContext mContext;

    private SparkDemo(SparkConf conf) {
        mContext = new JavaSparkContext(conf);
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf(true)
                .setMaster("local")
                .setAppName("OpenData Wifi Paris")
                .set("spark.cassandra.connection.host", "127.0.0.1");

        SparkDemo app = new SparkDemo(conf);
        app.run();
    }

    private void run() {

        CassandraJavaRDD<CassandraRow> cassandraRowsRDD = javaFunctions(mContext)
                .cassandraTable("donnees_urbaines", "opendata_wifi");

        JavaPairRDD<Date, Iterable<OpenDataWifi>> openDataWifiJavaRDD = cassandraRowsRDD.select("id", "language", "duration", "start_time", "os", "browser", "device", "site", "input_octets", "output_octets")
                .map(CassandraRow::toMap)
                .map(entry -> new OpenDataWifi(
                        entry.get("id") != null ? (String) entry.get("id"): null,
                        entry.get("start_time") != null ? (String) entry.get("start_time"): null,
                        entry.get("browser") != null ? (String) entry.get("browser"): null,
                        entry.get("device") != null ? (String) entry.get("device"): null,
                        entry.get("language") != null ? (String) entry.get("language"): null,
                        entry.get("site") != null ? (String) entry.get("site"): null,
                        entry.get("os") != null ? (String) entry.get("os"): null,
                        entry.get("duration") != null ? (double) entry.get("duration"): 0,
                        entry.get("input_octets") != null ? (double) entry.get("input_octets"): 0,
                        entry.get("output_octets") != null ? (double) entry.get("output_octets"): 0
                ))
                .groupBy(OpenDataWifi::getStart_time)
                .cache();


        /*JavaRDD<Tuple2> duration = cassandraRowsRDD.select("duration", "start_time", "os")
                .map(CassandraRow::toMap)
                .filter(entry -> ((String) entry.get("start_time")).contains("2016-12-31"))
                .groupBy(entry -> entry.get("start_time"))
                .map(entry -> new Tuple2((double) entry.get("duration"), (String) entry.get("os")))
                .cache();

        System.out.println("duration: " + duration.first());
        System.out.println("count: " + duration.count());*/

        System.out.println(openDataWifiJavaRDD.first().toString());
        for(Map.Entry<Date, Iterable<OpenDataWifi>> entry: openDataWifiJavaRDD.collectAsMap().entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().spliterator().getExactSizeIfKnown());
            for(OpenDataWifi entry2: entry.getValue()) {
                long timestamp = entry2.getStart_time().getTime() / 1000;
            }
        }
    }
}
