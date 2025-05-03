
package br.com.fjsistemas.gamerating.ui.views.home;

import br.com.fjsistemas.gamerating.config.AuthenticatedUser;
import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.controller.ReviewController;
import br.com.fjsistemas.gamerating.model.Game;


import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import br.com.fjsistemas.gamerating.ui.views.game.GameBanner;
import br.com.fjsistemas.gamerating.ui.views.game.GameCard;
import br.com.fjsistemas.gamerating.ui.views.game.GameCarousel;

import br.com.fjsistemas.gamerating.ui.views.game.TopGames;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "home", layout = MainLayout.class)
@PageTitle("Home | GAMERATING")
@PermitAll
public class HomeView extends VerticalLayout {
    private final GameController controller;
    private final ReviewController reviewController;

    public HomeView(@Autowired GameController controller, ReviewController reviewController) throws ParseException {
        this.controller = controller;
        this.reviewController = reviewController;


        this.addClassName("home-view");
        this.setSpacing(false);
        this.setPadding(true);

        // Verifica se o usuário está logado
        if (UI.getCurrent().getSession().getAttribute("user") != null) {
            this.getElement().getThemeList().add("dark");
        } else {
            this.getElement().getThemeList().remove("dark");
        }

        GameCarousel featuredCarousel = this.createFeaturedCarousel();
        VerticalLayout gamesSection = this.createTopGamesSection();
        gamesSection.addClassName("games-section");
        Div themeToggle = this.createThemeToggleButton();
        this.add(new Component[]{featuredCarousel,createGamesSection(), gamesSection, themeToggle});
    }

