package com.bhasaka.fruitables.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.osgi.service.component.annotations.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component(service = CountryStateService.class)
public class CountryStateServiceImpl implements CountryStateService {

    private static final String API_URL =
            "https://countriesnow.space/api/v0.1/countries/states";

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public ArrayNode getStates(String country) {

        if (country == null || country.isEmpty()) {
            return mapper.createArrayNode();
        }

        try {
            return fetchFromAPI(country);
        } catch (Exception e) {
            return getFallbackStates(country);
        }
    }

    private ArrayNode fetchFromAPI(String country) throws IOException {

        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String requestBody = "{ \"country\": \"" + country + "\" }";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JsonNode root = mapper.readTree(response.toString());
        JsonNode states = root.path("data").path("states");

        return convertToDropdown(states);
    }

    private ArrayNode convertToDropdown(JsonNode states) {

        ArrayNode result = mapper.createArrayNode();

        for (JsonNode state : states) {

            ObjectNode obj = mapper.createObjectNode();
            obj.put("text", state.path("name").asText());
            obj.put("value", state.path("name").asText());

            result.add(obj);
        }

        return result;
    }

    private ArrayNode getFallbackStates(String country) {

        ArrayNode result = mapper.createArrayNode();

        if ("India".equalsIgnoreCase(country)) {
            result.add(create("Andhra Pradesh"));
            result.add(create("Telangana"));
            result.add(create("Karnataka"));
        } else if ("United States".equalsIgnoreCase(country)) {
            result.add(create("Alabama"));
            result.add(create("Texas"));
            result.add(create("California"));
        } else {
            result.add(create("Default State 1"));
            result.add(create("Default State 2"));
        }

        return result;
    }

    private ObjectNode create(String name) {
        ObjectNode obj = mapper.createObjectNode();
        obj.put("text", name);
        obj.put("value", name);
        return obj;
    }
}