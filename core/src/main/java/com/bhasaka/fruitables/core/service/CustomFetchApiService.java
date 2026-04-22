package com.bhasaka.fruitables.core.service;


import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = CustomFetchApiService.class, immediate = true)
@Designate(ocd = CustomFetchApiConfig.class)
public class CustomFetchApiService {
    Logger log= LoggerFactory.getLogger(CustomFetchApiService.class);

    private CustomFetchApiConfig config;

    @Activate
    @Modified
    protected void activate(CustomFetchApiConfig config) {
        this.config = config;
        log.info("==================================== URL ::"+config.apiUrl());
    }

    public String getApiData() {
        if (!config.apiEnabled()) {
            return "[]";
        }

        try {
            URL url = new URL(config.apiUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
}
