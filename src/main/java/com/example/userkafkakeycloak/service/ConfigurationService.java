package com.example.userkafkakeycloak.service;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    public String getAuthServerUrl() {
        return "http://localhost:8484/auth";
    }

    public String getRealm() {
        return "my_realm";
    }

    public String getClientId() {
        return "my_client";
    }

    public String getClientSecret() {
        return "Ph1EFo8Gm6Tl8ZwiPCnQSpqtSCXohMVh";
    }

    public String getMasterRealm() {
        return "master";
    }

    public String getMasterClientId() {
        return "ba84693d-75dd-4053-b085-0dc1c40425ae";
    }

    public String getMasterUsername() {
        return "admin";
    }

    public String getMasterPassword() {
        return "admin";
    }

}
