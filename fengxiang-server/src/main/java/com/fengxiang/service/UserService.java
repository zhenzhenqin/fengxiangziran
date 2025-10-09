package com.fengxiang.service;

import com.fengxiang.dto.UserLoginDTO;
import com.fengxiang.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
