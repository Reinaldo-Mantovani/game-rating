package br.com.fjsistemas.gamerating.ui.views.game;



import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.model.Game;
import br.com.fjsistemas.gamerating.model.Publisher;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.service.SecurityService;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@PageTitle("Games")
@Route(value = "/games", layout = MainLayout.class)
@RolesAllowed({"EDITOR", "ADM"})
public class GameView extends VerticalLayout {

    private final GameController controller;
    private User user;
    private Grid<Game> grid;

    private final SecurityService securityService;
    private final UserDetailsService userDetailsService;

    public GameView(GameController controller, SecurityService securityService, UserDetailsService userDetailsService) {
        this.controller = controller;
        this.securityService = securityService;
        this.userDetailsService = userDetailsService;

        setSizeFull();
        addClassNames(LumoUtility.Margin.MEDIUM);

        // Verifica se o usuário está logado
        if (UI.getCurrent().getSession().getAttribute("user") != null) {
            this.getElement().getThemeList().add("dark");
        } else {
            this.getElement().getThemeList().remove("dark");
        }

        HorizontalLayout cabecalho = getHorizontalLayout();

        add(
                cabecalho, new VerticalLayout(createGrid())
        );
    }


    /**
     * Método que cria o cabeçalho da tela de jogos.
     *
     * @return HorizontalLayout com os botões de ação
     */
    private HorizontalLayout getHorizontalLayout() {
        Button btnExcluir = new Button("Excluir", new Icon(VaadinIcon.TRASH));
        btnExcluir.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnExcluir.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnExcluir.addClickListener(this::onComponentEvent);


        Button btnEditar = new Button("Editar", new Icon(VaadinIcon.PENCIL));
        btnEditar.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnEditar.addClickListener(event -> {
            Set<Game> selectedItems = grid.getSelectedItems();
            selectedItems.stream().findFirst().ifPresent(selectedItem -> {
                UI.getCurrent().navigate("/games/" + selectedItem.getId());
            });
        });

        Button btnNovo = new Button(new Icon(VaadinIcon.PLUS));
        btnNovo.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnNovo.addClickListener(event -> {
            UI.getCurrent().navigate("/games/new");
        });

        HorizontalLayout cabecalho = new HorizontalLayout(btnNovo, btnEditar, btnExcluir);
        cabecalho.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cabecalho.setAlignItems(FlexComponent.Alignment.CENTER);
        cabecalho.setWidthFull();
        return cabecalho;
    }

    /**
     * Método que trata o evento de clique do botão de excluir.
     *
     * @param event evento de clique
     */
    private void onComponentEvent(ClickEvent<Button> event) {
        Set<Game> gameToDelete = grid.getSelectedItems();
        if (gameToDelete.isEmpty()) {
            showNotification("Erro ao excluír jogo: " , NotificationVariant.LUMO_ERROR);

        } else {
            gameToDelete.stream().findFirst().ifPresent(selectedItem -> {
                controller.deleteById(selectedItem.getId());
                showNotification("Jogo excluído com sucesso.", NotificationVariant.LUMO_SUCCESS);
            });

        }
        updateGrid();
    }


    /**
     * Método que atualiza o grid de jogos.
     */
    private void updateGrid() {
        grid.setItems(controller.list());
    }


