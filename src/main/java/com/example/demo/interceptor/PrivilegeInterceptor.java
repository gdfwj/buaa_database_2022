package com.example.demo.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Users;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
@Component
public class PrivilegeInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception{
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("accessToken");
        System.out.println(token);
        if(null !=token) {
            boolean result = JwtUtil.verify(token);
            if(result) {
                if(JwtUtil.getUserIsAdmin(token)) {
                    return true;
                }
            }
        }
        response.getWriter().write("{\"meta\":{\"status\":401, \"msg\":\"权限不足\"}}");
        return false;
    }
}
