package com.bitespeed.sample_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityRequest {
    private String email;
    private String phoneNumber;
}
