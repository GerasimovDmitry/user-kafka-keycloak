package com.example.userkafkakeycloak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompany {
    Integer user_id;
    Integer company_id;
    String keycloak_id;
    Boolean isOwner;
    String companyName;
    String name;
    String username;
}
