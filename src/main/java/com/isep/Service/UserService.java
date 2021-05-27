package com.isep.Service;

import com.isep.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    int save(User user);
    int updateUserInfo(User user);
    User findOne(String email,String password);
    User findByEmail(String email);
    User findById(int id);
    boolean comparePassword(User user, User userInDataBase);
}
