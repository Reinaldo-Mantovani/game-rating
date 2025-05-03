package br.com.fjsistemas.gamerating.ui.views.game;

import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.controller.ReviewController;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Review;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Page;

import java.text.ParseException;
import java.util.*;

public class TopGames extends VerticalLayout {

    private List<Game> top10RatedGames;
    private GameController gameController;
    private ReviewController reviewController;
    private FlexLayout sliderContainer;
    private int currentPosition = 0;
    private List<Review> topGames;
    private Map<Integer, Game> gameMap = new HashMap<>();

    private byte[] imageUrl;
    private  String title;


    /**
     * Construtor da classe TopGames.
     *
     * @throws ParseException Se houver um erro ao analisar os dados do jogo.
     */
    public TopGames(GameController gameController, ReviewController reviewController, byte[] imageUrl, String title,
                    List<Game> top10RatedGames, List<Review> list) throws ParseException {
        this.gameController = gameController;
        this.reviewController = reviewController;
        this.title = title;
        this.imageUrl = imageUrl;
        this.top10RatedGames = top10RatedGames;
        this.topGames = list;


        setAlignItems(Alignment.CENTER);

        setPadding(true);
        setSpacing(true);
        setWidthFull();
        addClassName("top-games-view");
        getStyle().set("background-color", "#141414");

        H2 titleSection = new H2("Top 10 Games");
        titleSection.getStyle()
                .set("color", "white")
                .set("margin-bottom", "20px")
                .set("margin-left", "20px");

        add(titleSection);

        try {
            loadGames();
            createSlider();
        } catch (ParseException e) {
            add(createErrorMessage("Erro ao carregar jogo: " + e.getMessage()));
        }
    }

    public TopGames(List<Game> top10RatedGames, List<Review> list, VerticalLayout section) {
            this.top10RatedGames = top10RatedGames;
            this.topGames = list;

    }

    /**
     * Método para carregar os jogos.
     *
     * @throws ParseException Se houver um erro ao analisar os dados do jogo.
     */
    private void loadGames() throws ParseException {
        // Pega os 10 melhores jogos
        topGames = getTopRatedGames();

        // Cria um mapa de jogos para acesso rápido com base nos IDs
        List<Game> allGames = gameController.list();
        for (Game game : allGames) {
            gameMap.put(Math.toIntExact(game.getId()), game);
        }
    }

    private void createSlider() {
        // Cria a seção do slider
        VerticalLayout sliderSection = new VerticalLayout();
        sliderSection.setPadding(false);
        sliderSection.setSpacing(false);
        sliderSection.setWidthFull();

        // Cria os controles do slider
        HorizontalLayout controls = new HorizontalLayout();
        controls.setWidthFull();
        controls.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Button prevButton = new Button(new Icon(VaadinIcon.ARROW_LEFT));
        prevButton.addClassName("slider-control");
        prevButton.getStyle()
                .set("background-color", "rgba(0,0,0,0.5)")
                .set("color", "white")
                .set("border", "none")
                .set("border-radius", "50%")
                .set("padding", "10px");
        prevButton.addClickListener(e -> slideLeft());

        Button nextButton = new Button(new Icon(VaadinIcon.ARROW_RIGHT));
        nextButton.addClassName("slider-control");
        nextButton.getStyle()
                .set("background-color", "rgba(0,0,0,0.5)")
                .set("color", "white")
                .set("border", "none")
                .set("border-radius", "50%")
                .set("padding", "10px");
        nextButton.addClickListener(e -> slideRight());

        controls.add(prevButton, nextButton);

        // Cria o container do slider
        sliderContainer = new FlexLayout();
        sliderContainer.setWidthFull();
        sliderContainer.getStyle()
                .set("overflow-x", "hidden")
                .set("display", "flex")
                .set("flex-wrap", "nowrap")
                .set("transition", "transform 0.5s ease")
                .set("padding", "20px 0");

        // Add o jogo ao slider
        if (topGames != null && !topGames.isEmpty()) {
            for (int i = 0; i < topGames.size(); i++) {
                Review review = topGames.get(i);
                Game game = gameMap.get(review.getGame().getId());

                if (game != null) {
                    Component gameItem = createGameItem(i + 1, game, review.getRating());
                    sliderContainer.add(gameItem);
                }
            }
        } else {
            sliderContainer.add(createErrorMessage("Jogo não encontrado!"));
        }

        sliderSection.add(sliderContainer, controls);
        add(sliderSection);
    }

