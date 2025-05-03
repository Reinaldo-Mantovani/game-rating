package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.Contact;
import br.com.fjsistemas.gamerating.repository.ContactRepository;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class ContactController extends GenericController<Contact, Long, ContactRepository> {

    public ContactController(ContactRepository contactRepository) {
        super(contactRepository);
    }

}
