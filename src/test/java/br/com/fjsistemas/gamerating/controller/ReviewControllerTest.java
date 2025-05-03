package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.enuns.Platform;

import br.com.fjsistemas.gamerating.enuns.Role;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Review;

import br.com.fjsistemas.gamerating.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SpringBootTest
public class ReviewControllerTest {

    @Autowired
    ReviewController controller;



    @Test
    void saveTest() {
        Review review = getReview();
        Review reviewAfterSave = controller.save(review);

        Assertions.assertNotNull(reviewAfterSave);
    }

    @Test
    void updateTest() {
        Review review = getReview();

        Review before = controller.save(review);
        before.setTitle("New Review");

        Review after = controller.save(before);

        System.out.println(before.getTitle() + " " + after.getTitle());
        Assertions.assertNotEquals(before.getTitle(), after.getTitle());
    }

    @Test
    void loadTest() {
        Review save = controller.save(getReview());

        Review load = controller.load(save.getId());

        Assertions.assertNotNull(load);
    }

    @Test
    void deleteTest() {
        Review save = controller.save(getReview());

        controller.delete(save);

        Review deletedReview = controller.load(save.getId());

        Assertions.assertNull(deletedReview);
    }

    @Test
    void deletedByIdTest () {

        Review review = getReview();

        Review saved = controller.save(review);

        controller.delete(saved);

        Review reviewDelete = controller.load(saved.getId());

        Assertions.assertThrows(RuntimeException.class, () -> controller.load(reviewDelete.getId()));
    }


    private static Review getReview() {
        Game game = new Game(1L,"Mario World", "nintendo.com", LocalDate.now(), Collections.singleton(Platform.WII), Collections.emptySet(), new byte[]{1,2,3,4});
        User user = new User(1L,"reinaldo88", "reinaldo","1234","reinaldo@gmail.com", Role.ADM,LocalDateTime.now(), LocalDateTime.now());
        Review newReview = new Review(1L, game, "Excelente jogo","Melhor jogo de todos os tempos.",
                4, user, Platform.WII, LocalDateTime.now());

        return newReview;
    }

    @Test
    void listTest() {
        List<Review> list = new ArrayList<>();
        list.add(getReview());
        list.add(getReview());
        list.add(getReview());

        List<Review> reviews;
        reviews = controller.saveBatch(list);
        Assertions.assertFalse(reviews.isEmpty());
    }



}
