package com.fengxiang.controller.user;


import com.fengxiang.constant.JwtClaimsConstant;
import com.fengxiang.dto.UserLoginDTO;
import com.fengxiang.entity.User;
import com.fengxiang.properties.JwtProperties;
import com.fengxiang.result.Result;
import com.fengxiang.service.UserService;
import com.fengxiang.utils.JwtUtil;
import com.fengxiang.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "C端-用户登录接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> wxLogin(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("微信登录用户参数:{}", userLoginDTO);

        /*//微信登录
        User user = userService.wxLogin(userLoginDTO);
        String openid = user.getOpenid();

        //生成登录的token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put("openid", openid);
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        //封装VO返回前端
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();

        return Result.success(userLoginVO);*/


        try {
            //微信登录
            User user = userService.wxLogin(userLoginDTO);

            String openid = user.getOpenid();

            //生成登录的token
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getId());
            claims.put("openid", openid);
            String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

            //封装VO返回前端
            UserLoginVO userLoginVO = UserLoginVO.builder()
                    .id(user.getId())
                    .openid(openid)
                    .token(token)
                    .build();

            return Result.success(userLoginVO);
        } catch (Exception e) {
            log.error("微信登录异常: ", e);
            return Result.error("登录失败: " + e.getMessage());
        }
    }
}
