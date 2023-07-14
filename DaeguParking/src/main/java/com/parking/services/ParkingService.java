package com.parking.services;

import com.parking.entities.ParkingEntity;
import com.parking.mappers.IParkingMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingService {
    private final IParkingMapper parkingMapper;

    public ParkingService(IParkingMapper parkingMapper) {
        this.parkingMapper = parkingMapper;
    }

    @Transactional
    public void getData() throws
            IOException {
        updateData();
    }

    private void updateData() throws
            IOException {

        List<ParkingEntity> parkingList = new ArrayList<>();
        URL url = new URL("https://apis.data.go.kr/6270000/dgBuildingPark/getBuildingParkList?serviceKey=imii4iaHoy0f4wVlWC6T2EOKj%2Fs8zV2gxZE%2BiWceNtLBxioJ%2FIPSRXqn7oNNfL%2BpgPrSQ2qYP5B3p1kHza0RdQ%3D%3D&numOfRows=4765&pageNo=1&type=json");
        URLConnection urlConnection = url.openConnection();
        StringBuilder responseBuilder = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream())) {
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    responseBuilder.append(line);
                }
            }
        }
        JSONObject responseObject = new JSONObject(responseBuilder.toString());

        JSONObject itemsObject = responseObject.getJSONObject("body").getJSONObject("items");
        JSONArray itemArray = itemsObject.getJSONArray("item");

        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject parkingObject = itemArray.getJSONObject(i);
            ParkingEntity parking = ParkingEntity.build(parkingObject);
            parkingList.add(parking);
        }

        System.out.printf("총 %,d개의 데이터가 수신되었습니다.\n",
                parkingList.size());

        for (ParkingEntity parking : parkingList) {
            this.parkingMapper.insertParking(parking);
        }
    }
}