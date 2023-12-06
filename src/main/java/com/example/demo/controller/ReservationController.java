package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.accessory.ProductWithPhoto;
import com.example.demo.accessory.ReservationOut;
import com.example.demo.entity.Productaction;
import com.example.demo.entity.Products;
import com.example.demo.entity.Reservation;
import com.example.demo.mapper.ProductActionMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ReservationMapper;
import com.example.demo.mapper.UrlidtoproductsMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class ReservationController {
    @Autowired
    ReservationMapper reservationMapper;
    @Autowired
    ProductActionMapper productActionMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    UrlidtoproductsMapper urlidtoproductsMapper;

    @PostMapping("/user/reservation/add")
    public Map add(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        int number = 1;
        if (jsonParam.get("number") != null) {
            number = (int) jsonParam.get("number");
        }
        // 获取当前时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        // 把日期往后增加一天,整数  往后推,负数往前移动
        calendar.add(Calendar.MONTH, 1);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        Reservation reservation = new Reservation(pid, uid, number, new Date(), new Date(), date, true);
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到商品");
        } else {
            Products product = products.get(0);
            if (product.getProduct_status() == 0) {
                metaMap.put("status", 410);
                metaMap.put("msg", "找不到商品");
            } else {
                product.setProduct_status(2);
                productMapper.updateById(product);
                int i = reservationMapper.insert(reservation);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "预定成功");
                    productActionMapper.insert(new Productaction(0, uid, pid, 3, new Date(), 0));
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "预定失败");
                    productActionMapper.insert(new Productaction(0, uid, pid, 3, new Date(), 1));
                }
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/reservation/edit")
    public Map edit(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int number = 1;
        if (jsonParam.get("number") != null) {
            number = (int) jsonParam.get("number");
        }
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Reservation> list = reservationMapper.searchByIds(uid, pid);
        if (!list.isEmpty()) {
            Reservation reservation = list.get(0);
            reservation.setNumber(number);
            reservation.setUpdated_time(new Date());
            int i = reservationMapper.updateNumberByIds(reservation);
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

    @PostMapping("/user/reservation/delete")
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
                metaMap.put("msg", "权限不足，请删除自己的预定");
            } else {
                int i = reservationMapper.deleteByIds(deleteUid, pid);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "成功删除");
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "删除失败");
                }
            }
        } else {
            int i = reservationMapper.deleteByIds(workingUid, pid);
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

    @GetMapping("/user/reservation/search")
    public Map search(int page, int page_capacity, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        page = page - 1;
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Reservation> list = reservationMapper.searchByUid(uid);
        list.sort(new Comparator<Reservation>() {
            @Override
            public int compare(Reservation o1, Reservation o2) {
                return o2.getUpdated_time().compareTo(o1.getUpdated_time());
            }
        });
        List<Reservation> retList;
        int start = page * page_capacity;
        int end = (page + 1) + page_capacity;
        System.out.println(list.size());
        if (start >= list.size()) {
            retList = new ArrayList<>();
        } else {
            if (end > list.size()) {
                end = list.size();
            }
            retList = list.subList(start, end);
        }
        List<ReservationOut> out = new ArrayList<>();
        System.out.println(retList.size());
        for(Reservation i:retList) {
            Products product = productMapper.selectById(i.getPid()).get(0);
            ProductWithPhoto j = new ProductWithPhoto(product);
            j.setImage(urlidtoproductsMapper.selectUrlByPid(i.getPid()));
            out.add(new ReservationOut(i, j));
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
