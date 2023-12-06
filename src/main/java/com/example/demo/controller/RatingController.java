package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Rating;
import com.example.demo.entity.Useraction;
import com.example.demo.mapper.RatingMapper;
import com.example.demo.mapper.UserActionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class RatingController {
    @Autowired
    RatingMapper ratingMapper;
    @Autowired
    UserActionMapper userActionMapper;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/user/rating/add")
    public Map add(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        int uid2 = (int) jsonParam.get("uid");
        double ratingPoint = Double.parseDouble((jsonParam.get("rating").toString()));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int uid1 = Integer.parseInt(JwtUtil.getUserId(token));
        if (uid1 == uid2) {
            metaMap.put("msg", "重复的uid");
            metaMap.put("status", 405);
        }
        Rating rating = new Rating(0, uid1, uid2, ratingPoint);
        List<Rating> anotherRatings = ratingMapper.searchByIds(uid1, uid2);
        if (!anotherRatings.isEmpty()) {
            metaMap.put("msg", "上传失败");
            metaMap.put("status", 405);
            userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 1));
        } else if (uid1 == uid2) {
            metaMap.put("msg", "不可以评价自己");
            metaMap.put("status", 405);
            userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 1));
        } else {
            int i = ratingMapper.insert(rating);
            if (i > 0) {
                metaMap.put("msg", "上传成功");
                metaMap.put("status", 200);
                userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 0));
            } else {
                metaMap.put("msg", "上传失败");
                metaMap.put("status", 500);
                userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 1));
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/rating/edit")
    public Map edit(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        int uid2 = (int) jsonParam.get("uid");
        double ratingPoint = Double.parseDouble(jsonParam.get("rating").toString());
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int uid1 = Integer.parseInt(JwtUtil.getUserId(token));
        if (uid1 == uid2) {
            metaMap.put("msg", "重复的uid");
            metaMap.put("status", 405);
        }
        Rating rating = new Rating(0, uid1, uid2, ratingPoint);
        int i;
        if (ratingMapper.searchByIds(uid1, uid2).isEmpty()) {
            i = ratingMapper.insert(rating);
        } else {
            i = ratingMapper.updateRatingByIds(rating);
        }
        if (i > 0) {
            metaMap.put("msg", "上传成功");
            metaMap.put("status", 200);
            userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 0));
        } else {
            metaMap.put("msg", "上传失败");
            metaMap.put("status", 500);
            userActionMapper.insert(new Useraction(0, uid1, uid2, 0, new Date(), 1));
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/rating/search")
    public Map search(int uid, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        if (!hasUser(uid)) {
            metaMap.put("status", 410);
            dataMap.put("uid", uid);
            metaMap.put("msg", "找不到用户");
        } else {
            double ratingPoint = getAverageRating(uid);
            metaMap.put("status", 200);
            metaMap.put("msg", "搜索成功");
            dataMap.put("uid", uid);
            dataMap.put("rating", ratingPoint);
        }
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }

    @PostMapping("user/rating/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int uid1 = (int) jsonParam.get("uid1");
        int uid2 = (int) jsonParam.get("uid2");
        boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
        if (!is_admin) {
            metaMap.put("status", 403);
            metaMap.put("msg", "权限不足，无法删除评分");
        } else {
            int i = ratingMapper.deleteByIds(uid1, uid2);
            if (i > 0) {
                metaMap.put("status", 200);
                metaMap.put("msg", "成功删除");
            } else {
                metaMap.put("status", 500);
                metaMap.put("msg", "删除失败");
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    public double getAverageRating(int uid) {
        List<Rating> ratings = ratingMapper.searchById(uid);
        double totalPoints = 0;
        for (Rating rating : ratings) {
            totalPoints += rating.getRating();
        }
        if (ratings.isEmpty()) {
            return 0;
        } else {
            return totalPoints / (double) ratings.size();
        }
    }

    public boolean hasUser(int uid) {
        return !userMapper.selectList(uid).isEmpty();
    }
}
