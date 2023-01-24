package com.example.userkafkakeycloak.controller;

import com.example.userkafkakeycloak.entity.RoleKey;
import com.example.userkafkakeycloak.repository.UserkkRepository;
import com.example.userkafkakeycloak.service.ConfigurationService;
import com.example.userkafkakeycloak.service.KeycloakService;
import com.example.userkafkakeycloak.service.UserService;
import com.google.gson.Gson;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class MainController {
    KeycloakService keycloakService;

    ConfigurationService configurationService;

    KeycloakRestTemplate restTemplate;

    UserService userService;

    UserkkRepository repository;

    public MainController(ConfigurationService configurationService, KeycloakService keycloakService, KeycloakRestTemplate restTemplate, UserService userService, UserkkRepository repository) {
        this.configurationService = configurationService;
        this.keycloakService = keycloakService;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.repository = repository;
    }
    @GetMapping("/anonymous")
    public String getAnonymousInfo() {
        return "Anonymous";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String getUserInfo() {
        return "user info";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminInfo() {
        return "admin info";
    }

    @GetMapping("/service")
    @PreAuthorize("hasRole('SERVICE')")
    public String getServiceInfo() {
        return "service info";
    }

    @GetMapping("/me")
    public Object getMe() {
        System.out.println("get info");
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return authentication.getName();
    }

    @GetMapping(path = "/find")
    @PreAuthorize("hasRole('ADMIN')")
    public UserRepresentation findUserById(@RequestParam("userId") String userId) {
        System.out.println("find api");
        Keycloak keycloak = keycloakService.getToken();
        RealmResource realmResource = keycloak.realm(configurationService.getRealm());
        UsersResource usersResource = realmResource.users();
        UserRepresentation user = usersResource.get(userId).toRepresentation();
        return user;
    }

/*
    @GetMapping(path = "/findAllUsers/{litter}")
    @PreAuthorize("hasRole('ADMIN')")
    public Stream<UserRepresentation> findAllUsers(@PathVariable("litter") String litter) {
        Keycloak keycloak = keycloakService.getToken();
        RealmResource realmResource = keycloak.realm(configurationService.getRealm());
        List<UserRepresentation> list = realmResource.users().list();
        return list.stream().filter(x -> x.getUsername().startsWith(litter));
    }
*/

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List users() {
        return restTemplate.getForEntity(URI.create(configurationService.getAuthServerUrl() + "/admin/realms/my_realm/users"), List.class)
                .getBody();
    }

    @PostMapping("/addAdminRole")
    @PreAuthorize("hasRole('ADMIN')")
    public String addRole(@RequestParam("userId") String userId) {
        List<RoleKey> roleKeys = new ArrayList<>();
        roleKeys.add(new RoleKey("fe69469e-cb36-4db3-a12b-6894efaa8655", "ADMIN"));
        this.restTemplate.postForEntity(URI.create(configurationService.getAuthServerUrl()
                + "/admin/realms/my_realm/users/"
                + userId + "/role-mappings/realm"),
                roleKeys,List.class);
        return "Role was added";
    }

    @DeleteMapping("/removeAdminRole")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeRole(@RequestParam("userId") String userId) {
        List<RoleKey> roleKeys = new ArrayList<>();
        roleKeys.add(new RoleKey("fe69469e-cb36-4db3-a12b-6894efaa8655", "ADMIN"));

        Gson gson = new Gson();
        try {
            String jsonPayload  = gson.toJson(roleKeys);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.set("Content-Type",
                    "application/json");
            HttpEntity<String> entity = new HttpEntity<String>(jsonPayload.toString(), requestHeaders);
            this.restTemplate.exchange(URI.create(configurationService.getAuthServerUrl()
                    + "/admin/realms/my_realm/users/"
                    + userId + "/role-mappings/realm"), HttpMethod.DELETE, entity, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return "Role was removed";
    }

}