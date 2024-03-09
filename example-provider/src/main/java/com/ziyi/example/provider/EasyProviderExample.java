package com.ziyi.example.provider;

import com.ziyi.example.common.service.TeacherService;
import com.ziyi.example.common.service.UserService;
import com.ziyi.ziyirpc.registry.LocalRegistry;
import com.ziyi.ziyirpc.server.HttpServer;
import com.ziyi.ziyirpc.server.VertxHttpServer;

/**
 * 简易服务提供者示例
 *
 */
public class EasyProviderExample {

    public static void main(String[] args) {
        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        LocalRegistry.register(TeacherService.class.getName(), TeacherServiceImpl.class);

        // 启动 web 服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
