package com.ziyi.ziyirpc.serializer;

import com.ziyi.ziyirpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂(用于获取序列化器对象)
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {
//        {
//            put(SerializerKeys.JDK, new JdkSerializer());
//            put(SerializerKeys.JSON, new JsonSerializer());
//            put(SerializerKeys.KYRO, new KryoSerializer());
//            put(SerializerKeys.HESSIAN, new HessianSerializer());
//        }
//    };
    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_DERIALIZER = new JdkSerializer();


    /**
     * 获取实例
     */

    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }


}
