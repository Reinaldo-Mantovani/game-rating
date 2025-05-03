package br.com.fjsistemas.gamerating.ui.views.game;

import br.com.fjsistemas.gamerating.config.AuthenticatedUser;
import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.enuns.Platform;
import br.com.fjsistemas.gamerating.model.Favorites;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Review;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;


@Route(value = "games-details/", layout = MainLayout.class)
@CssImport("/../frontend/themes/default/game-details.css")
@RolesAllowed({"EDITOR", "ADM"})
public class GameDetailsView extends VerticalLayout implements HasUrlParameter<String> {

    private String gameTitle;
    private byte[] imageUrl;
    private final Set<Favorites> favorites;

    private final GameController controller;
    private Game selectedGame;

    @Autowired
    private AuthenticatedUser authenticatedUser;

    public GameDetailsView(GameController controller, Set<Favorites> favorites) {
        this.favorites = favorites;
        this.controller = controller;
        addClassName("details-container");

        setSpacing(false);
        setPadding(true);
        setWidthFull();
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        this.gameTitle = parameter.replace("-", " ");

        // Busca o jogo na lista pelo nome
        Game game = controller.list().stream()
                .filter(g -> g.getName().equalsIgnoreCase(gameTitle))
                .findFirst()
                .orElse(null);

        if (game != null) {
            this.imageUrl = game.getImage();
            createGameDetailsView(game);
        } else {
            add(new H2("Jogo não encontrado."));
        }

    }

