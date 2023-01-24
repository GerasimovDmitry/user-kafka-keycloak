package com.example.userkafkakeycloak.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "userkk")
public class Userkk {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_id")
    private String keycloak_id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "name")
    private String name;

    @Column(name = "company_id")
    private Integer company_id;

    @Column(name = "isEnabled")
    private Boolean isEnabled;

    public Userkk() {

    }
}