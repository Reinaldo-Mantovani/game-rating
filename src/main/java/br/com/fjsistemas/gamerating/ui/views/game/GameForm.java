package br.com.fjsistemas.gamerating.ui.views.game;

import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.enuns.Platform;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import java.time.LocalDate;


@PageTitle("Games: Cadastrar & Editar")
@Route(value = "/games/:id", layout = MainLayout.class)
@RolesAllowed({"EDITOR", "ADM"})
public class GameForm extends VerticalLayout {

    private final GameController controller;

    private final TextField txtName;
    private final TextField txtWebsite;
    private final MultiSelectComboBox<Platform> cbxPlatform;
    private final Upload imageGame;

    private  Button btnVoltar;
    private  Button btnSalvar;

    private BeanValidationBinder<Game> gameBinder;
    private Game game;
    private byte[] imageBytes;


    public GameForm(GameController controller){
        this.controller = controller;

        txtName = new TextField("Name");
        txtWebsite = new TextField("Website");
        cbxPlatform = new MultiSelectComboBox<>("Platform", Platform.values());
        imageGame = new Upload();
        imageGame.setAcceptedFileTypes("image/jpeg", "image/png", "image/jpg");

        MemoryBuffer buffer = new MemoryBuffer();
        imageGame.setReceiver(buffer);

        imageGame.addSucceededListener(event -> {
            try {
                imageBytes = buffer.getInputStream().readAllBytes();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnVoltar = new Button("Voltar");
        btnVoltar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnVoltar.addClickListener(event -> UI.getCurrent().navigate("/games"));

        btnSalvar = new Button("Salvar");
        btnSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSalvar.addClickListener(event -> {
            try {
                gameBinder.writeBean(game);
                game.setImage(imageBytes);

                if (game.getId() == null) {
                    // Novo game
                    game.setReleaseDate(LocalDate.now());
                    controller.save(game);
                    Notification.show("Game cadastrado com sucesso",
                                    3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else {
                    // Editar game existente
                    Game existingGame = controller.load(game.getId());
                    if (existingGame != null) {
                        game.setReleaseDate(existingGame.getReleaseDate());
                        gameBinder.readBean(game);
                        controller.save(game);
                        Notification.show("Game atualizado com sucesso")
                                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    } else {
                        Notification.show("Erro: Game não encontrado para edição",
                                        3000, Notification.Position.TOP_CENTER)
                                .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }

                clearField();
                UI.getCurrent().navigate("/home");
            } catch (ValidationException e) {
                e.printStackTrace();
                Notification.show("Erro de validação no formulário.",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception ex) {
                Notification.show("Erro ao salvar no banco: " + ex.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });


        this.game = new Game();
        try {
            createBinder();
            gameBinder.readBean(game);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        imageGame.addSucceededListener(event -> {
            try {
                imageBytes = buffer.getInputStream().readAllBytes();
                Image imagePreview = new Image();
                imagePreview.setSrc("data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes));
                imagePreview.setAlt("Preview da imagem carregada");
                imagePreview.setWidth("150px");
                imagePreview.setHeight("150px");
                add(imagePreview);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        add(createFormLayout(), new HorizontalLayout(btnVoltar, btnSalvar));
    }

    private Component createFormLayout() {
        FormLayout form = new FormLayout();
        form.add(txtName, txtWebsite, cbxPlatform ,imageGame);
        form.setColspan(txtName, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400", 3));
        return form;
    }

    private void createBinder() throws ValidationException {
        gameBinder = new BeanValidationBinder<>(Game.class);

        gameBinder.forField(txtName).bind("name");
        gameBinder.forField(txtWebsite).bind("website");
        gameBinder.forField(cbxPlatform).bind("platforms");

    }

    private void clearField() {
        Game game = new Game();
        gameBinder.readBean(game);
        gameBinder.getFields().forEach(HasValue::clear);
    }
}
