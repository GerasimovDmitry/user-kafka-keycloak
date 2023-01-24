package com.example.userkafkakeycloak.m2m;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/m2m")
public class M2MController {

    private final M2MClient client;

    @GetMapping("/companies/{companyId}")
    public boolean findById(@PathVariable Integer companyId) {
        return client.findById(companyId);
    }

    @GetMapping("/companies/getOne/{companyId}")
    public String getOne(@PathVariable Integer companyId) {
        return client.getOne(companyId);
    }

}