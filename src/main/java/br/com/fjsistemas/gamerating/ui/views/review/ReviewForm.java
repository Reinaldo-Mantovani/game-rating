package br.com.fjsistemas.gamerating.ui.views.review;

import br.com.fjsistemas.gamerating.config.AuthenticatedUser;
import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.controller.ReviewController;
import br.com.fjsistemas.gamerating.enuns.Platform;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Review;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.WildcardParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Base64;
import java.util.Optional;

@com.vaadin.flow.router.Route(value = "review", layout = MainLayout.class)
public class ReviewForm extends VerticalLayout implements HasUrlParameter<String> {

    private String gameTitle;
    private Game selectedGame;

    private final TextField txtTitle = new TextField("Título");
    private final TextArea txtContent = new TextArea("Conteúdo da review");
    private final IntegerField numberRating = new IntegerField("Nota (1 a 5)");
    private final ComboBox<Platform> cbxPlatform = new ComboBox<>("Plataforma", Platform.values());

    private final Button btnVoltar = new Button("Voltar");
    private final Button btnSalvar = new Button("Salvar review");

    private final HorizontalLayout headerLayout = new HorizontalLayout();
    private final Image gameImage = new Image();
    private final H2 gameTitleComponent = new H2();

    private BeanValidationBinder<Review> reviewBinder;
    private Review review = new Review();

    private final ReviewController controller;
    private final GameController gameController;

    @Autowired
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    private final UserDetailsService userDetailsService;

    public ReviewForm(ReviewController controller, GameController gameController, AuthenticatedUser authenticatedUser, UserDetailsService userDetailsService) {
        this.controller = controller;
        this.gameController = gameController;
        this.authenticatedUser = authenticatedUser;
        this.userDetailsService = userDetailsService;

        setupHeader();
        setupForm();
        setupBinder();
        setupButtons();

        add(headerLayout, createFormLayout(), new HorizontalLayout(btnVoltar, btnSalvar));
    }

    private void setupHeader() {
        gameImage.setWidth("100px");
        gameImage.setHeight("150px");
        gameImage.getStyle().set("object-fit", "cover").set("border-radius", "4px");

        gameTitleComponent.getStyle().set("margin", "0");

        headerLayout.setWidthFull();
        headerLayout.setSpacing(true);
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.add(gameImage, gameTitleComponent);
    }

    private void setupForm() {
        numberRating.setMin(1);
        numberRating.setMax(5);
        numberRating.setStep(1);
        numberRating.setValue(3);
    }

    private void setupButtons() {
        btnVoltar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnVoltar.addClickListener(event -> UI.getCurrent().navigate("/home"));

        btnSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSalvar.addClickListener(event -> salvarReview());
    }

    private void setupBinder() {
        reviewBinder = new BeanValidationBinder<>(Review.class);
        reviewBinder.forField(txtTitle).asRequired("O título é obrigatório").bind("title");
        reviewBinder.forField(txtContent).asRequired("O conteúdo é obrigatório").bind("content");
        reviewBinder.forField(numberRating).asRequired("A nota é obrigatória").bind("rating");
        reviewBinder.forField(cbxPlatform).asRequired("A plataforma é obrigatória").bind("platform");
    }

    private Component createFormLayout() {
        FormLayout form = new FormLayout(txtTitle, txtContent, numberRating, cbxPlatform);
        txtContent.setMinHeight("150px");
        txtContent.setMaxHeight("300px");
        form.setColspan(txtContent, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );
        return form;
    }

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        this.gameTitle = parameter.replace('-', ' ');

        selectedGame = gameController.list().stream()
                .filter(g -> g.getName().equalsIgnoreCase(gameTitle))
                .findFirst()
                .orElse(null);

        if (selectedGame != null) {
            review.setGame(selectedGame);
            updateHeader(selectedGame);

            if (selectedGame.getPlatforms() != null && selectedGame.getPlatforms().size() == 1) {
                cbxPlatform.setValue(selectedGame.getPlatforms().iterator().next());
            }
        } else {
            removeAll();
            add(new H2("Jogo não encontrado."));
            Button btn = new Button("Voltar para Home", e -> UI.getCurrent().navigate("/home"));
            add(btn);
        }
    }

    private void updateHeader(Game game) {
        if (game == null) return;

        if (game.getImage() != null) {
            gameImage.setSrc("data:image/png;base64," + Base64.getEncoder().encodeToString(game.getImage()));
        } else {
            gameImage.setSrc("https://via.placeholder.com/100x150");
        }

        gameImage.setAlt(game.getName());
        gameTitleComponent.setText("Review: " + game.getName());
    }

    private void salvarReview() {
        try {

            if (!authenticatedUser.isAuthenticated()) {
                Notification.show("Usuário não autenticado. Faça login para salvar a review.",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (selectedGame == null) {
                Notification.show("Jogo não encontrado. Não é possível salvar a review.",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (review == null) {
                review = new Review();
            }

            Optional<User> optionalUser = authenticatedUser.getAuthenticatedUser();
            if (!optionalUser.isPresent()) {
                Notification.show("Erro ao obter o usuário autenticado.",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            review.setUser(authenticatedUser.getAuthenticatedUser().get());

            review.setGame(selectedGame);

            reviewBinder.writeBean(review);

            controller.save(review);

            Notification.show("Review salva com sucesso!",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate("games-details/" + selectedGame.getName().replace(" ", "-"));

        } catch (ValidationException ve) {
            StringBuilder errorMessages = new StringBuilder();
            ve.getFieldValidationErrors().forEach(error -> {
                errorMessages.append(error.getMessage()).append(". ");
            });
            Notification.show("Erro de validação: " + errorMessages.toString(),
                            5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } catch (Exception e) {
            Notification.show("Erro ao salvar a review: " + e.getMessage(),
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            e.printStackTrace();
        }
    }
}
