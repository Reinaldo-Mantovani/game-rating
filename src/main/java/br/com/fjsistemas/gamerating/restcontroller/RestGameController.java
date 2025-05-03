package br.com.fjsistemas.gamerating.restcontroller;

import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/game")
public class RestGameController {

    GameController controller;

    public RestGameController(GameController controller) {
        this.controller = controller;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAll(){
        List<Game> list = controller.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getById(@PathVariable Long id) {
        Game existGame = controller.load(id);
        if (existGame == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(existGame);
    }

    @PostMapping
    public ResponseEntity<Game> save(@RequestBody Game game){
            Game newGame = controller.save(game);
            return new ResponseEntity<>(newGame, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game game){
        Game existGame = controller.load(id);
        if(existGame == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existGame.setName(game.getName());
        existGame.setWebsite(game.getWebsite());
        existGame.setPlatforms(game.getPlatforms());
        existGame.setImage(game.getImage());
        existGame.setReview(game.getReview());

        Game updatedGame = controller.save(existGame);

        return new ResponseEntity<>(updatedGame,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Game gameExist = controller.load(id);
        if (gameExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Game com id: " + id + " não encontrado!");
        }

        controller.delete(gameExist);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Game com id: " + id + " foi deletado com sucesso.");
    }
}

