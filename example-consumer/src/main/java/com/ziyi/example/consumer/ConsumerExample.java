package com.ziyi.example.consumer;

import com.ziyi.ziyirpc.config.RpcConfig;
import com.ziyi.ziyirpc.utils.ConfigUtils;

/**
 * 简易服务消费者示例
 *
 */
public class ConsumerExample {

    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
