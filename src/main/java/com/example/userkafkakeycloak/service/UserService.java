package com.example.userkafkakeycloak.service;

import com.example.userkafkakeycloak.repository.UserkkRepository;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {
    ConfigurationService configurationService;
    UserkkRepository userkkRepository;
    @Autowired
    public UserService(UserkkRepository repository, ConfigurationService configurationService) {
        this.userkkRepository = repository;
        this.configurationService = configurationService;
    }

    public void addRealmRoleToUser(String userId, String userName, String role_name, Keycloak keycloak){
        System.out.println(keycloak.realms());
        ClientsResource clientsResource = keycloak.realm("my_realm").clients();
        for (RealmRepresentation realm : keycloak.realms().findAll()) {
            System.out.println(realm.getRealm());

        }
        System.out.println("realm  " +configurationService.getRealm());
        System.out.println(keycloak.realm(configurationService.getRealm())
                .clients().findAll());
        System.out.println(keycloak
                .realm(configurationService.getRealm())
                .clients()
                .findByClientId(configurationService.getClientId()));
/*        String client_id = keycloak
                .realm(configurationService.getRealm())
                .clients()
                .findByClientId(configurationService.getClientId())
                .get(0)
                .getId();*/
        String client_id = clientsResource.findAll().get(0).getId();
        UserResource user = keycloak
                .realm(configurationService.getRealm())
                .users()
                .get(userId);
        List<RoleRepresentation> roleToAdd = new LinkedList<>();
        roleToAdd.add(keycloak
                .realm(configurationService.getRealm())
                .clients()
                .get(client_id)
                .roles()
                .get(role_name)
                .toRepresentation()
        );
        user.roles().clientLevel(client_id).add(roleToAdd);
    }
}
