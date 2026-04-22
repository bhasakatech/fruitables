package com.bhasaka.fruitables.core.service.impl;

import com.bhasaka.fruitables.core.config.DynamicAPIConfig;
import com.bhasaka.fruitables.core.service.ApiService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Designate(ocd = DynamicAPIConfig.class)
@Component(service = ApiService.class)
public class ApiServiceImpl implements ApiService {

    private String apiUrl;
    private boolean status;

    @Activate
    protected  void activated(DynamicAPIConfig config){
        this.apiUrl=config.api();
        this.status=config.status();
    }

    @Override
    public String getApiData() {
        if(!status){
            return "Status is disable";
        }

        StringBuilder builder=new StringBuilder();
        try {
            URL url=new URL(apiUrl);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );

            String line;
            while((line = br.readLine()) != null){
                builder.append(line);
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return "Error fetching API: " + e.getMessage();
        }
        return builder.toString();
    }
}
