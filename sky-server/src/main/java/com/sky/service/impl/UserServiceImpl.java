package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 获取openid
        String openid = getOpenid(userLoginDTO.getCode());

        // 判断是否是合法的微信用户
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断openid是否在我们的用户表中，没有则自动注册
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            // 注册
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }

        // 返回查询后的用户
        return user;
    }

    /**
     * 封装一下微信的登陆接口
     */
    private String getOpenid(String code){
        // 调用微信接口服务获得当前用户的openid
        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, params);

        return JSON.parseObject(json).getString("openid");
    }
}
