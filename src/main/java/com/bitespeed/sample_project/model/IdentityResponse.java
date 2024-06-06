package com.bitespeed.sample_project.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentityResponse {
    
    private Long primaryContactId;
    private Set<String> emails = new HashSet<>();;
    private Set<String> phoneNumbers = new HashSet<>();
    private Set<Long> secondaryContactIds = new HashSet<>();
}
