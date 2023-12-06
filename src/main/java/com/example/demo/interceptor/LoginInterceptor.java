package com.example.demo.interceptor;

import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception{
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("accessToken");
        System.out.println(token);
        if(null !=token) {
            boolean result = JwtUtil.verify(token);
            if(result) {
                if(!userMapper.selectToken(token).isEmpty()) {
                    return true;
                }
            }
        }
        response.getWriter().write("{\"meta\":{\"status\":401, \"msg\":\"请登录\"}}");
        return false;
    }
}
