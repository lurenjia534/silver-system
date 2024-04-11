package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class TokenService {

    private final RestTemplate restTemplate;

    @Autowired
    public TokenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String postRequest(
            String tenantId,
            String clientId,
            String clientSecret,
            String grantType,
            String scope
    ) {
        String url = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";
        // set Request Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 创建请求体
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
        map.add("scope", scope);

        // 创建httpEntity对象
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map, headers);
        // 发送post请求
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    public String getTokenAll(
            String tenantId,
            String clientId,
            String clientSecret,
            String grantType,
            String scope
    ) throws JsonProcessingException {
        String response = postRequest(tenantId, clientId, clientSecret, grantType, scope);
        if (response != null) {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(response, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } else {
            return null;
        }
    }

    public String getTokenAccessToken(
            String tenantId,
            String clientId,
            String clientSecret,
            String grantType,
            String scope
    ) throws JsonProcessingException {
        String response = postRequest(tenantId, clientId, clientSecret, grantType, scope);
        if (
                response != null
        ) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> result = mapper.readValue(response, new TypeReference<>() {
            });
            return result.get("access_token");
        } else {
            return null;
        }
    }

    public String listAllFiles(String accessToken,String userId){
        String url = "https://graph.microsoft.com/v1.0/users/"+userId+"/drive/root/children";
        return getString(accessToken, url);
    }

    private String getString(String accessToken, String url) {
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>("parameters",headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            System.out.println("Response:"+response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error while making request:"+e.getMessage());
            System.out.println("Response body:"+e.getResponseBodyAsString());
            return null;
        }
    }

    public String getDriveQuota(String accessToken,String userId)throws JsonProcessingException{
        String url = "https://graph.microsoft.com/v1.0/users/"+userId+"/drive";
        // set headers
        return getString(accessToken, url);
    }

    public String getUserProfile(String accessToken,String userId){
        String url = "https://graph.microsoft.com/v1.0/users/"+userId;
        return getString(accessToken, url);
    }

}

