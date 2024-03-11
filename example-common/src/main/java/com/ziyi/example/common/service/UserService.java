package com.ziyi.example.common.service;

import com.ziyi.example.common.model.User;

import java.io.IOException;

/**
 * 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user) throws IOException;


    /**
     * 获取数字
     */
    default short getNumber() {
        return 1;
    }
}
