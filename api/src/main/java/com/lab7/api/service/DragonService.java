package com.lab7.api.service;

import com.lab7.api.dao.DragonDao;
import com.lab7.api.entity.Dragon;
import com.lab7.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Stack;

@AllArgsConstructor
@Getter @Setter
public class DragonService {

    private DragonDao dao;

    public Dragon save(Dragon dragon, User user) {

        if(user.getName().equals(dragon.getUser_name())) {
            return dao.save(dragon);
        }

        return null;
    }

    public void saveAll(List<Dragon> entities) {
        dao.saveAll(entities);
    }

    public Stack<Dragon> findAll() {
        return dao.findAll();
    }

    public Dragon findById(Integer id) {
        return dao.findById(id);
    }

    public boolean update(Dragon dragon, User user) {
        if(user.getName().equals(dragon.getUser_name())) {
            return dao.update(dragon);
        }
        return false;
    }

    public boolean delete(Dragon dragon, User user) {
        if(user.getName().equals(dragon.getUser_name())) {
            return dao.delete(dragon);
        }
        return false;
    }

    public boolean deleteById(Integer id, User user) {
        Dragon dragon = findById(id);

        if(user.getName().equals(dragon.getUser_name())) {
            return dao.deleteById(id);
        }
        return false;
    }

    public void deleteAll() {
        dao.deleteAll();
    }

}
