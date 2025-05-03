package br.com.fjsistemas.gamerating.model;

import br.com.fjsistemas.gamerating.enuns.Platform;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Insira um nome valido!")
    @Column(length = 50)
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy kk:mm")
    @CreatedDate
    private LocalDate releaseDate;

    @NotNull(message = "Coloque um website valido!")
    @Column(length = 100)
    private String website;

    @ElementCollection(targetClass = Platform.class, fetch = FetchType.EAGER)
    @JoinTable(name = "game_platform", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "platform")
    private Set<Platform> platforms;

    @JsonIgnore
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Review> review;

    @NotNull
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    private boolean favorite;


    public Game() {
    }


    public Game(Long id, String name, LocalDate releaseDate, String website, Set<Platform> platforms, Set<Review> review, @NotNull byte[] image) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.website = website;
        this.platforms = platforms;
        this.review = review;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = LocalDate.now();
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public Set<Review> getReview() {
        return review;
    }

    public void setReview(Set<Review> review) {
        this.review = review;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return  id + name + releaseDate + website + platforms +
                (image != null ? image.length : 0) + " bytes";
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }
}