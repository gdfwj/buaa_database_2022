package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.accessory.CommentOnUserWithUser;
import com.example.demo.accessory.CommentWithUser;
import com.example.demo.entity.Commentonuser;
import com.example.demo.entity.Users;
import com.example.demo.mapper.CommentonuserMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
public class CommentonuserController {
    @Autowired
    CommentonuserMapper commentonuserMapper;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/user/commentonuser/add")
    public Map add(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String comment_content = (String) jsonParam.get("comment_content");
        int uid2 = (int) jsonParam.get("uid");
        Commentonuser commentonuser = new Commentonuser(0, comment_content, Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken"))), uid2, new Date(), new Date());
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int i = commentonuserMapper.insert(commentonuser);
        if (i > 0) {
            metaMap.put("status", 200);
            metaMap.put("msg", "评论成功");
        } else {
            metaMap.put("status", 500);
            metaMap.put("msg", "评论失败");
        }
        map.put("meta", metaMap);
        return map;
    }

    @GetMapping("/user/commentonuser/look")
    public Map look(int receiverid, int page, int page_capacity) {
        int uid2 = receiverid;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Commentonuser> list = commentonuserMapper.selectByReceiverId(uid2);
        list.sort((o1, o2) -> o2.getUpdate_time().compareTo(o1.getUpdate_time()));
        page = page - 1;
        int start = page * page_capacity;
        int end = (page + 1) * page_capacity;
        if (start >= list.size()) {
            start = 0;
            end = 0;
        } else if (end > list.size()) {
            end = list.size();
        }
        List<Commentonuser> retList = list.subList(start, end);
        List<CommentOnUserWithUser> out = new ArrayList<>();
        for (Commentonuser i : retList) {
            int uid = i.getUid1();
            Users user = userMapper.selectList(uid).get(0);
            out.add(new CommentOnUserWithUser(user.getUsername(), user.getUser_img_url(), i));
        }
        dataMap.put("ret", out);
        map.put("data", dataMap);
        metaMap.put("msg", "成功搜索，返回页" + page);
        metaMap.put("status", 200);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/commentonuser/edit")
    public Map editComment(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int comment_id = (int) jsonParam.get("comment_id");
        String comment_content = (String) jsonParam.get("comment_content");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
        List<Commentonuser> commentsonuser = commentonuserMapper.selectById(comment_id);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (commentsonuser.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Commentonuser commentonuser = commentsonuser.get(0);
            if (is_admin || (uid == commentonuser.getUid1())) { // 允许修改
                commentonuser.setComment_content(comment_content);
                commentonuser.setUpdate_time(new Date());
                int i = commentonuserMapper.updateById(commentonuser);
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "修改成功");
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "修改失败");
                }
            } else {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足");
            }
        }
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/commentonuser/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int comment_id = (int) jsonParam.get("comment_id");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
        List<Commentonuser> commentsonuser = commentonuserMapper.selectById(comment_id);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (commentsonuser.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Commentonuser commentonuser = commentsonuser.get(0);
            if (is_admin || (uid == commentonuser.getUid1())) { // 允许删除
                int i = commentonuserMapper.deleteById(commentonuser.getComment_id());
                if (i > 0) {
                    metaMap.put("status", 200);
                    metaMap.put("msg", "删除成功");
                } else {
                    metaMap.put("status", 500);
                    metaMap.put("msg", "删除失败");
                }
            } else {
                metaMap.put("status", 403);
                metaMap.put("msg", "权限不足");
            }
        }
        map.put("meta", metaMap);
        return map;
    }
}
