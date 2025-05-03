package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.Favorites;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Publisher;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.repository.GameRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameController extends GenericController<Game, Long, GameRepository> {

    public GameController(GameRepository gameRepository) {
       super(gameRepository);
    }

    public List<Game> list() {
        return repository.findAllWithPlatforms();
    }

    public List<Game> findTop10RatedGames() {
        return repository.findTop10RatedGames();
    }


    public void removeFromFavorites(Game game) {
        game.setFavorite(false);
        repository.save(game);
    }

    public void addToFavorites(User user, Game game) {
        if (user == null || game == null || game.getId() == null) {
            throw new IllegalArgumentException("Usuário ou Jogo inválido");
        }

        // lógica para adicionar ou remover dos favoritos
        if (game.isFavorite()) {
            game.setFavorite(false);
        } else {
            game.setFavorite(true);
        }
    }

    public User getCurrentUser() {
        // Obtém o usuário atual a partir do contexto de segurança
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new IllegalStateException("Usuário não autenticado");
        }

    }


}
