package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Categories;
import com.example.demo.mapper.CategoriesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CategoriesController {
    @Autowired
    CategoriesMapper categoriesMapper;

    @PostMapping("/root/category/add")
    public Map add(@RequestBody JSONObject jsonParam) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        String category_name = (String) jsonParam.get("category_name");
        Categories categories = new Categories(0, category_name);
        int i = categoriesMapper.insert(categories);
        if (i > 0) {
            metaMap.put("msg", "请求成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "添加失败");
            metaMap.put("status", 500);
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/category/all")
    public Map all() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Categories> categoriesList = categoriesMapper.selectAll();
        dataMap.put("ret", categoriesList);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/root/category/delete")
    public Map delete(@RequestBody JSONObject jsonParam) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int i = categoriesMapper.deleteById((int) jsonParam.get("category_id"));
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "查询成功");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "删除失败");
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/root/category/edit")
    public Map edit(@RequestBody JSONObject jsonParam) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        String category_name = (String) jsonParam.get("category_name");
        int category_id = (int)jsonParam.get("category_id");
        Categories categories = new Categories(category_id, category_name);
        int i = categoriesMapper.update(categories);
        if (i > 0) {
            metaMap.put("msg", "请求成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "添加失败");
            metaMap.put("status", 500);
        }
        map.put("meta", metaMap);
        return map;
    }
}
