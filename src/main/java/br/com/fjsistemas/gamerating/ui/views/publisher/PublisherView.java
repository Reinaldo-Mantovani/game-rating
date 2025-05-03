package br.com.fjsistemas.gamerating.ui.views.publisher;

import br.com.fjsistemas.gamerating.controller.PublisherController;
import br.com.fjsistemas.gamerating.model.Publisher;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Publisher")
@Route(value = "/publisher", layout = MainLayout.class)
@RolesAllowed({"EDITOR", "ADM"})
public class PublisherView extends VerticalLayout {

    private Grid<Publisher> grid;
    private TextField txtName = new TextField("Editora");
    private TextField txtWebsite = new TextField("Website");

    private Dialog dialog;
    private BeanValidationBinder<Publisher> publisherBinder;
    private Publisher publisher;

    private final PublisherController controller;

    public PublisherView(PublisherController controller) {
        this.controller = controller;

        configureLayout();
        configureGrid();
        configureDialog();

        add(createHeader(), grid);
    }

    private void configureLayout() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setWidthFull();
        setSizeFull();
    }

    private void configureDialog() {
        dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeight("270px");

        FormLayout form = new FormLayout(txtName, txtWebsite);
        createBinder();

        Button btnCancel = new Button("Cancelar", e -> {
            clearFields();
            dialog.close();
        });

        Button btnSave = new Button("Salvar", e -> savePublisher());
        HorizontalLayout buttons = new HorizontalLayout(btnCancel, btnSave);

        form.add(buttons);
        dialog.add(form);
    }

    private void configureGrid() {
        grid = new Grid<>(Publisher.class, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setItems(controller.list());

        grid.addColumn(Publisher::getId).setHeader("Id").setSortable(true).setResizable(true);
        grid.addColumn(Publisher::getName).setHeader("Editora").setSortable(true).setResizable(true);
        grid.addColumn(Publisher::getWebsite).setHeader("Website").setSortable(true).setResizable(true);

        grid.setHeightFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.addItemClickListener(event -> showDetails(event.getItem()));
    }

    private void createBinder() {
        publisherBinder = new BeanValidationBinder<>(Publisher.class);
        publisherBinder.forField(txtName).bind("name");
        publisherBinder.forField(txtWebsite).bind("website");
    }

    private HorizontalLayout createHeader() {
        Button btnNovo = new Button(new Icon(VaadinIcon.PLUS), event -> {
            publisher = new Publisher();
            publisherBinder.readBean(publisher);
            dialog.open();
        });

        Button btnEditar = new Button("Editar", new Icon(VaadinIcon.PENCIL));
        btnEditar.addClickListener(event -> grid.getSelectedItems().stream().findFirst().ifPresent(selected -> {
            publisher = selected;
            publisherBinder.readBean(publisher);
            dialog.open();
        }));

        Button btnExcluir = new Button("Excluir", new Icon(VaadinIcon.TRASH));
        btnExcluir.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnExcluir.addClickListener(this::deletePublisher);

        HorizontalLayout header = new HorizontalLayout(btnNovo, btnEditar, btnExcluir);
        header.setJustifyContentMode(JustifyContentMode.END);
        header.setAlignItems(Alignment.CENTER);
        header.setWidthFull();
        return header;
    }

    private void savePublisher() {
        try {
            publisherBinder.writeBean(publisher);
            if (publisher.getId() == null) {
                controller.save(publisher);
                showNotification("Editora salvo com sucesso!", NotificationVariant.LUMO_SUCCESS);
            } else {
                controller.update(publisher);
                showNotification("Editora atualizado com sucesso!", NotificationVariant.LUMO_SUCCESS);
            }
            dialog.close();
            updateGrid();
            clearFields();
        } catch (ValidationException e) {
            showNotification("Erro ao salvar Editora: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    private void deletePublisher(ClickEvent<Button> event) {
        grid.getSelectedItems().stream().findFirst().ifPresent(selected -> {
            controller.deleteById(selected.getId());
            showNotification("Editora excluído com sucesso!", NotificationVariant.LUMO_SUCCESS);
            updateGrid();
        });
    }

    private void showDetails(Publisher publisher) {
        Dialog details = new Dialog();
        details.setWidth("500px");
        details.setHeight("100px");
        details.add(new Div(new Text("Editora: " + publisher.getName())));
        details.add(new Div(new Text("Website: " + publisher.getWebsite())));
        details.open();
    }

    private void showNotification(String message, NotificationVariant variant) {
        Notification.show(message, 3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(variant);
    }

    private void updateGrid() {
        grid.setItems(controller.list());
    }

    private void clearFields() {
        txtName.clear();
        txtWebsite.clear();
    }
}
