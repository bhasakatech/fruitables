package com.bhasaka.fruitables.core.service;

import com.bhasaka.fruitables.core.configurations.HarshaApiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Component(service = HarshaApiService.class)
@Designate(ocd = HarshaApiConfig.class)
public class HarshaApiServiceImpl implements HarshaApiService {

    private HarshaApiConfig harshaApiConfig;

    @Activate
    @Modified
    protected void activate(final HarshaApiConfig harshaApiConfig) {
        this.harshaApiConfig = harshaApiConfig;
    }

    @Override
    public List<Map<String, Object>> getData() {

        if (harshaApiConfig == null || !harshaApiConfig.enableApi()) {
            return Collections.emptyList();
        }

        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(harshaApiConfig.fetchUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }
            bufferedReader.close();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.toString(), List.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList(); // safe fallback
    }
}