    private Component createGameItem(int position, Game game, double rating) {
        // Cria um container para o item do jogo
        VerticalLayout gameItem = new VerticalLayout();
        gameItem.setPadding(false);
        gameItem.setSpacing(false);
        gameItem.setWidth("300px");
        gameItem.setHeight("400px");
        gameItem.getStyle()
                .set("flex-shrink", "0")
                .set("margin-right", "10px")
                .set("position", "relative");

        // Cria o numero no elemento
        Div numberElement = new Div();
        numberElement.setText(String.valueOf(position));
        numberElement.getStyle()
                .set("position", "absolute")
                .set("font-size", "180px")
                .set("font-weight", "bold")
                .set("color", "#404040")
                .set("z-index", "1")
                .set("left", "-20px")
                .set("top", "50px")
                .set("line-height", "1")
                .set("opacity", "0.8");

        // Cria um poster
        Div posterContainer = new Div();
        posterContainer.setWidth("220px");
        posterContainer.setHeight("300px");
        posterContainer.getStyle()
                .set("position", "relative")
                .set("z-index", "2")
                .set("margin-left", "60px")
                .set("background-color", "#333")
                .set("border-radius", "4px")
                .set("overflow", "hidden");

        // Se o jogo tem imagem, adiciona a imagem ao poster, se não, adiciona um placeholder
        if (game.getImage() != null) {
            Image poster = new Image(Arrays.toString(imageUrl),title);
            poster.setSrc("data:image/png;base64," + Base64.getEncoder().encodeToString(imageUrl));
            poster.getStyle()
                    .set("object-fit", "cover")
                    .set("border-radius", "4px")
                    .set("width", "100%")
                    .set("height", "100%");
            posterContainer.add(poster);

            // Add clickListener no poster
            poster.addClickListener(e -> {
                // Handle click event (e.g., navigate to game details)
                getUI().ifPresent(ui -> ui.navigate("game/" + game.getId()));
            });

            // Seta config de Largura e altura da imagem
            poster.setWidth("100%");
            poster.setHeight("100%");
            posterContainer.add(poster);
        } else {
            Div placeholder = new Div();
            placeholder.setText(title);
            placeholder.getStyle()
                    .set("display", "flex")
                    .set("align-items", "center")
                    .set("justify-content", "center")
                    .set("width", "100%")
                    .set("height", "100%")
                    .set("color", "white")
                    .set("text-align", "center")
                    .set("padding", "10px");
            posterContainer.add(placeholder);
        }

        // Seta imagem de como background
        if (game.getImage() != null) {
            Image gameImage = new Image(Arrays.toString(imageUrl), title);
            gameImage.setSrc("data:image/png;base64," + Base64.getEncoder().encodeToString(imageUrl));
            gameImage.setWidth("100%");
            gameImage.setHeight("100%");
            gameImage.getStyle().set("object-fit", "cover");
            posterContainer.add(gameImage);
        }

        // Add efeito hover
        posterContainer.getStyle()
                .set("transition", "transform 0.3s ease")
                .set("cursor", "pointer");
        posterContainer.addClickListener(e -> {
            // Handle click event (e.g., navigate to game details)
            getUI().ifPresent(ui -> ui.navigate("game/" + game.getId()));
        });

        // Add gradient overlay
        Div overlay = new Div();
        overlay.getStyle()
                .set("position", "absolute")
                .set("top", "0")
                .set("left", "0")
                .set("width", "100%")
                .set("height", "100%")
                .set("background-color", "rgba(0, 0, 0, 0.5)")
                .set("z-index", "1");
        posterContainer.add(overlay);

        // Cria container para as informações do jogo
        Div infoContainer = new Div();
        infoContainer.getStyle()
                .set("margin-left", "60px")
                .set("margin-top", "10px")
                .set("color", "white")
                .set("z-index", "2")
                .set("position", "relative");

        // Titulo do jogo
        Div title = new Div();
        title.setText(game.getName());
        title.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "16px")
                .set("white-space", "nowrap")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis");

