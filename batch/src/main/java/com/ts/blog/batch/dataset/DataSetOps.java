package com.ts.blog.batch.dataset;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;

import javax.xml.crypto.Data;

/**
 * Created by rahul on 8/25/19.
 */
public class DataSetOps {


    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("Test");
        SparkSession session = SparkSession.builder().config(conf).getOrCreate();

        JavaSparkContext jsc = new JavaSparkContext(session.sparkContext());

        String path= "/Users/rahul/git/spark-drools/batch/src/main/conf/employee";

        //rdd(jsc, path);

        Dataset<Employee> dataset = fromJson(session,path);
        dataset.show();

        Row max = dataset.agg(org.apache.spark.sql.functions.max(dataset.col("id"))).as("max").head();
        System.out.println(max);

    }

    private static JavaRDD<Employee> rdd(JavaSparkContext jsc, String path) {
        return jsc.textFile(path+".csv")
       .map(row->row.split(","))
               .map(s->new Employee(s));
    }

    private static Dataset<Employee> fromJson(SparkSession session, String path) {
        Encoder<Employee> employeeEncoder = Encoders.bean(Employee.class);
        return session.read().json(path+".json").as(employeeEncoder);
    }
}
