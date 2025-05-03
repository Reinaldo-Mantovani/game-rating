package br.com.fjsistemas.gamerating.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "tb_favorites")
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "tb_favorites_games", joinColumns = @JoinColumn(name = "favorites_id"))
    @MapKeyJoinColumn(name = "game_id")
    @Column(name = "favorite_status")
    private Map<Game, Boolean> games = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Favorites() {
    }

    public Favorites(Long id, Map<Game, Boolean> games, User user) {
        this.id = id;
        this.games = games;
        this.user = user;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Game, Boolean> getGames() {
        return games;
    }

    public void setGames(Map<Game, Boolean> games) {
        this.games = games;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addGame(Game game) {
        games.put(game, true);
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public boolean isGameFavorite(Game game) {
        return games.getOrDefault(game, false);
    }

    public Set<Game> getFavoriteGames() {
        Set<Game> favoriteGames = new HashSet<>();
        for (Map.Entry<Game, Boolean> entry : games.entrySet()) {
            if (entry.getValue()) {
                favoriteGames.add(entry.getKey());
            }
        }
        return favoriteGames;


    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Favorites favorites = (Favorites) o;
        return Objects.equals(id, favorites.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "id=" + id +
                ", games=" + games +
                ", user=" + user +
                '}';
    }
}
