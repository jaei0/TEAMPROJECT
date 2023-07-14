package com.parking.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(
    method = RequestMethod.GET,
    path = "/parking_area"
)
public class RestAPIController {

    private final String url = "jdbc:mariadb://localhost:33061/api";
    private final String username = "study";
    private final String password = "test1234";

    @GetMapping
    public List<Map<String, Object>> getData() {
        Connection connection;
        PreparedStatement statement;
        ResultSet resultSet;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            String query = "SELECT * FROM parking_area LIMIT 4765";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                resultList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }
}