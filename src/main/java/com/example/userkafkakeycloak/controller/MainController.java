package com.example.userkafkakeycloak.controller;

import com.example.userkafkakeycloak.entity.Company;
import com.example.userkafkakeycloak.entity.RoleKey;
import com.example.userkafkakeycloak.entity.UserCompany;
import com.example.userkafkakeycloak.entity.Userkk;
import com.example.userkafkakeycloak.m2m.M2MController;
import com.example.userkafkakeycloak.repository.UserkkRepository;
import com.example.userkafkakeycloak.service.ConfigurationService;
import com.example.userkafkakeycloak.service.KeycloakService;
import com.example.userkafkakeycloak.service.UserService;
import com.google.gson.Gson;
import org.apache.kafka.common.errors.ResourceNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api")
public class MainController {
    KeycloakService keycloakService;

    ConfigurationService configurationService;

    KeycloakRestTemplate restTemplate;

    UserService userService;

    UserkkRepository repository;

    M2MController m2mController;

    public MainController(ConfigurationService configurationService,
                          KeycloakService keycloakService,
                          KeycloakRestTemplate restTemplate,
                          UserService userService,
                          UserkkRepository repository,
                          M2MController m2mController) {
        this.configurationService = configurationService;
        this.keycloakService = keycloakService;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.repository = repository;
        this.m2mController = m2mController;
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

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Userkk> usersAll() {
        return this.userService.getAll();
    }

    @GetMapping(path = "/users/findById")
    @PreAuthorize("hasRole('USER')")
    public Userkk findById(@RequestParam("userId") Integer userId) throws ResourceNotFoundException {
        Userkk user = userService.findById(userId);
        System.out.println(user);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        return user;
    }

    @GetMapping(path = "/users/company/findById")
    @PreAuthorize("hasRole('USER')")
    public UserCompany findUserCompanyById(@RequestParam("userId") Integer userId) throws ResourceNotFoundException {
        Userkk user = userService.findById(userId);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }
        Company company = m2mController.findById(user.getCompany_id());
        UserCompany userCompany = new UserCompany();

        userCompany.setCompany_id(company.getId());
        userCompany.setUser_id(user.getId());
        userCompany.setKeycloak_id(user.getKeycloak_id());
        userCompany.setIsOwner(true);
        userCompany.setUsername(user.getUserName());
        userCompany.setName(user.getName());
        userCompany.setCompanyName(company.getName());

        return userCompany;
    }

    @DeleteMapping("/company/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public String removeCompany(@RequestParam("companyId") Integer companyId) {
        Company company = m2mController.findById(companyId);
        if (company == null) {
            throw new ResponseStatusException(NOT_FOUND, "Unable to find resource");
        }

        String response = m2mController.removeCompany(companyId);
        return response + " was removed";
    }
}