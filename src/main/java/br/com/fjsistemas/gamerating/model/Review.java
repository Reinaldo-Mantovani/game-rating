package br.com.fjsistemas.gamerating.model;

import br.com.fjsistemas.gamerating.enuns.Platform;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    @NotNull(message = "Insira um game válido!")
    private Game game;

    @Column(length = 50)
    @NotBlank(message = "Insira um título válido!")
    private String title;

    @Column(length = 1000)
    @NotBlank(message = "Insira um conteúdo válido")
    private String content;

    @Min(value = 1, message = "Insira um valor de 1 a 5")
    @Max(value = 5, message = "Insira um valor de 1 a 5")
    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column
    private Platform platform;


    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm")
    @CreationTimestamp
    private LocalDateTime date;

    public Review(){}

    public Review(Long id, Game game, String title, String content, int rating, User user, Platform platform, LocalDateTime date) {
        this.id = id;
        this.game = game;
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.platform = platform;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public int getRating() {
        return rating = 1;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUser (User user) {
        this.user = user;
    }

    public User getUser () {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", game=" + game +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", user=" + user +
                ", platform=" + platform +
                ", date=" + date +
                '}';
    }
}
