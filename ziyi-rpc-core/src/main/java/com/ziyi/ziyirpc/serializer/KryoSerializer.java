package com.ziyi.ziyirpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziyi.ziyirpc.model.RpcRequest;
import com.ziyi.ziyirpc.model.RpcResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Kryo序列化器
 */
public class KryoSerializer implements Serializer {


    /**
     * kyro 线程不安全，使用ThreadLocal 保证线程里只有一个Kyro
     */

    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> {
                Kryo kryo = new Kryo();
//                设置动态序列化和反序列化类,不提前注册所有类(可能有安全问题)
                kryo.setRegistrationRequired(false);
                return kryo;
            });


    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        KRYO_THREAD_LOCAL.get().writeClassAndObject(output, obj);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        T result = KRYO_THREAD_LOCAL.get().readObject(input, type);
        input.close();
        return result;
    }


}
