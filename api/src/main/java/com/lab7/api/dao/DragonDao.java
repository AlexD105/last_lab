package com.lab7.api.dao;

import com.lab7.api.entity.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Stack;

public class DragonDao implements GenericDao<Dragon, Integer> {

    private JdbcConfig jdbc;

    public DragonDao() {
        try {
            jdbc = new JdbcConfig();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dragon save(Dragon dragon) {
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement(
                    "INSERT INTO DRAGON (" +
                            "NAME, " +
                            "X_COORDINATE, " +
                            "Y_COORDINATE, " +
                            "CREATION_DATE, " +
                            "AGE, " +
                            "COLOR, " +
                            "DRAGON_TYPE, " +
                            "DRAGON_CHARACTER, " +
                            "CAVE_DEPTH, " +
                            "CAVE_NUM_OF_TREASURES," +
                            "USER_NAME)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            convertToStatement(dragon, statement);
            int rowsInserted = statement.executeUpdate();
            if(rowsInserted > 0) {
                return dragon;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAll(List<Dragon> entities) {
        entities.forEach(this::save);
    }

    @Override
    public Stack<Dragon> findAll() {

        Stack<Dragon> dragons = new Stack<>();
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement("SELECT * FROM DRAGON");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dragons.add(convertToDragon(resultSet));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dragons;
    }

    @Override
    public Dragon findById(Integer id) {
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement(
                    "SELECT * FROM DRAGON WHERE id=?"
            );
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return convertToDragon(resultSet);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Dragon dragon) {

        try {

            PreparedStatement statement = jdbc.getConnection().prepareStatement(
                    "UPDATE dragon SET " +
                            "NAME=?, " +
                            "X_COORDINATE=?, " +
                            "Y_COORDINATE=?, " +
                            "CREATION_DATE=?, " +
                            "AGE=?, " +
                            "COLOR=?, " +
                            "DRAGON_TYPE=?, " +
                            "DRAGON_CHARACTER=?, " +
                            "CAVE_DEPTH=?, " +
                            "CAVE_NUM_OF_TREASURES=?, " +
                            "USER_NAME=?" +
                            "where id=?"
            );

            convertToStatement(dragon, statement);

            return statement.executeUpdate() > 0;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Dragon dragon) {
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement(
                    "DELETE FROM dragon WHERE id=?"
            );
            statement.setLong(1, dragon.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteById(Integer id) {
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement(
                    "DELETE FROM dragon WHERE id=?"
            );
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public void deleteAll() {
        try {
            PreparedStatement statement = jdbc.getConnection().prepareStatement("TRUNCATE dragon");
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Dragon convertToDragon(ResultSet resultSet) throws SQLException {
        Dragon dragon = new Dragon();
        dragon.setId(resultSet.getInt(1));
        dragon.setName(resultSet.getString(2));
        dragon.setCoordinates(new Coordinates(resultSet.getDouble(3), resultSet.getInt(4)));
        dragon.setCreationDate(resultSet.getString(5));
        dragon.setAge(resultSet.getLong(6));
        dragon.setColor(Color.valueOf(resultSet.getString(7)));
        dragon.setType(DragonType.valueOf(resultSet.getString(8)));
        dragon.setCharacter(DragonCharacter.valueOf(resultSet.getString(9)));
        dragon.setCave(new DragonCave(resultSet.getLong(10), resultSet.getLong(11)));
        dragon.setUser_name(resultSet.getString(12));
        return dragon;
    }

    private void convertToStatement(Dragon dragon, PreparedStatement statement) throws SQLException {
        statement.setString(1, dragon.getName());
        statement.setDouble(2, dragon.getCoordinates().getX());
        statement.setInt(3, dragon.getCoordinates().getY());
        statement.setString(4, dragon.getCreationDate());
        statement.setLong(5, dragon.getAge());
        statement.setString(6, dragon.getColor().name());
        statement.setString(7, dragon.getType().name());
        statement.setString(8, dragon.getCharacter().name());
        statement.setLong(9, dragon.getCave().getDepth());
        statement.setLong(10, dragon.getCave().getNumberOfTreasures());
        statement.setString(11, dragon.getUser_name());
    }
}
