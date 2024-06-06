package com.bitespeed.sample_project.data.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitespeed.sample_project.data.bo.Contact;
import com.bitespeed.sample_project.data.repository.ContactRepository;
import com.bitespeed.sample_project.model.IdentityRequest;
import com.bitespeed.sample_project.model.IdentityResponse;
import com.bitespeed.sample_project.model.LinkPrecedence;

@Service
public class IdentityReconsilationService {

    @Autowired
    ContactRepository contactRepository;
    
    public Map<String, IdentityResponse> createIdentity(IdentityRequest request) {
        List<Contact> identityContacts = new ArrayList<>();
        List<Contact> existingContacts = new ArrayList<>();
        Boolean emailMatched = Boolean.FALSE;
        Boolean phoneNumberMatched = Boolean.FALSE;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            List<Contact> emailMatchedContacts = contactRepository.findAllByEmail(request.getEmail());
            if (emailMatchedContacts != null && !emailMatchedContacts.isEmpty()) {
                Long primaryId = getPrimaryIdFromList(emailMatchedContacts);
                if (primaryId != null) {
                    emailMatchedContacts.addAll(contactRepository.findAllByLinkedIdOrId(primaryId));
                }
                emailMatched = Boolean.TRUE;
                existingContacts.addAll(emailMatchedContacts);
            }
        } 
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            List<Contact> phoneMatchedContacts = contactRepository.findAllByPhoneNumber(request.getPhoneNumber());
            if (phoneMatchedContacts != null && !phoneMatchedContacts.isEmpty()) {
                Long primaryId = getPrimaryIdFromList(phoneMatchedContacts);
                if (primaryId != null) {
                    phoneMatchedContacts.addAll(contactRepository.findAllByLinkedIdOrId(primaryId));
                }
                phoneNumberMatched = Boolean.TRUE;
                existingContacts.addAll(phoneMatchedContacts);
            }
        }
        if (emailMatched.equals(Boolean.TRUE) && phoneNumberMatched.equals(Boolean.TRUE) && existingContacts != null && !existingContacts.isEmpty()) {
            existingContacts = updateExistingContacts(existingContacts);
        }
        if (existingContacts == null || existingContacts.isEmpty()) {
            Contact contact = new Contact();
            contact.setEmail(request.getEmail());
            contact.setPhoneNumber(request.getPhoneNumber());
            identityContacts.add(contactRepository.save(contact));
        } else {
            identityContacts.addAll(existingContacts);
            if ((emailMatched.equals(Boolean.FALSE) || phoneNumberMatched.equals(Boolean.FALSE)) && request.getEmail() != null && request.getPhoneNumber() != null) {
                Contact contact = new Contact();
                contact.setEmail(request.getEmail());
                contact.setPhoneNumber(request.getPhoneNumber());
                contact.setLinkPrecedence(LinkPrecedence.secondary);
                contact.setLinkedId(getPrimaryIdFromList(existingContacts));
                identityContacts.add(contactRepository.save(contact));
            }
        }
        return formatIdentityContactResponse(identityContacts);
    }

    private List<Contact> updateExistingContacts(List<Contact> contacts) {
        Map<Long, Date> map = new HashMap<>();
        for(Contact contact: contacts) {
            map.put(contact.getId(), contact.getCreatedAt());
        }
        map = sortByValue(map);
        LinkPrecedence updatedPrecedence = LinkPrecedence.primary;
        Long primaryId = null;
        for (Map.Entry<Long, Date> entry: map.entrySet()) {
            Contact contact = getContactByIdFromList(entry.getKey(), contacts);
            if (primaryId == null && updatedPrecedence.equals(LinkPrecedence.primary)) {
                primaryId = contact.getId();
            }
            contact.setLinkPrecedence(updatedPrecedence);
            if (updatedPrecedence.equals(LinkPrecedence.secondary) && primaryId != null) {
                contact.setLinkedId(primaryId);
            }
            updatedPrecedence = LinkPrecedence.secondary;
        }
        return contactRepository.saveAll(contacts);
    }

    public Map<Long, Date> sortByValue(Map<Long, Date> map) {
        List<Map.Entry<Long, Date>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<Long, Date> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Date> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private Long getPrimaryIdFromList(List<Contact> contacts) {
        Long primaryId = null;
        for (Contact contact: contacts) {
            if (contact.getLinkPrecedence().equals(LinkPrecedence.primary)) {
                primaryId = contact.getId();
            } else {
                primaryId = contact.getLinkedId();
            }
        };
        return primaryId;
    }

    public Contact getContactByIdFromList(Long id, List<Contact> contacts) {
        for (Contact contact: contacts) {
            if (contact.getId().equals(id)) {
                return contact;
            }
        }
        return null;
    } 

    private Map<String, IdentityResponse> formatIdentityContactResponse(List<Contact> contacts) {
        IdentityResponse identityResponse = new IdentityResponse();
        contacts.sort(Comparator.comparing(Contact::getLinkPrecedence)
            .thenComparing(Contact::getCreatedAt));
        contacts.forEach(contact -> {
            if (contact.getLinkPrecedence().equals(LinkPrecedence.primary)) {
                identityResponse.setPrimaryContactId(contact.getId());
            }
            if (contact.getLinkPrecedence().equals(LinkPrecedence.secondary)) {
                identityResponse.getSecondaryContactIds().add(contact.getId());
            }
            if (contact.getEmail() != null && !contact.getEmail().isEmpty()) {
                identityResponse.getEmails().add(contact.getEmail());
            }
            if (contact.getPhoneNumber() != null && !contact.getPhoneNumber().isEmpty()) {
                identityResponse.getPhoneNumbers().add(contact.getPhoneNumber());
            }
        });
        Map<String, IdentityResponse> response = new HashMap<>();
        response.put("contact", identityResponse);
        return response;
    }

}