    /**
     * Cria um carrossel para jogos em destaque.
     *
     * @return Um GameCarousel contendo os jogos em destaque.
     */
    private GameCarousel createFeaturedCarousel() {
        GameCarousel carousel = new GameCarousel();
        List<Game> games = this.controller.list();
        if (games != null && !games.isEmpty()) {
            for(int i = 0; i < Math.min(8, games.size()); ++i) {
                Game game = (Game)games.get(i);
                GameBanner banner = new GameBanner(game.getImage(), game.getName(),
                        "Post: " + game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                carousel.addBanner(banner);
            }
        } else {
            GameBanner placeholder = new GameBanner((byte[])null, "Game não encontrado",
                    "Adione para poder ver aqui");
            carousel.addBanner(placeholder);
        }

        carousel.setAutoPlayDelay(5000);
        carousel.setAutoPlay(true);
        return carousel;
    }

    /**
     * Cria section com todos os jogos.
     *
     * @return VerticalLayout contendo a section com todos jogos.
     * @throws ParseException Se houver um erro analisando os dados do jogo.
     */
    private VerticalLayout createGamesSection() throws ParseException {

        // Cria um título para a seção
        Div iconAndTitle = new Div();
        iconAndTitle.getStyle()
                .set("display", "flex")
                .set("width", "100%")
                .set("justify-content", "start")
                .set("align-items", "center")
                .set("margin-top", "1rem")
                .set("gap", "var(--spacing-m)");

        Icon icon = new Icon(VaadinIcon.GAMEPAD);
        icon.setSize("var(--lumo-icon-size-l)");
        icon.getStyle()
                .set("margin-top", "0.3rem")
                .set("align-self", "center")
                .set("align-items", "center");

        H2 sectionTitle = new H2("Todos Jogos");
        sectionTitle.getStyle()
                .set("display", "flex")
                .set("color", "#293648")
                .set("justify-content", "center")
                .set("align-items", "center");

        iconAndTitle.add(sectionTitle, icon);

        // Cria um separador
        Div separator = new Div();
        separator.setHeight("1px");
        separator.setWidthFull();
        separator.getStyle()
                .set("background-color", "var(--lumo-contrast-10pct)")
                .set("margin", "var(--lumo-space-m) 0");

        FlexLayout cardContainer = new FlexLayout();

        cardContainer.addClassName("card-container");
        cardContainer.setWidthFull();
        List<Game> games = this.controller.list();

        if (games != null && !games.isEmpty()) {
            for(Game game : games) {

                GameCard card = new GameCard(game.getImage(), game.getName(), game.getReleaseDate(), game.getWebsite(), game.getPlatforms());
                cardContainer.add(new Component[]{card});
            }
        } else {
            Div noGames = new Div();
            noGames.setText("Jogos não encontrados!");
            noGames.getStyle()
                    .set("padding", "var(--spacing-l)")
                    .set("color", "var(--text-secondary)")
                    .set("text-align", "center")
                    .set("width", "100%");
            cardContainer.add(new Component[]{noGames});
        }


        VerticalLayout section = new VerticalLayout(new Component[]{iconAndTitle, separator, cardContainer});
        section.setPadding(false);
        section.setSpacing(true);
        return section;
    }

    /**
     * Cria section com os top 10 jogos.
     *
     * @return  VerticalLayout.
     */
    private TopGames createTopGamesSection() throws ParseException {

        // Cria um título para a seção
        Div iconAndTitle = new Div();
        iconAndTitle.getStyle()
                .set("display", "flex")
                .set("width", "100%")
                .set("justify-content", "start")
                .set("align-items", "center")
                .set("margin-top", "1rem")
                .set("gap", "var(--spacing-m)");
        Icon icon = new Icon(VaadinIcon.STAR);
        icon.setSize("var(--lumo-icon-size-l)");
        icon.getStyle()
                .set("margin-top", "0.3rem")
                .set("align-self", "center")
                .set("align-items", "center");
        H2 sectionTitle = new H2("Top 10 Jogos");
        sectionTitle.getStyle()
                .set("display", "flex")
                .set("color", "#293648")
                .set("justify-content", "center")
                .set("align-items", "center");
        iconAndTitle.add(sectionTitle, icon);
        // Cria um separador
        Div separator = new Div();
        separator.setHeight("1px");
        separator.setWidthFull();
        separator.getStyle()
                .set("background-color", "var(--lumo-contrast-10pct)")
                .set("margin", "var(--lumo-space-m) 0");
        FlexLayout cardContainer = new FlexLayout();
        cardContainer.addClassName("card-container");
        cardContainer.setWidthFull();
        List<Game> games = this.controller.findTop10RatedGames();
        if (games != null && !games.isEmpty()) {
            for(Game game : games) {
                GameCard card = new GameCard(game.getImage(), game.getName(), game.getReleaseDate(), game.getWebsite(), game.getPlatforms());
                cardContainer.add(new Component[]{card});
            }
        } else {
            Div noGames = new Div();
            noGames.setText("Top 10 Jogos não encontrados!");
            noGames.getStyle()
                    .set("padding", "var(--spacing-l)")
                    .set("color", "var(--text-secondary)")
                    .set("text-align", "center")
                    .set("width", "100%");
            cardContainer.add(new Component[]{noGames});
        }
        VerticalLayout section = new VerticalLayout(new Component[]{iconAndTitle, separator, cardContainer});
        section.setPadding(false);
        section.setSpacing(true);
        section.setWidthFull();
        section.setHeight("100%");
        section.getStyle()
                .set("background-color", "var(--lumo-contrast-5pct)")
                .set("border-radius", "var(--lumo-border-radius-m)")
                .set("padding", "var(--lumo-space-l)");
        section.getElement().getStyle()
                .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
        section.getElement().getStyle()
                .set("transition", "box-shadow 0.3s ease-in-out");
        section.getElement().getStyle()
                .set("cursor", "pointer");
        section.getElement().addEventListener("mouseover", event -> {
            section.getElement().getStyle()
                    .set("box-shadow", "0 4px 10px rgba(0, 0, 0, 0.2)");
        });
        section.getElement().addEventListener("mouseout", event -> {
            section.getElement().getStyle()
                    .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)");
        });
        section.getElement().addEventListener("click", event -> {
            UI.getCurrent().navigate("game");
        });
        section.getElement().getStyle()
                .set("cursor", "pointer");


        return new TopGames(this.controller.findTop10RatedGames(), this.reviewController.list(), section);
    }


    /**
     * Cria um botão para alternar o tema entre o modo claro e escuro.
     *
     * @return Div com o botão de alternância de tema.
     */
    private Div createThemeToggleButton() {
        Div themeToggle = new Div();
        themeToggle.addClassName("theme-toggle");
        Icon icon = new Icon(VaadinIcon.MOON);
        themeToggle.add(new Component[]{icon});
        themeToggle.getElement().addEventListener("click", (e) -> {
            ThemeList themeList = ((UI)this.getUI().get()).getElement().getThemeList();
            if (themeList.contains("dark")) {
                themeList.remove("dark");
                icon.setIcon(VaadinIcon.MOON);
            } else {
                themeList.add("dark");
                icon.setIcon(VaadinIcon.SUN_O);
            }

        });
        return themeToggle;
    }
}
