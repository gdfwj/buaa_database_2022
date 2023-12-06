package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.util.EncryptAndDecryptStr;
import com.example.demo.entity.Users;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public Map login(@RequestBody JSONObject jsonParam) {
        System.out.println(jsonParam);
        String username = (String) jsonParam.get("username");
        String password = (String) jsonParam.get("password");
        System.out.println(username + password);
        password = EncryptAndDecryptStr.encryptStr(username, password);
        List<Users> users = userMapper.login(username, password);
        Users user;
        if(users.isEmpty()) {
            user = null;
        } else {
            user = users.get(0);
        }
        Map<String, Object> map= new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if(user !=null) {
            if(user.getUser_status()==2) {
                metaMap.put("msg", "账号被冻结");
                metaMap.put("status", 410);
                map.put("meta", metaMap);
            }
            System.out.println(user);
            String token = JwtUtil.sign(username, String.valueOf(user.getUid()), user.isIs_admin());
            user.setToken(token);
            userMapper.update(user);
            if(token!=null) {
                dataMap.put("is_admin", user.isIs_admin());
                dataMap.put("token", token);
                map.put("data", dataMap);
                metaMap.put("msg", "登陆成功");
                metaMap.put("status", 200);
                map.put("meta", metaMap);
                return map;
            }
        }
        metaMap.put("msg", "密码错误");
        metaMap.put("status", 410);
        map.put("meta", metaMap);
        return map;
    }
}
