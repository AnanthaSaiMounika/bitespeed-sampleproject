package com.bitespeed.sample_project.data.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bitespeed.sample_project.model.LinkPrecedence;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String phoneNumber;
    private Long linkedId;
    private LinkPrecedence linkPrecedence = LinkPrecedence.primary;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private Date deletedAt;
}
