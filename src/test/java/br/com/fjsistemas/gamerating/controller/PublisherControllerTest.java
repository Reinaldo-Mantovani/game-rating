package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.Publisher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PublisherControllerTest {

    @Autowired
    PublisherController controller;

    @Test
    void saveTest(){
        Publisher publisher = getPublisher();

        Publisher publisherAfterSave = controller.save(publisher);

        Assertions.assertNotNull(publisherAfterSave);
        System.out.println("Result: " + publisherAfterSave);
    }

    @Test
    void updateTest(){
        Publisher publisher = getPublisher();

        Publisher before = controller.save(publisher);

        before.setName("Capcom");
        before.setWebsite("https://capcom.com");

        Publisher after = controller.save(before);

        Assertions.assertNotEquals(publisher.getName(), after.getName());

    }

    public static Publisher getPublisher(){
        Publisher publisher = new Publisher(0L , "Rockstar","https://rockstar.com");
        return publisher;
    }

    @Test
    void loadTest(){
        Publisher save = controller.save(getPublisher());

        Publisher load = controller.load(save.getId());

        Assertions.assertNotNull(load);
    }

    @Test
    void listTest(){
        List<Publisher> list = new ArrayList<>();
        list.add(getPublisher());
        list.add(getPublisher());
        list.add(getPublisher());
        list.add(getPublisher());

       List<Publisher> publishers = controller.saveBatch(list);

        Assertions.assertFalse(publishers.isEmpty());
    }


}
