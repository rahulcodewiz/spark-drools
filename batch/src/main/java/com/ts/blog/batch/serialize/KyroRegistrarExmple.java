package com.ts.blog.batch.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.ts.blog.batch.SessionRegistry;
import org.apache.spark.broadcast.Broadcast;

public class KyroRegistrarExmple {

    public static void main(String[] args) {
        SessionRegistry.jsc.getConf().set("spark.kryo.registrar",CustomKKryoRegistrator.class.getName());
      Broadcast<ABean> aBean=  SessionRegistry.jsc.broadcast(new ABean());
        System.out.println(aBean);

    }
}

class CustomKKryoRegistrator implements org.apache.spark.serializer.KryoRegistrator{

    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(ABean.class);
    }
}