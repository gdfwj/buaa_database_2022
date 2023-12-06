package com.example.demo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final long EXPIRE_TIME = 24*60*60*1000;
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";
    public static String sign(String username, String userId, boolean is_admin) {
        try {
            // 过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            if(is_admin) {
                return JWT.create()
                        .withHeader(header)
                        .withClaim("username", username)
                        .withClaim("userId", userId)
                        .withClaim("is_admin", "true")
                        .withExpiresAt(date)
                        .sign(algorithm);
            }
            else {
                return JWT.create()
                        .withHeader(header)
                        .withClaim("username", username)
                        .withClaim("userId", userId)
                        .withClaim("is_admin", "false")
                        .withExpiresAt(date)
                        .sign(algorithm);
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    public static boolean getUserIsAdmin(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("is_admin").asString().equals("true");
        } catch (JWTDecodeException e) {
            return false;
        }
    }
}
