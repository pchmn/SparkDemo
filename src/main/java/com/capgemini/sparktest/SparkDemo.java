package com.capgemini.sparktest;

import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

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
        CassandraJavaRDD<OpenDataWifi> metricRdd = javaFunctions(mContext)
                .cassandraTable("donnees_urbaines", "opendata_wifi", mapRowTo(OpenDataWifi.class))
                .select("start_time", "browser", "device", "os", "site");

        metricRdd
                .filter(metric -> metric.getId().equals("5ee9c4929b68edfb9d1697138ca77bb08ec7e70b"))
                .foreach(openDataWifi -> System.out.println("result: " + openDataWifi.getDevice() + ", " + openDataWifi.getBrowser()));
    }
}
