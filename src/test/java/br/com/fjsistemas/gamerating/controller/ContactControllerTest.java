package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.enuns.ContactType;
import br.com.fjsistemas.gamerating.model.Contact;
import br.com.fjsistemas.gamerating.model.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ContactControllerTest {

    @Autowired
    ContactController contactController;

    @Test
    void saveTest(){
        Contact contact = getContct();

        Contact saved = contactController.save(contact);

        Assertions.assertNotNull(saved);
        System.out.println("Result:" + saved);
    }

    @Test
    void updateContactTest(){
        Contact cont = getContct();
        Contact before = contactController.save(cont);

        System.out.println("Dados salvo:" + before);

        before.setType(ContactType.PHONE);
        before.setValue("91801164");

        Contact after = contactController.save(before);

        System.out.println("Dados atualizados:" + before);

        Assertions.assertNotNull(cont.getType(), after.getValue());
    }

    @Test
    void load(){
        Contact save = contactController.save(getContct());

        Contact load = contactController.load(save.getId());

        Assertions.assertNotNull(load);
    }

    @Test
    void listTest(){
        List<Contact> list = new ArrayList<>();
        list.add(getContct());
        list.add(getContct());
        list.add(getContct());
        list.add(getContct());

        List<Contact> publishers = contactController.saveBatch(list);

        Assertions.assertFalse(publishers.isEmpty());
    }


    public static Contact getContct(){
        Contact cont = new Contact();
        cont.setId(1L);
        cont.setType(ContactType.EMAIL);
        cont.setValue("reinaldo@gmail.com");

        return cont;
    }

}
