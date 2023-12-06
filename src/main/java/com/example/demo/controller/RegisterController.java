package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.EncryptAndDecryptStr;
import com.example.demo.entity.Users;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RegisterController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public Map register(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String username = (String) jsonParam.get("username");
        String password = (String) jsonParam.get("password");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (password.length() < 6 || password.length() > 16) {
            metaMap.put("msg", "注册失败，请检查密码长度");
            metaMap.put("status", 500);
            map.put("meta", metaMap);
            return map;
        }
        String email = (String) jsonParam.get("email");
        String phone = (String) jsonParam.get("phone");
        if (email == null) {
            email = "1111111111@mm.com";
        }
        if(phone!=null && phone.length()!=11) {
            metaMap.put("msg", "注册失败，请检查电话长度");
            metaMap.put("status", 500);
            map.put("meta", metaMap);
            return map;
        }
        List<Users> check = userMapper.findName(username);
        if(!check.isEmpty()) {
            metaMap.put("msg", "注册失败，用户名重复");
            metaMap.put("status", 500);
            map.put("meta", metaMap);
            return map;
        }
        if (phone == null) {
            phone = "13700000000";
        }
        boolean is_admin = false;
        password = EncryptAndDecryptStr.encryptStr(username, password);
        Users user = new Users(username, password, email, phone, is_admin);
        user.setUser_status(0);
        user.setRegister_time(new java.util.Date());
        user.setToken("0"); // fixme
        user.setUser_img_url("");
        int i = userMapper.insert(user);
        if (i > 0) {
            metaMap.put("msg", "注册成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "注册失败");
            metaMap.put("status", 500);
        }
        System.out.println(map.toString());
        map.put("meta", metaMap);
        return map;
    }

    public void saveFile(MultipartFile file, String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File fileS = new File(path + file.getOriginalFilename());
        try {
            file.transferTo(fileS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
