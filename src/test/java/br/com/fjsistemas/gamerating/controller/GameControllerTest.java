package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.enuns.Platform;
import br.com.fjsistemas.gamerating.model.Contact;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@SpringBootTest
public class GameControllerTest {

    @Autowired
    GameController controller;

    @Test
    void saveTest(){
        Game game = getGame();

        Game gameAfterSave = controller.save(game);

        Assertions.assertNotNull(gameAfterSave);
        System.out.println("Result: "+ gameAfterSave);
    }

    @Test
    void updateGameTest(){
        Game game = getGame();

        Game before = controller.save(game);
        System.out.println("Dados Salvo: "+ before);
        before.setName("Dong Kong");
        before.setPlatforms(Collections.singleton(Platform.XBOX));

        Game after = controller.save(before);
        System.out.println("Dados atualizados: "+ after);
        Assertions.assertNotEquals(game.getName(), game.getPlatforms(), after.getName());
    }

    @Test
    void loadGameTest(){
        Game save = controller.save(getGame());

        Game load = controller.load(save.getId());

        Assertions.assertNotNull(load);
    }

    public static Game getGame(){
        Game game = new Game(0L,"Mario World", "nintendo.com", LocalDate.now(),
                Collections.singleton(Platform.WII), Collections.emptySet(), new byte[]{1,2,3});
        return game;
    }

    @Test
    void listTest(){
        List<Game> list = new ArrayList<>();
        list.add(getGame());
        list.add(getGame());
        list.add(getGame());
        list.add(getGame());

        List<Game> games = controller.saveBatch(list);

        Assertions.assertFalse(games.isEmpty());
    }



}
