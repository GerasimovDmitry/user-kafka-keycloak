package com.example.userkafkakeycloak.m2m;

import com.example.userkafkakeycloak.entity.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "M2MClient", url = "http://localhost:8090")
public interface M2MClient {

    @GetMapping("/companies/getOne/{companyId}")
    String getOne(@PathVariable Integer companyId);

    @RequestMapping(method = RequestMethod.GET, value="/companies/findById",consumes = "application/json", produces = "application/json")
    Company findById (@RequestParam Integer companyId);

    @RequestMapping(method = RequestMethod.DELETE, value="/companies/remove",consumes = "application/json", produces = "application/json")
    String removeCompany (@RequestParam Integer companyId);
}