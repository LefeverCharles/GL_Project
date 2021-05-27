package com.isep.Service;

import com.isep.dao.UserDao;
import com.isep.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userdao;

    @Override
    public List<User> findAll() {
        return userdao.findAll();
    }

    @Override
    public int save(User user) {
        return userdao.save(user);
    }

    @Override
    public int updateUserInfo(User user) {
        return userdao.updateUserInfo(user);
    }

    @Override
    public User findOne(String email,String password) {
        return userdao.findOne(email,password);
    }

    @Override
    public User findByEmail(String email) {
        return userdao.findByEmail(email);
    }

    @Override
    public User findById(int id) {
        return userdao.findById(id);
    }

    @Override
    public boolean comparePassword(User user, User userInDataBase) {
        return user.getPassword()
                .equals(userInDataBase.getPassword());
    }
}
