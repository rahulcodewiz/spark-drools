package com.ts.blog.batch;

import org.apache.spark.util.AccumulatorV2;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rahul on 8/30/19.
 */
public class AccumulatorExample {

    public static void main(String[] args) {

        AccumulatorV2 accumulatorV2 = new CustomAccumulator();
        SessionRegistry.jsc.sc().register(accumulatorV2);
    }

}

/**
 * The sample accumulator to store set of string values
 */
class CustomAccumulator extends AccumulatorV2<String,Set<String>>{
    Set<String> myval= new HashSet<>();
    @Override
    public void merge(AccumulatorV2<String, Set<String>> other) {
        other.value().stream().forEach(val->myval.add(val));
    }
    @Override
    public boolean isZero() {
        return myval.size()==0;
    }

    @Override
    public AccumulatorV2<String, Set<String>> copy() {
        return this;
    }

    @Override
    public void reset() {
        myval.clear();
    }

    @Override
    public void add(String v) {
        myval.add(v);
    }



    @Override
    public Set<String> value() {
        return myval;
    }
}
