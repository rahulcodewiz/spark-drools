package com.ts.blog.batch;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

public class SessionRegistry {
    public static SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("Test");
    public static SparkSession session = SparkSession.builder().config(conf).getOrCreate();

   public static JavaSparkContext jsc = new JavaSparkContext(session.sparkContext());
}
