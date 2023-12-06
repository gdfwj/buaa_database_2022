package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.accessory.ProductOutput;
import com.example.demo.accessory.ProductWithPhoto;
import com.example.demo.accessory.ProductWithPhotoAndUser;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import com.example.demo.util.JwtUtil;
import io.swagger.models.auth.In;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductActionMapper productActionMapper;
    @Autowired
    private Urlidtourlmapper urlidtourlmapper;
    @Autowired
    private UrlidtoproductsMapper urlidtoproductsMapper;
    @Value("${static.path}")
    private String staticPath;

    @RequestMapping(value = "/user/product/add", method = RequestMethod.POST)
    public Map upload(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String product_name = (String) jsonParam.get("product_name");
        double price = Double.parseDouble(jsonParam.get("price").toString());
        int product_number = (int) jsonParam.get("product_number");
        String isbn = (String) jsonParam.get("isbn");
        int category_id = (int) jsonParam.get("category_id");
        String des = (String) jsonParam.get("description");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        Products product = new Products(0, product_name, price, product_number, isbn, category_id, 1,
                new java.util.Date(),
                new java.util.Date(), uid, des);
        int i = productMapper.insert(product);
        if (i > 0) {
            metaMap.put("msg", "上传成功");
            metaMap.put("status", 200);
            productActionMapper.insert(new Productaction(0, uid, product.getPid(), 0, new Date(), 0));
        } else {
            metaMap.put("msg", "上传失败");
            metaMap.put("status", 500);
            productActionMapper.insert(new Productaction(0, uid, product.getPid(), 0, new Date(), 1));
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/product/search")
    public Map search(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        String key = (String) jsonParam.get("key");
        System.out.println(jsonParam.get("price_low"));
        double price_low = Double.parseDouble(jsonParam.get("price_low").toString());
        double price_high = Double.parseDouble(jsonParam.get("price_high").toString());
        ArrayList<Integer> category_ids;
        int f = 0;
        if (jsonParam.get("category_ids") != null) {
            category_ids = (ArrayList<Integer>) jsonParam.get("category_ids");
        } else {
            category_ids = new ArrayList<>();
            f = 1;
        }
        if (category_ids.isEmpty()) {
            f = 1;
        }
        int page = (int) jsonParam.get("page") - 1;
        int sort_status, search_status;
        int page_capability;
        if (jsonParam.get("sort_status") == null) {
            sort_status = 0;
        } else {
            sort_status = (int) jsonParam.get("sort_status");
        }
        if (jsonParam.get("search_status") == null) {
            search_status = 0;
        } else {
            search_status = (int) jsonParam.get("search_status");
        }
        if (jsonParam.get("page_capability") == null) {
            page_capability = 12;
        } else {
            page_capability = (int) jsonParam.get("page_capability");
        }
        Map<String, Object> dataMap = new HashMap<>();
        System.out.println(key);
        if (key != null) {
            StringBuilder search_category = new StringBuilder();
            if (f == 0) {
                search_category.append("(");
                for (Integer i : category_ids) {
                    if (search_category.length() == 1) {
                        search_category.append("category_id = ").append(i);
                    } else {
                        search_category.append(" or category_id = ").append(i);
                    }
                }
                search_category.append(")");
                search_category.append(" and ");
            } else {
                search_category.append("");
            }
            String status_line;
            if (search_status == 0) {
                status_line = "( product_status = 1 or product_status = 2)";
            } else if (search_status == 1) {
                status_line = "( product_status = 1 or product_status = 2 ) and created_by_uid = " + uid;
            } else if (search_status == 2) {
                status_line = "( product_status = 1 or product_status = 2 ) and created_by_uid <> " + uid;
            } else {
                status_line = "";
            }
            List<Products> list = productMapper.fuzzySearch("%" + key + "%",
                    price_low, price_high, search_category.toString(), status_line);
            sortByStatus(list, sort_status);
            int start = page * page_capability;
            int end = (page + 1) * page_capability;
            if (start >= list.size()) {
                start = 0;
                end = 0;
            } else if (end > list.size()) {
                end = list.size();
            }
            List<Products> retList = list.subList(start, end);
            List<ProductOutput> outputList = new ArrayList<>();
            for (Products product : retList) {
                int createUid = product.getCreated_by_uid();
                Users createUser = userMapper.selectList(createUid).get(0);
                List<String> urls = urlidtoproductsMapper.selectUrlByPid(product.getPid());
                String url;
                if (!urls.isEmpty()) {
                    url = urls.get(0);
                } else {
                    url = "";
                }
                List<Products> productsByCreate = productMapper.selectByUid(createUid);
                int uploaded = productsByCreate.size();
                int soldedout = 0;
                for (Products i : productsByCreate) {
                    boolean flag = false;
                    for (int j : productActionMapper.selectTypeByPid(i.getPid())) {
                        if (j == 1) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        soldedout++;
                    }
                }
                outputList.add(new ProductOutput(product, createUser, url, uploaded, soldedout));
            }
            dataMap.put("ret", outputList);
            dataMap.put("numBooks", list.size());
            map.put("data", dataMap);
            metaMap.put("msg", "成功搜索，返回页" + page);
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "参数不能为空");
            metaMap.put("status", 422);
        }
        map.put("meta", metaMap);
        return map;
    }

    private void sortByStatus(List<Products> list, int sort_status) {
        if (sort_status == 0) { // 按更新时间倒序
            list.sort(new Comparator<Products>() {
                @Override
                public int compare(Products o1, Products o2) {
                    return o2.getUpdate_time().compareTo(o1.getUpdate_time());
                }
            });
        }
        if (sort_status == 1) { // 按价格正序
            Collections.sort(list, new Comparator<Products>() {
                @Override
                public int compare(Products o1, Products o2) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                }
            });
        }
        if (sort_status == 2) { // 按价格倒序
            Collections.sort(list, new Comparator<Products>() {
                @Override
                public int compare(Products o1, Products o2) {
                    return Double.compare(o2.getPrice(), o1.getPrice());
                }
            });
        }
    }

    @PostMapping("user/product/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int pid = (int) jsonParam.get("pid");
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        List<Users> users = userMapper.selectList(uid);
        Users user = users.get(0);
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "没有表项");
            productActionMapper.insert(new Productaction(0, uid, pid, 4, new Date(), 1));
        } else {
            Products product = products.get(0);
            if (user.isIs_admin() || product.getCreated_by_uid() == user.getUid()) { // 权限判断
                product.setProduct_status(0);
                int i = productMapper.updateById(product);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "删除成功");
                    productActionMapper.insert(new Productaction(0, uid, pid, 4, new Date(), 0));
                } else {
                    metaMap.put("status", 410);
                    metaMap.put("msg", "没有表项");
                    productActionMapper.insert(new Productaction(0, uid, pid, 4, new Date(), 1));
                }
            } else {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足");
                productActionMapper.insert(new Productaction(0, uid, pid, 4, new Date(), 1));
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("user/product/edit")
    public Map edit(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int pid = (int) jsonParam.get("pid");
        String product_name = (String) jsonParam.get("product_name");
        double price = (double) jsonParam.get("price");
        int product_number = (int) jsonParam.get("product_number");
        String isbn = (String) jsonParam.get("isbn");
        int category_id = (int) jsonParam.get("category_id");
        String token = request.getHeader("accessToken");
        String des = (String) jsonParam.get("description");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        List<Users> users = userMapper.selectList(uid);
        Users user = users.get(0);
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("msg", "没有表项");
            metaMap.put("status", 410);
            productActionMapper.insert(new Productaction(0, uid, pid, 5, new Date(), 1));
        } else {
            Products product = products.get(0);
            if (user.isIs_admin() || product.getCreated_by_uid() == user.getUid()) { // 权限判断
                product.setProduct_name(product_name);
                product.setPrice(price);
                product.setProduct_number(product_number);
                product.setIsbn(isbn);
                product.setCategory_id(category_id);
                product.setUpdate_time(new Date());
                product.setText(des);
                int i = productMapper.updateById(product);
                if (i > 0) {
                    metaMap.put("msg", "修改成功");
                    metaMap.put("status", 200);
                    productActionMapper.insert(new Productaction(0, uid, pid, 5, new Date(), 0));
                } else {
                    metaMap.put("msg", "修改失败");
                    metaMap.put("status", 500);
                    productActionMapper.insert(new Productaction(0, uid, pid, 5, new Date(), 1));
                }
            } else {
                metaMap.put("msg", "权限不足");
                metaMap.put("status", 403);
                productActionMapper.insert(new Productaction(0, uid, pid, 5, new Date(), 1));
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/product/get")
    public Map get(int pid, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("msg", "没有表项");
            metaMap.put("status", 410);
        } else {
            Products product = products.get(0);
            List<Users> users = userMapper.selectList(product.getCreated_by_uid());
            if (users.isEmpty()) {
                metaMap.put("msg", "无创建商品用户");
                metaMap.put("status", 410);
            }
            Users user = users.get(0);
            ProductWithPhotoAndUser out = new ProductWithPhotoAndUser(product, user);
            out.setImage(urlidtoproductsMapper.selectUrlByPid(pid));
            dataMap.put("ret", out);
            map.put("data", dataMap);
            if (product != null) {
                metaMap.put("msg", "获取成功");
                metaMap.put("status", 200);
            } else {
                metaMap.put("msg", "没有表项");
                metaMap.put("status", 200);
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/product/uploadPhoto")
    public Map uploadPhoto(int pid, MultipartFile file, HttpServletRequest request) {
        System.out.println(pid);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Products product = products.get(0);
            if (product.getCreated_by_uid() == uid) {
                String url = saveFile(file, staticPath + "/images/product" + pid + "/",
                        new Date().toString().replace(" ", "").replace(":", "")
                                .replace("+", ""));
                Urlidtourl urlidtourl = new Urlidtourl(0, url);
                urlidtourlmapper.insert(urlidtourl);
                Urlidtourl urlidtourl1 = urlidtourlmapper.selectByUrl(url).get(0);
                Urlidtoproducts urlidtoproducts = new Urlidtoproducts(urlidtourl1.getUrlid(), pid);
                urlidtoproductsMapper.insert(urlidtoproducts);
                metaMap.put("status", 200);
                metaMap.put("msg", "添加图片成功");
                dataMap.put("url", url);
            } else {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足，请修改自己的商品");
            }
        }
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/product/searchPhoto")
    public Map getPhoto(int pid, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        List<Products> products = productMapper.selectById(pid);
        if (products.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Products product = products.get(0);
            if (product.getCreated_by_uid() == uid) {
                List<String> urls = new ArrayList<>();
                List<Urlidtoproducts> list = urlidtoproductsMapper.selectByPid(pid);
                for (Urlidtoproducts i : list) {
                    List<Urlidtourl> j = urlidtourlmapper.selectById(i.getUrlid());
                    for (Urlidtourl k : j) {
                        urls.add(k.getUrl());
                    }
                }
                dataMap.put("ret", urls);
                metaMap.put("status", 200);
                metaMap.put("msg", "获取链接成功");
            } else {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足，请修改自己的商品");
            }
        }
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/root/product/all")
    public Map rootAll() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Products> list = productMapper.all();
        List<ProductOutput> outputList = new ArrayList<>();
        for (Products product : list) {
            int createUid = product.getCreated_by_uid();
            Users createUser = userMapper.selectList(createUid).get(0);
            List<String> urls = urlidtoproductsMapper.selectUrlByPid(product.getPid());
            String url;
            if (!urls.isEmpty()) {
                url = urls.get(0);
            } else {
                url = "";
            }
            List<Products> productsByCreate = productMapper.selectByUid(createUid);
            int uploaded = productsByCreate.size();
            int soldedout = 0;
            for (Products i : productsByCreate) {
                boolean flag = false;
                for (int j : productActionMapper.selectTypeByPid(i.getPid())) {
                    if (j == 1) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    soldedout++;
                }
            }
            outputList.add(new ProductOutput(product, createUser, url, uploaded, soldedout));
        }
        dataMap.put("ret", outputList);
        dataMap.put("numBooks", list.size());
        map.put("data", dataMap);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        return map;
    }

    public String saveFile(MultipartFile file, String path, String name) {
        System.out.println(path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            Files.write(Paths.get(path + name + ".jpg"), file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/" + path.substring(path.indexOf("static")) + name + ".jpg";
    }
}
