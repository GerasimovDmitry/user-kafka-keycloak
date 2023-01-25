package com.example.userkafkakeycloak.m2m;

import com.example.userkafkakeycloak.entity.Company;
import com.example.userkafkakeycloak.entity.Userkk;
import com.example.userkafkakeycloak.repository.UserkkRepository;
import com.example.userkafkakeycloak.service.ConfigurationService;
import com.example.userkafkakeycloak.service.KeycloakService;
import com.example.userkafkakeycloak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class M2MController {

    private final M2MClient client;
    KeycloakService keycloakService;

    ConfigurationService configurationService;

    KeycloakRestTemplate restTemplate;
    @Autowired
    UserService userService;

    UserkkRepository repository;
/*    public M2MController(ConfigurationService configurationService,
                         KeycloakService keycloakService,
                         KeycloakRestTemplate restTemplate,
                         UserService userService,
                         UserkkRepository repository) {
        this.configurationService = configurationService;
        this.keycloakService = keycloakService;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.repository = repository;
    }*/
    @GetMapping("/companies/findById")
    public Company findById(@RequestParam Integer companyId) {
        return this.client.findById(companyId);
    }

    @DeleteMapping("/companies/remove")
    public String removeCompany(@RequestParam Integer companyId) {
        return this.client.removeCompany(companyId);
    }

    @GetMapping("/companies/getOne/{companyId}")
    public String getOne(@PathVariable Integer companyId) {
        return client.getOne(companyId);
    }

    @GetMapping("/m2m/users/findById")
    public Userkk usersFindById(@RequestParam Integer userId) {
        System.out.println("m2m userfind");
        System.out.println(userService.findById(userId));
        return userService.findById(userId);
    }

    @GetMapping("/m2m/users")
    public List<Userkk> getAllUsers() {
        System.out.println("m2m getallusers");
        return userService.getAll();
    }
}