        // Nota
        Div ratingDiv = new Div();
        ratingDiv.getStyle().set("display", "flex").set("align-items", "center");

        // Add stars basedos na nota
        for (int i = 0; i < 5; i++) {
            Icon star;
            if (i < Math.floor(rating)) {
                star = VaadinIcon.STAR.create();
                star.setColor("gold");
            } else if (i < rating) {
                star = VaadinIcon.STAR_HALF_LEFT_O.create();
                star.setColor("gold");
            } else {
                star = VaadinIcon.STAR_O.create();
                star.setColor("#aaa");
            }
            star.setSize("16px");
            ratingDiv.add(star);
        }

        Span ratingText = new Span(String.format("%.1f", rating));
        ratingText.getStyle().set("margin-left", "5px").set("color", "#aaa");
        ratingDiv.add(ratingText);

        infoContainer.add(title, ratingDiv);

        // Add todos os componentes ao gameItem
        gameItem.add(numberElement, posterContainer, infoContainer);

        return gameItem;
    }

    private void slideLeft() {
        if (currentPosition > 0) {
            currentPosition--;
            updateSliderPosition();
        }
    }

    private void slideRight() {
        if (currentPosition < topGames.size() - 3) {  // Show 3 items at a time
            currentPosition++;
            updateSliderPosition();
        }
    }

    private void updateSliderPosition() {
        int slideAmount = currentPosition * -310;  // 300px width + 10px margin
        sliderContainer.getElement().getStyle().set("transform", "translateX(" + slideAmount + "px)");
    }

    private Div createErrorMessage(String message) {
        Div errorDiv = new Div();
        errorDiv.setText(message);
        errorDiv.getStyle()
                .set("padding", "var(--spacing-l)")
                .set("color", "var(--text-secondary)")
                .set("text-align", "center")
                .set("width", "100%");
        return errorDiv;
    }

    /**
     * Método para configurar o layout responsivo
     */
    private void setupResponsiveLayout() {
        // Register a resize listener to adjust the number of visible items
        Page page = UI.getCurrent().getPage();
        page.retrieveExtendedClientDetails(details -> {
            int screenWidth = details.getScreenWidth();
            adjustForScreenSize(screenWidth);
        });

        // Add a resize listener to adjust when window size changes
        page.addBrowserWindowResizeListener(event -> {
            adjustForScreenSize(event.getWidth());
        });
    }

    private void adjustForScreenSize(int width) {
        int itemsPerView;
        if (width < 600) {
            // Mobile: show 1 item at a time
            currentPosition = 1;
        } else if (width < 960) {
            // Tablet: show 2 items at a time
            itemsPerView = 2;
        } else {
            // Desktop: show 3 items at a time
            itemsPerView = 3;
        }

        // Update the slider position based on new itemsPerView
        updateSliderPosition();
    }

    /**
     * Método para obter os 10 melhores jogos com classificação mais alta
     *
     * @return List of Reviews para jogos com melhor classificação
     */
    private List<Review> getTopRatedGames() {
        List<Review> reviews = reviewController.list();

        if (reviews != null && !reviews.isEmpty()) {
            // Ordena as avaliações por classificação (do maior para o menor)
            reviews.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));

            // Limite de 10 jogos
            if (reviews.size() > 10) {
                reviews = reviews.subList(0, 10);
            }

            return reviews;
        } else {
            return new ArrayList<>();
        }
    }



}