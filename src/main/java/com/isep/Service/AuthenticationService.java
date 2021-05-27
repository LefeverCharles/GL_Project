package com.isep.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.isep.entity.User;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class AuthenticationService {
    public String getToken(User user) {
        String token = "";
        int idN = user.getId();
        String id = Integer.toString(idN);
        try {
            token = JWT.create()
                    .withAudience(id)                                 // 将 user id 保存到 token 里面
                    .sign((Algorithm.HMAC256(user.getPassword())));   // 以 password 作为 token 的密钥
        } catch (UnsupportedEncodingException ignore) {

        }
        return token;
    }

    public Boolean decodeToken(User user,String token){
        JWTVerifier jwtVerifier = null;
        try {
            jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("password not match in DB");
        }
        return true;
    }
}
