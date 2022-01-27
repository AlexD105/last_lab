package com.lab7.api.service;

import com.lab7.api.entity.User;
import com.lab7.api.dao.UserDao;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Stack;

@AllArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public void saveAll(List<User> entities) {
        userDao.saveAll(entities);
    }

    public Stack<User> findAll() {
        return userDao.findAll();
    }

    public User findById(String name) {
        return userDao.findById(name);
    }

    public boolean update(User user) {
        return userDao.update(user);
    }

    public boolean delete(User user) {
        return userDao.delete(user);
    }

    public boolean deleteById(String name) {
        return userDao.deleteById(name);
    }

    public void deleteAll() {
        userDao.deleteAll();
    }
}
