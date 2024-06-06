package com.bitespeed.sample_project.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bitespeed.sample_project.data.bo.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{

    @Query("SELECT c FROM Contact c WHERE c.email = :email")
    List<Contact> findAllByEmail(@Param("email") String email);

    @Query("SELECT c FROM Contact c WHERE c.phoneNumber = :phoneNumber")
    List<Contact> findAllByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT c FROM Contact c WHERE c.linkedId = :id OR c.id = :id")
    List<Contact> findAllByLinkedIdOrId(@Param("id") Long id);
}
