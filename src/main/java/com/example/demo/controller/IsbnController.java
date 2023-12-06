package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Isbninformation;
import com.example.demo.mapper.ISBNInformationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class IsbnController {
    @Autowired
    ISBNInformationMapper isbnInformationMapper;

    @PostMapping("/root/isbn/add")
    public Map addInfo(@RequestBody JSONObject jsonParam) {
        String isbn = (String) jsonParam.get("isbn");
        String book_name= (String) jsonParam.get("book_name");
        double book_price = Double.parseDouble((String) jsonParam.get("book_price"));
        Isbninformation isbnInformation = new Isbninformation(isbn, book_name, book_price);
        int i;
        if(isbnInformationMapper.selectById(isbn).isEmpty()){
        i= isbnInformationMapper.insert(isbnInformation);}
        else {
            i = isbnInformationMapper.update(isbnInformation);
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if(i>0) {
            metaMap.put("msg", "请求成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "添加失败");
            metaMap.put("status", 500);
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/root/isbn/delete")
    public Map deleteInfo(@RequestBody JSONObject jsonParam){
        Map<String, Object> map = new HashMap<>();
        String isbn = (String) jsonParam.get("isbn");
        int i = isbnInformationMapper.deleteById(isbn);
        Map<String, Object> metaMap = new HashMap<>();
        if(i>0) {
            metaMap.put("msg", "请求成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "删除失败");
            metaMap.put("status", 500);
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/isbn/all")
    public Map showInfo(){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Isbninformation> list = isbnInformationMapper.selectAll();
        dataMap.put("ret", list);
        map.put("data", dataMap);
        Map<String, Object> metaMap = new HashMap<>();
        metaMap.put("msg", "请求成功");
        metaMap.put("status", 200);
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/root/isbn/get")
    public Map getInfo(String isbn){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Isbninformation> list = isbnInformationMapper.selectById(isbn);
        Isbninformation i = list.get(0);
        dataMap.put("ret", i);
        map.put("data", dataMap);
        Map<String, Object> metaMap = new HashMap<>();
        if(i!=null) {
            metaMap.put("msg", "请求成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "没有数据项");
            metaMap.put("status", 410);
        }
        map.put("meta", metaMap);
        return map;
    }
}
