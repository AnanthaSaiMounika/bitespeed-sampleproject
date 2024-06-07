package com.bitespeed.sample_project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitespeed.sample_project.data.service.IdentityReconsilationService;
import com.bitespeed.sample_project.model.IdentityRequest;
import com.bitespeed.sample_project.model.IdentityResponse;

@RestController
@RequestMapping("")
public class IdentityReconcilationController {

    @Autowired
    IdentityReconsilationService identityReconsilationService;
    
    @PostMapping("/identity")
    public Map<String, IdentityResponse> createIdentity(@RequestBody IdentityRequest request) {
        return identityReconsilationService.createIdentity(request);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
