package com.ziyi.example.provider;

import com.ziyi.example.common.model.Teacher;
import com.ziyi.example.common.service.TeacherService;

/**
 * 用户服务实现类
 *
 */
public class TeacherServiceImpl implements TeacherService {


    @Override
    public Teacher getTeacherName(Teacher teacher) {
        System.out.println("用户名：" + teacher.getName());
        return teacher;
    }
}
