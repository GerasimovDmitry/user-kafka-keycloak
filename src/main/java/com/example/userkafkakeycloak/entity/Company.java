package com.example.userkafkakeycloak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class Company {
    private Integer id;

    private String name;

    private Integer owner_id;

    public Company() {

    }
}