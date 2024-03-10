package com.ziyi.example.common.service;

import com.ziyi.example.common.model.User;

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
    User getUser(User user);


    /**
     * 获取数字
     */
    default short getNumber() {
        return 1;
    }
}
