package com.subway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

/*
@RestController
public class RestAPIController {
    @RequestMapping(
        path = {
           "/subway"
        }
    )

    @RequestMapping(value = "/subway", method = RequestMethod.GET)
    Map<String, Object> SubwayAPI() {
        //DB에서 데이터 읽어오기

        //결과물을 map 으로 반환

        return new HashMap<String, Object>() {{
            put("a", new LinkedList(){
                {
                    add("a");
                    add("b");
                    add("b");
                }
            });
        }};
    }
}
*/

@RestController
@RequestMapping(
    method = RequestMethod.GET,
    path = "/nearby_subway"
)
public class RestAPIController {

    /*
    @RequestMapping(
            path = {
                    "/nearby_subway"
            }
    )
    */

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
            String query = "SELECT * FROM nearby_subway LIMIT 201";
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