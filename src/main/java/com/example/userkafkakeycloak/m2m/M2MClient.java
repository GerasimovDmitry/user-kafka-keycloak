package com.example.userkafkakeycloak.m2m;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Companies", url = "http://localhost:8099")
public interface M2MClient {

    @GetMapping("/companies/{companyId}")
    boolean findById (@PathVariable Integer companyId);

    @GetMapping("/companies/getOne/{companyId}")
    String getOne(@PathVariable Integer companyId);
}