    private HorizontalLayout getHorizontalLayout() {

        Button btnVoltar = new Button("Voltar",
                new Icon(VaadinIcon.ARROW_CIRCLE_LEFT));
        btnVoltar.addClassName("btn-voltar");
        btnVoltar.addClickListener(event -> {
            UI.getCurrent().navigate("/home");
        });

        Button addToFavoritesBtn = new Button("Adicionar aos favoritos",
                new Icon(VaadinIcon.BOOKMARK_O));
        addToFavoritesBtn.addClickListener(event -> {
                Game selectedGame = controller.list().stream()
                        .filter(g -> g.getName().equalsIgnoreCase(gameTitle))
                        .findFirst()
                        .orElse(null);

                if (selectedGame != null && selectedGame.getId() != null) {
                    Favorites favorite = favorites.stream()
                            .filter(f -> f.getUser().equals(controller.getCurrentUser()))
                            .findFirst()
                            .orElseGet(() -> {
                                Favorites newFavorite = new Favorites();
                                newFavorite.setUser(controller.getCurrentUser());
                                favorites.add(newFavorite);
                                return newFavorite;
                            });

                    favorite.addGame(selectedGame);

                    addToFavoritesBtn.setIcon(new Icon(VaadinIcon.BOOKMARK));
                    Notification.show("Jogo adicionado aos favoritos!",
                                    3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    Notification.show("Erro: Jogo não encontrado ou inválido para favoritar!",
                                    3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            });


// Modify the review button click listener in the getHorizontalLayout method
        Button btnReview = new Button("Fazer review");
        btnReview.getStyle()
                .set("align-items","center")
                .set("justify-content","center")
                .set("padding","10px")
                .set("color","#fff")
                .set("cursor","pointer")
                .set("background","#253244");
        btnReview.addClickListener(event -> isUserLoggedIn());

        HorizontalLayout cabecalho = new HorizontalLayout(btnVoltar,addToFavoritesBtn, btnReview);
        cabecalho.setJustifyContentMode(JustifyContentMode.END);
        cabecalho.setAlignItems(Alignment.CENTER);
        cabecalho.setWidthFull();
        return cabecalho;
    }

    private void isUserLoggedIn() {
        if (authenticatedUser.isAuthenticated()) {
            // User is logged in, navigate to review page
            UI.getCurrent().navigate("review/" + gameTitle.replace(" ", "-"));
        } else {
            // User is not logged in, show dialog
            com.vaadin.flow.component.dialog.Dialog loginDialog = new com.vaadin.flow.component.dialog.Dialog();
            loginDialog.setHeaderTitle("Login Necessário");

            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.add(new Paragraph("Você precisa estar logado para fazer uma review."));

            Button loginButton = new Button("Ir para Login", e -> {
                loginDialog.close();
                UI.getCurrent().navigate("login");
            });

            Button cancelButton = new Button("Cancelar", e -> loginDialog.close());
            cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            HorizontalLayout buttonLayout = new HorizontalLayout(loginButton, cancelButton);
            buttonLayout.setJustifyContentMode(JustifyContentMode.END);

            dialogLayout.add(buttonLayout);
            loginDialog.add(dialogLayout);

            loginDialog.open();
        }
    }

    private void createGameDetailsView(Game game) {
        removeAll();

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(true);
        header.addClassName("game-header");

        Div gameImage = new Div();
        Image imgGame = new Image();
        if (game.getImage() != null) {
            imgGame.setSrc("data:image/png;base64," + Base64.getEncoder().encodeToString(imageUrl));
            imgGame.setAlt(gameTitle);
            gameImage.addClassName("game-image");
            imgGame.getStyle().set("width", "300px");
            imgGame.getStyle().set("height", "400px");
            imgGame.getStyle().set("object-fit", "cover");
            imgGame.getStyle().set("border-radius", "4px");
            gameImage.add(imgGame);

        }

        VerticalLayout gameInfo = new VerticalLayout();
        gameInfo.setPadding(false);
        gameInfo.setSpacing(true);
        gameInfo.addClassName("game-info");

        H1 title = new H1(gameTitle);
        title.getStyle().set("margin-top", "0");

        HorizontalLayout ratings = new HorizontalLayout();
        ratings.setSpacing(true);

        double averageRating = 0;
        if (game.getReview() != null && !game.getReview().isEmpty()) {
            averageRating = game.getReview().stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        }

        String platformsText = game.getPlatforms().stream()
                .map(Platform::getDesc)
                .collect(Collectors.joining(" | "));

        Span spanPlatform = new Span(platformsText);
        spanPlatform.getStyle()
                .set("display","inline-block")
                .set("font-size", "13px")
                .set("align-items","end")
                .set("flex-direction","flex-end")
                .set("color","#FFCC00")
                .set("padding","8px 12px")
                .set("border-radius","4px")
                .set("background-color","rgb(26, 31, 43)");

        Span rating1 = createRating(String.format("%.1f", averageRating), "RATING");
        ratings.add(rating1, spanPlatform);

        Button btnByNow = new Button("Comprar jogo");
        btnByNow.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnByNow.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("margin-top", "16px")
                .set("color", "#FFCC00")
                .set("border"," 1px solid #FFCC00 ")
                .set("background-color","transparent");

        gameInfo.add(new VerticalLayout( getHorizontalLayout(),title,ratings, btnByNow));

        header.add(gameImage, gameInfo);

        Tabs detailsTabs = new Tabs();
        detailsTabs.add(
                new Tab("Reviews"),
                new Tab("Requisitos para o sistema")
        );
        detailsTabs.setSelectedIndex(0);
        detailsTabs.setWidthFull();
        detailsTabs.getStyle().set("margin-top", "32px");

        Div tabContent = new Div();
        tabContent.getStyle().setWidth("99%");
        tabContent.addClassName("review-box");
        tabContent.getStyle().set("padding", "10px");
        tabContent.getStyle().set("background-color", "#1a1f2b");
        tabContent.getStyle().set("border-radius", "4px");
        tabContent.getStyle().set("margin-top", "16px");

        H3 descriptionTitle = new H3("Descrição");
        descriptionTitle.getStyle().set("color","#cccc");
        VerticalLayout reviewsLayout = new VerticalLayout();

        if (game.getReview() != null) {
            game.getReview().forEach(review -> {
                H3 titleReview = new H3("Titulo: "+review.getTitle());
                Paragraph reviewContent = new Paragraph("Review: " +review.getContent());
                reviewsLayout.add(titleReview, reviewContent);
            });
        } else {
            reviewsLayout.add(new Paragraph("Este jogo ainda não possui reviews."));
        }
        tabContent.add(descriptionTitle, reviewsLayout);

        add(header, detailsTabs, tabContent);
    }

    private Span createRating(String score, String source) {
        Span rating = new Span();
        rating.getStyle().set("background-color", "#1a1f2b");
        rating.getStyle().set("padding", "8px 12px");
        rating.getStyle().set("border-radius", "4px");
        rating.getStyle().set("display", "inline-block");

        Span scoreSpan = new Span(score);
        scoreSpan.getStyle().set("color", "#ffcc00");
        scoreSpan.getStyle().set("font-weight", "bold");
        scoreSpan.getStyle().set("font-size", "16px");
        scoreSpan.getStyle().set("margin-right", "4px");

        Span sourceSpan = new Span(source);
        sourceSpan.getStyle().set("color", "#aaaaaa");
        sourceSpan.getStyle().set("font-size", "12px");

        rating.add(scoreSpan, sourceSpan);

        return rating;
    }
}