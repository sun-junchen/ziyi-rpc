package com.ziyi.example.common.service;

import com.ziyi.example.common.model.Teacher;

/**
 * 教师服务
 *
 */
public interface TeacherService {

    /**
     * 获取教师名字
     *
     * @param teacher
     * @return
     */
    Teacher getTeacherName(Teacher teacher);
}
