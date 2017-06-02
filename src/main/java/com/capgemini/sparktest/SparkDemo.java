package com.capgemini.sparktest;

import com.capgemini.sparktest.retrofit.DarkSkyService;
import com.capgemini.sparktest.retrofit.Weather;
import com.datastax.spark.connector.japi.CassandraRow;
import com.datastax.spark.connector.japi.rdd.CassandraJavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;


public class SparkDemo implements Serializable {

    private JavaSparkContext mContext;
    private DarkSkyService mDarkSkyService;

    private SparkDemo(SparkConf conf, Retrofit retrofit) {
        mContext = new JavaSparkContext(conf);
        mDarkSkyService = retrofit.create(DarkSkyService.class);
    }

    public static void main(String[] args) throws IOException {
        /*SparkConf conf = new SparkConf(true)
                .setMaster("local")
                .setAppName("OpenData Wifi Paris")
                .set("spark.cassandra.connection.host", "127.0.0.1");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SparkDemo app = new SparkDemo(conf, retrofit);
        app.run();*/
        darkSky();
    }

    private void run() throws IOException {

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
            // meteo
            Call<Weather> call = mDarkSkyService.timeMachineRequest("48.866667", "2.333333", entry.getKey().getTime());
            Response<Weather> response = call.execute();
            Weather weather = response.body();

            if(response.isSuccessful())
                System.out.println(entry.getKey() + ": " + entry.getValue().spliterator().getExactSizeIfKnown() + ", " + weather.getCurrently().getTemperature());
            else
                System.out.println(entry.getKey() + ": " + entry.getValue().spliterator().getExactSizeIfKnown() + ", error");
        }
    }

    private static void darkSky() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.darksky.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DarkSkyService service = retrofit.create(DarkSkyService.class);

        Call<Weather> call = service.timeMachineRequest("48.866667", "2.333333", 1496236324);
        Response<Weather> response = call.execute();
        Weather weather = response.body();

        if(response.isSuccessful())
            System.out.println(weather.getCurrently().getTemperature());
        else
            System.out.println("bug");
    }
}
