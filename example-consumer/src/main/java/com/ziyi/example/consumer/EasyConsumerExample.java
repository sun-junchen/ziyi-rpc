package com.ziyi.example.consumer;

import com.ziyi.example.common.model.Teacher;
import com.ziyi.example.common.model.User;
import com.ziyi.example.common.service.TeacherService;
import com.ziyi.example.common.service.UserService;
import com.ziyi.ziyirpc.proxy.ServiceProxyFactory;

import java.io.IOException;

/**
 * 简易服务消费者示例
 *
 */
public class EasyConsumerExample {

    public static void main(String[] args) throws IOException {
        // 静态代理
//        UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        TeacherService teacherService = ServiceProxyFactory.getProxy(TeacherService.class);
        User user = new User();
        user.setName("ziyi");
        Teacher teacher = new Teacher();
        teacher.setName("ziyi teacher");
        // 调用
        User newUser = userService.getUser(user);
        Teacher newTeacher = teacherService.getTeacherName(teacher);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }

        long number = userService.getNumber();

        System.out.println(number);

        if (newTeacher != null) {
            System.out.println(newTeacher.getName());
        } else {
            System.out.println("newTeacher == null");
        }
    }
}
