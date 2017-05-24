package com.capgemini.sparktest;

import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
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

        CassandraJavaRDD<CassandraRow> cassandraRowsRDD = javaFunctions(mContext)
                .cassandraTable("donnees_urbaines", "opendata_wifi");

        JavaRDD<Double> duration = cassandraRowsRDD.select("duration", "start_time")
                .map(CassandraRow::toMap)
                .filter(entry -> ((String) entry.get("start_time")).contains("2016-05-21"))
                .map(entry -> (double) entry.get("duration"))
                .cache();

        System.out.println("count: " + duration.count());

    }
}
