package br.com.fjsistemas.gamerating.ui.views.user;

import br.com.fjsistemas.gamerating.controller.UserController;
import br.com.fjsistemas.gamerating.model.Contact;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaders.setHeader;

@PageTitle("Usuários")
@Route(value = "/user", layout = MainLayout.class)
@RolesAllowed({"EDITOR", "ADM"})
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Usuário")
public class UserView extends VerticalLayout  {

    private final UserController controller;
    private final Grid<User> grid;

    public UserView(UserController controller) {
        this.controller = controller;

        TextField textField = new TextField();
        textField.setPlaceholder("Buscar usuário");
        textField.setPrefixComponent(new Icon("lumo", "Buscar"));
        textField.setTooltipText("Buscar por: id ou descrição");

        VerticalLayout search = new VerticalLayout(textField);
        search.getStyle().setAlignItems(Style.AlignItems.NORMAL);
        search.setWidthFull();

        grid = new Grid<>();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(User::getId).setHeader("Id").setSortable(true).setResizable(true);
        grid.addColumn(User::getUsername).setHeader("Username").setSortable(true).setResizable(true);
        grid.addColumn(User::getName).setHeader("Name").setSortable(true).setResizable(true);
        grid.addColumn(User::getEmail).setHeader("Email").setSortable(true).setResizable(true);
        grid.addColumn(User::getRole).setHeader("Role").setSortable(true).setResizable(true);
        grid.addColumn(user -> user.getContacts().stream()
                        .map(contact -> contact.getType() + ": " + contact.getValue())
                        .collect(Collectors.joining(", ")))
                .setHeader("Contatos")
                .setSortable(true).setResizable(true);
        grid.addColumn(user -> user.getCreatedAt()
                .format((DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))))
                .setHeader("Data de cadastro").setSortable(true).setResizable(true);
        grid.addColumn(user -> user.getUpdatedAt()
                .format((DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))))
                .setHeader("Data de atualização").setSortable(true).setResizable(true);

        List<User> list = controller.list();

        grid.setItems(list);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidthFull();
        HorizontalLayout cabecalho = getHorizontalLayout();

        VerticalLayout vertical = new VerticalLayout(grid);

        add(cabecalho, search, vertical);
    }

    private HorizontalLayout getHorizontalLayout() {
        Button btnExcluir = new Button("Excluir", new Icon(VaadinIcon.TRASH));
        btnExcluir.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnExcluir.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnExcluir.addClickListener(this::onComponentEvent);


        Button btnEditar = new Button("Editar", new Icon(VaadinIcon.PENCIL));
        btnEditar.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnEditar.addClickListener(event -> {
            Set<User> selectedItems = grid.getSelectedItems();
            selectedItems.stream().findFirst().ifPresent(selectedItem -> {
                UI.getCurrent().navigate("/user/" + selectedItem.getId());
            });
        });

        Button btnNovo = new Button(new Icon(VaadinIcon.PLUS));
        btnNovo.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnNovo.addClickListener(event -> {
            UI.getCurrent().navigate("/user/new");
        });

        HorizontalLayout cabecalho = new HorizontalLayout(btnNovo, btnEditar, btnExcluir);
        cabecalho.setJustifyContentMode(JustifyContentMode.END);
        cabecalho.setAlignItems(Alignment.CENTER);
        cabecalho.setWidthFull();
        return cabecalho;
    }

    private Optional<User> getSelectedUser() {
        Set<User> selectedUser  = grid.getSelectedItems();
        if (selectedUser.isEmpty()) {
            return Optional.empty();
        } else {
            return selectedUser.stream().findFirst();
        }
    }

    private void onComponentEvent(ClickEvent<Button> event) {
        Optional<User> userToDelete = getSelectedUser (); // Obtém o usuário selecionado

        if (userToDelete.isEmpty()) {
            Notification.show("Erro: Nenhum usuário selecionado para excluir.")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        ConfirmDialog confirmDialog = new ConfirmDialog("Confirmação", "Você tem certeza que deseja excluir este usuário?", "Excluir", e -> {

            try {
                ConfirmDialog confirmDialog1 = new ConfirmDialog();
                controller.deleteById(userToDelete.get().getId());

                Notification.show("Usuário excluído com sucesso!")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);


                grid.setItems(controller.list());
                confirmDialog1.close();
            } catch (Exception exception) {

                Notification.show("Erro ao excluir usuário: " + exception.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }

        }, "Cancelar" , e -> {
            ConfirmDialog confirmDialog2 = new ConfirmDialog();
            confirmDialog2.addClassNames("confirm-dialog-cancelar");
            confirmDialog2.close();
        });

        confirmDialog.open();
    }
}
