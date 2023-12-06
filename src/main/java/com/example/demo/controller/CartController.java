package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.accessory.CartOut;
import com.example.demo.accessory.ProductWithPhoto;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Productaction;
import com.example.demo.entity.Products;
import com.example.demo.entity.Reservation;
import com.example.demo.mapper.CartMapper;
import com.example.demo.mapper.ProductActionMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UrlidtoproductsMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin
public class CartController {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductActionMapper productActionMapper;
    @Autowired
    UrlidtoproductsMapper urlidtoproductsMapper;
    @Autowired
    ProductMapper productMapper;

    @PostMapping("/user/cart/add")
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
        calendar.add(Calendar.MINUTE, 60);
        // 这个时间就是日期往后推一天的结果
        date = calendar.getTime();
        Cart cart = new Cart(pid, uid, number, new Date(), new Date(), date, true);
        int i = cartMapper.insert(cart);
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "加入购物车成功");
            productActionMapper.insert(new Productaction(0, uid, pid, 2, new Date(), 0));
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "加入购物车失败");
            productActionMapper.insert(new Productaction(0, uid, pid, 2, new Date(), 1));
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/cart/edit")
    public Map edit(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int number = 1;
        if (jsonParam.get("number") != null) {
            number = (int) jsonParam.get("number");
        }
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Cart> list = cartMapper.searchByIds(uid, pid);
        if (!list.isEmpty()) {
            Cart cart = list.get(0);
            cart.setNumber(number);
            cart.setUpdated_time(new Date());
            int i = cartMapper.updateNumberByIds(cart);
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

    @PostMapping("/user/cart/delete")
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
                metaMap.put("msg", "权限不足，请删除自己的购物车");
            } else {
                int i = cartMapper.deleteByIds(deleteUid, pid);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "成功删除");
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "删除失败");
                }
            }
        } else {
            int i = cartMapper.deleteByIds(workingUid, pid);
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

    @GetMapping("/user/cart/search")
    public Map search(int page, int page_capacity, HttpServletRequest request) {
        page = page - 1;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        List<Cart> list = cartMapper.searchByUid(uid);
        list.sort(new Comparator<Cart>() {
            @Override
            public int compare(Cart o1, Cart o2) {
                return o2.getUpdated_time().compareTo(o1.getUpdated_time());
            }
        });
        List<Cart> retList;
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
        List<CartOut> out = new ArrayList<>();
        for (Cart i : retList) {
            Products product = productMapper.selectById(i.getPid()).get(0);
            ProductWithPhoto j = new ProductWithPhoto(product);
            j.setImage(urlidtoproductsMapper.selectUrlByPid(i.getPid()));
            out.add(new CartOut(i, j));
        }
        dataMap.put("ret", out);
        dataMap.put("numBooks", list.size());
        metaMap.put("status", 200);
        metaMap.put("msg", "查看成功");
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/cart/buy")
    public Map buy(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int pid = (int) jsonParam.get("pid");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));

        List<Products> productList = productMapper.selectById(pid);
        if (productList.isEmpty()) {
            metaMap.put("status", 404);
            metaMap.put("msg", "市场内无本商品(pid = " + pid + ")");
            map.put("meta", metaMap);
            return map;
        }
        Products product = productList.get(0);
        Products oldProduct = new Products(product.getPid(), product.getProduct_name(),
                product.getPrice(), product.getProduct_number(),
                product.getIsbn(), product.getCategory_id(), product.getProduct_status(),
                product.getCreate_time(), product.getUpdate_time(),
                product.getCreated_by_uid(), product.getText());

        List<Cart> orderList = cartMapper.searchByIds(uid, pid);
        if (orderList.isEmpty()) {
            metaMap.put("status", 404);
            metaMap.put("msg", "购物车内无本商品(pid = " + pid + ")");
            map.put("meta", metaMap);
            return map;
        }
        Cart order = orderList.get(0);
        if (order.getNumber() > product.getProduct_number()) {
            metaMap.put("status", 500);
            metaMap.put("msg", "库存不足");
            map.put("meta", metaMap);
            return map;
        }

        // 更新产品状态
        int newProductNumber = product.getProduct_number() - order.getNumber();
        int status = product.getProduct_status();
        if (newProductNumber == 0) {
            status = 0;
        }
        product.setProduct_status(status);
        product.setProduct_number(newProductNumber);
        product.setUpdate_time(new Date());

        int ret = productMapper.updateById(product);
        if (ret > 0) {
            ret = cartMapper.deleteByIds(uid, pid);
            if (ret > 0) {
                productActionMapper.insert(new Productaction(0, uid, pid, 1, new Date(), 0));
                metaMap.put("status", 200);
                metaMap.put("msg", "购买成功");
                map.put("meta", metaMap);
                return map;
            }
            else {
                ret = productMapper.updateById(oldProduct);
                metaMap.put("status", 500);
                metaMap.put("msg", "购买后购物车条目删除失败");
                map.put("meta", metaMap);
                return map;
            }
        }
        else {
            metaMap.put("status", 500);
            metaMap.put("msg", "购买失败");
            map.put("meta", metaMap);
            return map;
        }
    }
}
