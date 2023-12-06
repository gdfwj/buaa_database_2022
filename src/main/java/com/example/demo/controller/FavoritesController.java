package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.accessory.FavoriteOutput;
import com.example.demo.accessory.ProductWithPhoto;
import com.example.demo.entity.Favorites;
import com.example.demo.entity.Products;
import com.example.demo.mapper.FavoritesMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UrlidtoproductsMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class FavoritesController {
    @Autowired
    FavoritesMapper favoritesMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UrlidtoproductsMapper urlidtoproductsMapper;

    @PostMapping("/user/favorite/add")
    public Map add(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int pid = (int) jsonParam.get("pid");
        int number = 1;
        if (jsonParam.get("number") != null) {
            number = (int) jsonParam.get("number");
        }
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String end = "2999-12-31 00:00:00";
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Date expiredDate;
        try {
            expiredDate = dateFormat.parse(end);
        } catch (Exception e) {
            metaMap.put("status", 500);
            metaMap.put("msg", "日期转换寄了");
            map.put("meta", metaMap);
            return map;
        }
        Favorites favorites = new Favorites(pid, uid, number, new Date(), new Date(), expiredDate, true);
        int i = favoritesMapper.insert(favorites);
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "成功添加");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "添加失败，主键重复或服务器错误");
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/favorite/edit")
    public Map edit(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int number = 1;
        if (jsonParam.get("number") != null) {
            number = (int) jsonParam.get("number");
        }
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Favorites> list = favoritesMapper.searchByIds(uid, pid);
        if (!list.isEmpty()) {
            Favorites favorites = list.get(0);
            favorites.setNumber(number);
            favorites.setUpdated_time(new Date());
            int i = favoritesMapper.updateNumberByIds(favorites);
            if (i > 0) {
                metaMap.put("status", 200);
                metaMap.put("msg", "成功修改");
            } else {
                metaMap.put("status", 500);
                metaMap.put("msg", "修改失败");
            }
        } else {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/favorite/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int deleteUid = -1;
        if (jsonParam.get("uid") != null) {
            deleteUid = (int) jsonParam.get("uid");
        }
        int workingUid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        if (deleteUid != -1 && deleteUid != workingUid) {
            boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
            if (!is_admin) {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足，请删除自己的收藏");
            } else {
                int i = favoritesMapper.deleteByIds(deleteUid, pid);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "成功删除");
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "删除失败");
                }
            }
        } else {
            int i = favoritesMapper.deleteByIds(workingUid, pid);
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

    @GetMapping("/user/favorite/search")
    public Map search(int page, int page_capacity, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        page -= 1;
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Favorites> list = favoritesMapper.searchByUid(uid);
        list.sort(new Comparator<Favorites>() {
            @Override
            public int compare(Favorites o1, Favorites o2) {
                return o2.getUpdated_time().compareTo(o1.getUpdated_time());
            }
        });
        List<Favorites> retList;
        int start = page * page_capacity;
        int end = (page + 1) + page_capacity;
        if (start >= list.size()) {
            retList = new ArrayList<>();
        } else {
            if (end > list.size()) {
                end = list.size();
            }
            retList = list.subList(start, end);
        }
        List<FavoriteOutput> out = new ArrayList<>();
        for(Favorites i:retList) {
            Products product = productMapper.selectById(i.getPid()).get(0);
            ProductWithPhoto j = new ProductWithPhoto(product);
            j.setImage(urlidtoproductsMapper.selectUrlByPid(i.getPid()));
            out.add(new FavoriteOutput(i, j));
        }
        dataMap.put("ret", out);
        dataMap.put("numBooks", list.size());
        metaMap.put("status", 200);
        metaMap.put("msg", "查看成功");
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }
}
