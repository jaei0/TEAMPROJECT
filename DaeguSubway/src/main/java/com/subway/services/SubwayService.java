package com.subway.services;

import com.subway.entities.SubwayEntity;
import com.subway.mappers.ISubwayMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SubwayService {
    private final ISubwayMapper subwayMapper;

    public SubwayService(ISubwayMapper subwayMapper) {
        this.subwayMapper = subwayMapper;
    }

    @Transactional
    public void getGasStations() throws
            IOException {
        updateData();
    }

    private void updateData() throws
            IOException {

        List<SubwayEntity> subwayList = new ArrayList<>();
        URL url = new URL("https://api.odcloud.kr/api/15002527/v1/uddi:f6139192-8529-42c2-a1a2-dde3a7168c00?page=1&perPage=201&serviceKey=imii4iaHoy0f4wVlWC6T2EOKj%2Fs8zV2gxZE%2BiWceNtLBxioJ%2FIPSRXqn7oNNfL%2BpgPrSQ2qYP5B3p1kHza0RdQ%3D%3D");
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
        JSONArray subwayArray = responseObject.getJSONArray("data");
        for (int i = 0; i < subwayArray.length(); i++) {
            JSONObject subwayObject = subwayArray.getJSONObject(i);
            SubwayEntity subway = SubwayEntity.build(subwayObject);
            subwayList.add(subway);
        }

        System.out.printf("총 %,d개의 데이터가 수신되었습니다.\n",
                subwayList.size());

        for (SubwayEntity subway : subwayList) {
            this.subwayMapper.insertSubway(subway);
        }
    }
}