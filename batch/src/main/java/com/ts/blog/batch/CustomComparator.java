package com.ts.blog.batch;

import org.apache.spark.api.java.JavaRDD;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by rahul on 8/30/19.
 */
public class CustomComparator {

    public static void main(String[] args) {
        JavaRDD<Integer> javaRDD  = SessionRegistry.jsc.parallelize(Arrays.asList(new Integer[]{100,20,10,1020,100}));

       Integer maxVal=  javaRDD.max(new LengthComparator());
    }
}

class LengthComparator implements Comparator<Integer>{

    @Override
    public int compare(Integer o1, Integer o2) {
        return 0;
    }
}
