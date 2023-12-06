package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.accessory.CommentWithUser;
import com.example.demo.entity.Comments;
import com.example.demo.entity.Products;
import com.example.demo.entity.Users;
import com.example.demo.mapper.CommentsMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin
public class CommentsController {
    @Autowired
    CommentsMapper commentsMapper;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/user/comment/add")
    public Map add(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        String comment_content = (String) jsonParam.get("comment_content");
        String isbn = (String) jsonParam.get("isbn");
        Comments comment = new Comments(0, comment_content, isbn, Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken"))), new Date(), new Date());
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        int i = commentsMapper.insert(comment);
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

    @GetMapping("/user/comment/look")
    public Map look(String isbn, int page, int page_capacity) {
        page = page - 1;
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        List<Comments> list = commentsMapper.selectByISBN(isbn);
        list.sort((o1, o2) -> o2.getUpdate_time().compareTo(o1.getUpdate_time()));
        int start = page * page_capacity;
        int end = (page + 1) * page_capacity;
        if (start >= list.size()) {
            start = 0;
            end = 0;
        } else if (end > list.size()) {
            end = list.size();
        }
        List<Comments> commentList = list.subList(start, end);
        List<CommentWithUser> retList = new ArrayList<>();
        for (Comments comment : commentList) {
            int uid = comment.getUid();
            Users user = userMapper.selectList(uid).get(0);
            retList.add(new CommentWithUser(user, comment));
        }
        dataMap.put("ret", retList);
        map.put("data", dataMap);
        metaMap.put("msg", "成功搜索，返回页" + page);
        metaMap.put("status", 200);
        map.put("meta", metaMap);
        return map;
    }

    @PostMapping("/user/comment/edit")
    public Map editComment(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int comment_id = (int) jsonParam.get("comment_id");
        String comment_content = (String) jsonParam.get("comment_content");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
        List<Comments> comments = commentsMapper.selectById(comment_id);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (comments.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Comments comment = comments.get(0);
            if (is_admin || (uid == comment.getUid())) { // 允许修改
                comment.setComment_content(comment_content);
                comment.setUpdate_time(new Date());
                int i = commentsMapper.updateById(comment);
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

    @PostMapping("/user/comment/delete")
    public Map delete(@RequestBody JSONObject jsonParam, HttpServletRequest request) {
        int comment_id = (int) jsonParam.get("comment_id");
        int uid = Integer.parseInt(JwtUtil.getUserId(request.getHeader("accessToken")));
        boolean is_admin = JwtUtil.getUserIsAdmin(request.getHeader("accessToken"));
        List<Comments> comments = commentsMapper.selectById(comment_id);
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> metaMap = new HashMap<>();
        if (comments.isEmpty()) {
            metaMap.put("status", 410);
            metaMap.put("msg", "找不到表项");
        } else {
            Comments comment = comments.get(0);
            if (is_admin || (uid == comment.getUid())) { // 允许删除
                int i = commentsMapper.deleteById(comment.getComment_id());
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
