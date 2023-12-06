package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.accessory.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.*;
import com.example.demo.util.EncryptAndDecryptStr;
import com.example.demo.util.JwtUtil;
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
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserActionMapper userActionMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductActionMapper productActionMapper;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private UrlidtoproductsMapper urlidtoproductsMapper;
    @Autowired
    private CommentonuserMapper commentonuserMapper;

    @Value("${static.path}")
    private String staticPath;


    @GetMapping("/user")
    public Map myself(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        List<Users> list = userMapper.selectList(uid);
        if (list.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到用户");
        } else {
            Users user = list.get(0);
            dataMap.put("ret", user);
            metaMap.put("status", 200);
            metaMap.put("msg", "访问成功");
        }
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/uploadphoto")
    public Map photo(MultipartFile file, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        String path = request.getServletContext().getRealPath("/upload/user" + uid + "/");
        String name = saveFile(file, staticPath + "/images/user" + uid + "/", new Date().toString().replace(" ", "").replace(":", "").replace("+", ""));
        List<Users> users = userMapper.selectList(uid);
        if (users.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "用户不存在");
        } else {
            Users user = users.get(0);
            String oldPath = user.getUser_img_url();
            File old = new File(oldPath);
            if (old.exists()) {
                if (!old.delete()) {
                    System.out.println("删除原图片失败");
                }
            }
            user.setUser_img_url(name);
            userMapper.update(user);
            dataMap.put("url", name);
            metaMap.put("status", 200);
            metaMap.put("msg", "上传成功");
        }
        map.put("data", dataMap);
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

    @PostMapping("/user/updateInfo")
    public Map updateUserInfo(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        String newUserName = jsonParam.getString("username");
        String newEmail = jsonParam.getString("email");
        String newPhone = jsonParam.getString("phone");
        Users user = userMapper.selectList(uid).get(0);

        user.setUsername(newUserName);
        user.setEmail(newEmail);
        user.setPhone(newPhone);

        int i = userMapper.update(user);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "修改成功");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "修改失败");
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/editpwd")
    public Map updateUserPassword(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        int uid = Integer.parseInt(JwtUtil.getUserId(token));
        Users user = userMapper.selectList(uid).get(0);
        String newPassword = (String) jsonParam.get("password");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();

        if (newPassword.length() < 6 || newPassword.length() > 16) {
            metaMap.put("msg", "修改失败，请检查密码长度");
            metaMap.put("status", 500);
            map.put("meta", metaMap);
            return map;
        }
        String username = user.getUsername();
        newPassword = EncryptAndDecryptStr.encryptStr(username, newPassword);
        user.setPassword(newPassword);

        // 修改密码后，退出登录状态
        user.setToken("");
        int i = userMapper.update(user);
        if (i > 0) {
            metaMap.put("msg", "修改成功");
            metaMap.put("status", 200);
        } else {
            metaMap.put("msg", "修改失败");
            metaMap.put("status", 500);
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/root/user")
    public Map query() {
        List<Users> list = userMapper.selectAll();
        System.out.println(list);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ret", list);
        metaMap.put("status", 200);
        metaMap.put("msg", "所有用户");
        map.put("data", dataMap);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/token")
    public Map deleteToken(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String token = request.getHeader("accessToken");
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int i = userMapper.deleteToken(token);
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "成功退出");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "退出异常，请联系管理员");
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/root/user/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int uid = (int) jsonParam.get("uid");
        List<Users> list = userMapper.selectList(uid);
        int rootUid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        System.out.println(list);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (list.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到用户");
        } else {
            Users user = list.get(0);
            user.setUser_status(2);
            int i = userMapper.update(user);
            if (i > 0) {
                Useraction useraction = new Useraction(0, rootUid, uid, 2, new Date(), 0);
                userActionMapper.insert(useraction);
                metaMap.put("status", 200);
                metaMap.put("msg", "封号成功");
            } else {
                Useraction useraction = new Useraction(0, rootUid, uid, 2, new Date(), 1);
                userActionMapper.insert(useraction);
                metaMap.put("status", 500);
                metaMap.put("msg", "封号失败");
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/info")
    public Map info(int uid) {
        Users user = userMapper.selectList(uid).get(0);
        List<Products> products = productMapper.selectByUid(uid);
        List<ProductWithPhoto> out = new ArrayList<>();
        int uploaded = products.size();
        int soldedout = 0;
        int purchased = 0;
        double rate = 0.0;
        int count = 0;
        for (Products i : products) {
            ProductWithPhoto outP = new ProductWithPhoto(i);
            outP.setImage(urlidtoproductsMapper.selectUrlByPid(i.getPid()));
            out.add(outP);
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
        for (Productaction i : productActionMapper.selectByUid(uid)) {
            if (i.getAction_type() == 1) {
                purchased++;
            }
        }
        for (Rating i : ratingMapper.searchById(uid)) {
            rate += i.getRating();
            count++;
        }

        // 获取用户评价
        List<Commentonuser> commentsonuser = commentonuserMapper.selectByReceiverId(uid);
        List<CommentWithUser> reviews = new ArrayList<>();
        for (Commentonuser tmp : commentsonuser) {
            Users sender = userMapper.selectList(tmp.getUid1()).get(0);
            reviews.add(new CommentWithUser(sender, tmp));
        }

        UserInfo info = new UserInfo(user, uploaded, purchased, soldedout, 0, rate / count, out, reviews);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ret", info);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        System.out.println(map.toString());
        System.out.println("over");
        return map;
    }

    @GetMapping("/root/user/all")
    public Map rootAll() {
        List<Users> l = userMapper.selectAll();
        List<UserLessInfo> info = new ArrayList<>();
        // (int uid, String username, int uploaded, boolean is_admin, int status)
        for (Users i : l) {
            info.add(new UserLessInfo(i.getUid(), i.getUsername(),
                    productMapper.selectByUid(i.getUid()).size(), i.isIs_admin(), i.getUser_status()));
        }
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("items", info);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }

    @PostMapping("/root/user")
    public Map rootChange(@RequestBody JSONObject jsonParam) {
        int uid = (int) jsonParam.get("uid");
        boolean isadmin = (boolean) jsonParam.get("isadmin");
        int status = (int) jsonParam.get("status");
        Users user = userMapper.selectList(uid).get(0);
        user.setIs_admin(isadmin);
        user.setUser_status(status);
        int i = userMapper.update(user);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "修改成功");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "修改失败");
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/action")
    public Map actionInfo(int uid, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Productaction> productactions = productActionMapper.selectBuy(uid);
        List<BuyInfo> out = new ArrayList<>();
        for (Productaction i : productactions) {
            Products product = productMapper.selectById(i.getPid()).get(0);
            Users seller = userMapper.selectList(i.getUid()).get(0);
            List<Rating> ratings = ratingMapper.searchByIds(uid, seller.getUid());
            double rate = ratings.get(ratings.size() - 1).getRating();
            out.add(new BuyInfo(product, i, seller, rate));
        }
        dataMap.put("ret", out);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }

    @GetMapping("/user/history")
    public Map historyInfo(HttpServletRequest request) {
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<HistoryInfo> out = new ArrayList<>();
        List<HistoryInfo> buyInfo = userActionMapper.selectBuyAction(uid);
        List<HistoryInfo> sellInfo = userActionMapper.selectSellAction(uid);
        for (HistoryInfo i : sellInfo) {
            i.setAction(1);
        }
        out.addAll(buyInfo);
        out.addAll(sellInfo);
        dataMap.put("ret", out);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }

    @GetMapping("/hello")
    public Map hello() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Products> r = productMapper.onSell();
        Random random = new Random(new Date().getTime());
        int a = random.nextInt(r.size());
        int b = random.nextInt(r.size());
        if (a == b) {
            b = (a + 1) % r.size();
        }
        List<Products> retList = new ArrayList<>();
        retList.add(r.get(a));
        retList.add(r.get(b));
        List<ProductOutput> out = new ArrayList<>();
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
            out.add(new ProductOutput(product, createUser, url, uploaded, soldedout));
        }
        dataMap.put("ret", out);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }

    @GetMapping("/user/actions")
    public Map historyAction(int uid) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Productaction> out = productActionMapper.selectByUid(uid);
        dataMap.put("ret", out);
        metaMap.put("status", 200);
        metaMap.put("msg", "查询成功");
        map.put("meta", metaMap);
        map.put("data", dataMap);
        return map;
    }
}
