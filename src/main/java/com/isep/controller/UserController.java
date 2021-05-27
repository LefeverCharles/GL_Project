package com.isep.controller;
import com.alibaba.druid.sql.visitor.functions.If;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.isep.Service.AuthenticationService;
import com.isep.Service.UserService;
import com.isep.entity.User;
import com.sun.org.apache.bcel.internal.generic.I2F;
import org.json.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin //前端接口为8080，后端接口为8989，这个参数使用可进行跨域访问
@RequestMapping("/user")  //类路由
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/findAll") //设置请求方式和路由地址
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping(value = "/save",produces = "application/json")
    public Object save(@RequestBody User user) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        if (userService.findByEmail(user.getEmail()) != null){
            jsonObject.put("code","0");
            jsonObject.put("message","Email has used");
        }else {
            int save = userService.save(user);
            if (save == 1){
                jsonObject.put("code","1");
                jsonObject.put("message","sign up successfully");
            }else {
                jsonObject.put("code","2");
                jsonObject.put("message","failed");
            }
        }
        return jsonObject;
    }

    @PostMapping("/login")
    public Object findOne(@RequestBody User u){
        User userInDataBase = userService.findByEmail(u.getEmail());
        JSONObject jsonObject = new JSONObject();
        if (userInDataBase == null){
            jsonObject.put("code", "0");
            jsonObject.put("msg", "Email mistake");
        }else if (!userService.comparePassword(u,userInDataBase)){
            jsonObject.put("code", "2");
            jsonObject.put("msg", "Password mistake");
        }else {
            String token = authenticationService.getToken(userInDataBase);
            jsonObject.put("code", "1");
            jsonObject.put("token", token);
            jsonObject.put("msg", "sign in successfully");
        }
        return jsonObject;
    }

    @PostMapping("/profileCheck")
    public Object profileCheck(@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("phoneNumber", user.getPhoneNumber());
            jsonObject.put("residentialAddress", user.getResidentialAddress());
        }else {
            jsonObject.put("code", "0");
            jsonObject.put("msg", "failed");
        }
        return jsonObject;
    }

    @PostMapping("/profileEdit")
    public Object profileEdit(@RequestBody User newUser,@RequestParam String token){
        JSONObject jsonObject = new JSONObject();
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new RuntimeException("Verify token failed");
        }
        int id = Integer.parseInt(userId);
        User user = userService.findById(id);
        if (authenticationService.decodeToken(user,token)){
            newUser.setId(id);
            int i = userService.updateUserInfo(newUser);
            if (i == 1){
                String newToken = authenticationService.getToken(newUser);
                jsonObject.put("code", "1");
                jsonObject.put("token", newToken);
                jsonObject.put("msg", "Edit successfully");
            }else{
                jsonObject.put("code", "0");
                jsonObject.put("msg", "Edit failed");
            }
        }else{
            jsonObject.put("code", "0");
            jsonObject.put("msg", "token mistake");
        }
        return jsonObject;
    }

}