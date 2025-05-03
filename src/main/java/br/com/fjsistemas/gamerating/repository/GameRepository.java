package br.com.fjsistemas.gamerating.repository;

import br.com.fjsistemas.gamerating.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.platforms")
    List<Game> findAllWithPlatforms();

    //findTop10RatedGames()
    @Query(value = "SELECT g.* FROM tb_game g JOIN ( " +
            "SELECT game_id, AVG(rating) as avg_rating FROM tb_review GROUP BY game_id ORDER BY avg_rating DESC LIMIT 10 " +
            ") top_games ON g.id = top_games.game_id ORDER BY top_games.avg_rating DESC",
            nativeQuery = true)
    List<Game> findTop10RatedGames();





}

