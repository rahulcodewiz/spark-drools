package com.ts.blog.batch;

import org.apache.spark.sql.Dataset;

/**
 * Created by rahul on 8/30/19.
 */
public class UDFExample {

    public static void main(String[] args) {
       Dataset<Long> ds= SessionRegistry.session.range(1,20);

        ds.sparkSession().udf().register("add100",(Long l)->l+100,org.apache.spark.sql.types.DataTypes.LongType);

        ds.show();
        ds.registerTempTable("allnum");

        ds.sparkSession().sql("select add100(id) from allnum").show();
    }
}