    /**
     * Método que exibe uma notificação na tela.
     *
     * @param message mensagem a ser exibida
     * @param variant variante da notificação
     */
    private void showNotification(String message, NotificationVariant variant) {
        Notification.show(message, 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(variant);
    }

    /**
     * Método que cria o grid de jogos.
     *
     * @return Grid<Game> com os jogos
     */
    private Component createGrid() {
        grid = new Grid<>(Game.class, false);
        grid.getStyle()
                .set("margi-bottom", "0.5rem")
                .setHeight("640px");

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(createPosterAndWebsite()).setHeader("Jogos").setSortable(true).setResizable(true)
                .setAutoWidth(true).setFlexGrow(0)
                .setComparator(Game::getName);
     grid.addColumn(getUsername()).setHeader("Usuário").setSortable(true).setResizable(true)
             .setComparator(game -> game.getReview().stream()
                     .findFirst()
                     .map(review -> review.getUser().getUsername())
                     .orElse("Desconhecido"));
        grid.addColumn(getDatePost()).setHeader("Data da publicação").setSortable(true).setResizable(true)
                .setComparator(Game::getReleaseDate);
        List<Game> games = controller.list();
        grid.setItems(games);


        grid.addItemClickListener(event -> showDetails(event.getItem()));

        return grid;
    }


    /**
     * Método que cria o renderer para o grid de jogos.
     *
     * @return Renderer<Game> com o jogo
     */
    private static Renderer<Game> createPosterAndWebsite() {
    return LitRenderer.<Game>of(
                    "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                            + "  <vaadin-avatar img=\"${item.pictureUrl}\" name=\"${item.fullName}\"></vaadin-avatar>"
                            + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-l);\">"
                            + "    <span> ${item.fullName} </span>"
                            + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                            + "      ${item.website}" + "</span>"
                            + "  </vaadin-vertical-layout>"
                            + "</vaadin-horizontal-layout>")
            .withProperty("pictureUrl", game -> game.getImage() != null
                ? "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(game.getImage())
                : "")
            .withProperty("fullName", Game::getName)
            .withProperty("website", Game::getWebsite);
}
    private static Renderer<Game> getDatePost() {
        var releaseDate = LitRenderer.<Game>of(
                        "<vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "  <span>${item.releaseDate}</span>"
                                + "</vaadin-vertical-layout>")
                .withProperty("releaseDate", game -> game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return releaseDate;

    }


   private static Renderer<Game> getUsername() {
       return LitRenderer.<Game>of(
                       "<vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                               + "  <span>${item.username}</span>"
                               + "</vaadin-vertical-layout>")
               .withProperty("username", game -> game.getReview().stream()
                       .findFirst()
                       .map(review -> review.getUser().getUsername())
                       .orElse("Desconhecido"));
   }

    /**
     * Método que exibe os detalhes do jogo em um dialog.
     *
     * @param game jogo a ser exibido
     */
    private void showDetails(Game game) {
        if (game == null) {
            Notification.show("Nenhum jogo selecionado.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        Dialog details = new Dialog();

        details.setCloseOnEsc(true);
        details.setWidth("600px");
        details.setHeight("400px");

        Div content = new Div();
        content.addClassName("game-details");
        content.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("padding", "20px");


        Div gameInfo = new Div();

        // Game image
        Image imgGame = new Image();
        if (game.getImage() != null) {
            imgGame.setSrc("data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(game.getImage()));
            imgGame.setAlt(game.getName());
            imgGame.getStyle()
                    .set("width", "330px")
                    .set("height", "200px")
                    .set("margin-top", "0.4rem")
                    .set("object-fit", "cover")
                    .set("border-radius", "4px");
            content.add(imgGame);
        } else {
            imgGame.setSrc("https://via.placeholder.com/250x150");
            imgGame.setAlt("Imagem não disponível");
            imgGame.getStyle().set("width", "250px");
            imgGame.getStyle().set("height", "150px");
            imgGame.getStyle().set("object-fit", "cover");
            imgGame.getStyle().set("border-radius", "4px");
            content.add(imgGame);
        }

        // Game title
        H3 title = new H3(game.getName());
        title.getStyle()
                .set("margin-top", "0")
                .set("color", "#253244")
                .set("margin-top", "0.5rem");
        content.add(title);

        // Game website
        if (game.getWebsite() != null) {
            content.add(new Span("Website: " + game.getWebsite()));
        } else {
            content.add(new Span("Website: Não disponível"));
        }

        // Usuário que postou o jogo
        if (game.getReview() != null && !game.getReview().isEmpty()) {
            String username = game.getReview().stream()
                    .findFirst()
                    .map(review -> review.getUser().getUsername())
                    .orElse("Desconhecido");
            content.add(new Span("Postado por: " + username));
        } else {
            content.add(new Span("Postado por: Não disponível"));
        }
        // Data de publicação
        if (game.getReleaseDate() != null) {
            content.add(new Span("Data de publicação: " + game.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        } else {
            content.add(new Span("Data de publicação: Não disponível"));
        }

        gameInfo.add(content);

        details.add(gameInfo);
        details.open();
    }

}