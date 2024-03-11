package com.ziyi.ziyirpc.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ziyi.ziyirpc.model.RpcRequest;
import com.ziyi.ziyirpc.model.RpcResponse;

import java.io.IOException;
import java.util.Arrays;


/**
 * Json序列化器
 */
public class JsonSerializer implements Serializer {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T obj) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest) {
            return handleRequest(((RpcRequest) obj), type);
        }
        if (obj instanceof RpcResponse) {
            return handleResponse(((RpcResponse) obj), type);
        }
        return obj;
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转
     * 换成原始对象，因此这里做了特殊处理
     *
     * @param rpcRequest
     * @param type
     * @param <T>
     * @return
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

//        循环处理每个参数的类型

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            //            如果类型不同,则重新处理一下类型
            if (!clazz.isAssignableFrom(args[i].getClass())) {
                byte[] argsBytes = OBJECT_MAPPER.writeValueAsBytes(args[i]);
                args[i] = OBJECT_MAPPER.readValue(argsBytes, clazz);
            }
        }
        return type.cast(rpcRequest);
    }

    /**
     * 由于Object的原始对象会被擦除，导致反序列化时会被作为LinkedHashMap无法转
     * 换成原始对象，因此这里做了特殊处理
     *
     * @param rpcResponse
     * @param type
     * @param <T>
     * @return
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException {
//        处理响应数据
        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes,rpcResponse.getDataType()));
        return type.cast(rpcResponse);
    }
}
