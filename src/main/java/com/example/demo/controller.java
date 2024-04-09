package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class controller {
    @Autowired

    private TokenService tokenService;

    @GetMapping(value = "/token/all",produces = "application/json")
    public String getToken_all(
            @RequestParam String tenantId,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam String grantType,
            @RequestParam String scope) throws JsonProcessingException {

        return tokenService.getTokenAll(tenantId, clientId, clientSecret, grantType, scope);
    }
    @GetMapping(value = "/token/access",produces = "application/json")
    public String getTokenAccessToken(
            @RequestParam String tenantId,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam String grantType,
            @RequestParam String scope) throws JsonProcessingException {

        return tokenService.getTokenAccessToken(tenantId, clientId, clientSecret, grantType, scope);
    }

    @GetMapping(value = "/files" ,produces = "application/json")
    public ResponseEntity<String> listAllFiles(
            @RequestParam String tenantId,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam String grantType,
            @RequestParam String scope,
            @RequestParam String userId
    ) throws JsonProcessingException {

        String accessToken = tokenService.getTokenAccessToken(tenantId, clientId, clientSecret, grantType, scope);
        String fileJson = tokenService.listAllFiles(accessToken, userId);
        return ResponseEntity.ok(fileJson);
    }

    @GetMapping(value = "/drive/quota", produces = "application/json")
    public ResponseEntity<String> getDriveQuota(
            @RequestParam String tenantId,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam String grantType,
            @RequestParam String scope,
            @RequestParam String userId
    ) throws JsonProcessingException {

        String accessToken = tokenService.getTokenAccessToken(tenantId, clientId, clientSecret, grantType, scope);
        String quotaJson = tokenService.getDriveQuota(accessToken, userId);
        return ResponseEntity.ok(quotaJson);
    }

    @GetMapping(value = "/user/profile",produces = "application/json")
    public ResponseEntity<String> getUserProfile(
            @RequestParam String tenantId,
            @RequestParam String clientId,
            @RequestParam String clientSecret,
            @RequestParam String grantType,
            @RequestParam String scope,
            @RequestParam String userId
    )throws JsonProcessingException {
        String accessToken = tokenService.getTokenAccessToken(tenantId, clientId, clientSecret, grantType, scope);
        String profileJson = tokenService.getUserProfile(accessToken, userId);
        return ResponseEntity.ok(profileJson);
    }

}

