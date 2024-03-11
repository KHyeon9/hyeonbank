package com.springsecurity.hyeonbank.repository;

import com.springsecurity.hyeonbank.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {

}
