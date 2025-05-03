package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.enuns.Platform;
import br.com.fjsistemas.gamerating.enuns.Role;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController controller;

    public static User getUser(){
        return new User(1L,"reinaldo88","reinaldo", "123456", "reinaldo@gmail.com",Role.ADM, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void saveTest(){
        User user = getUser();

        User userAfterSaved = controller.save(user);

        Assertions.assertNotNull(userAfterSaved);
        System.out.println("Result: "+ userAfterSaved);
    }

    @Test
    void updateUserTest(){
        User user = getUser();

        User before = controller.save(user);
        System.out.println("Dados Salvo: "+ before);
        before.setUsername("porcoAranha");
        before.setName("Homer");
        before.setPassword("234567");
        before.setRole(Role.EDITOR);


        User after = controller.save(before);
        System.out.println("Dados atualizados: "+ after);
        Assertions.assertNotEquals(after.getUsername(), user.getName());
    }

    @Test
    void loadGameTest(){
        User save = controller.save(getUser());

        User load = controller.load(Long.valueOf(getUser().getUsername()));

        Assertions.assertNotNull(load);
    }

    @Test
    void listTest(){
        List<User> list = new ArrayList<>();
        list.add(getUser());
        list.add(getUser());
        list.add(getUser());
        list.add(getUser());

        List<User> users = controller.saveBatch(list);

        Assertions.assertFalse(users.isEmpty());
    }

}
