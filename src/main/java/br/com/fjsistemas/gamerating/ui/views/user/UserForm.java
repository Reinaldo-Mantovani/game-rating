package br.com.fjsistemas.gamerating.ui.views.user;

import br.com.fjsistemas.gamerating.controller.ContactController;
import br.com.fjsistemas.gamerating.controller.UserController;
import br.com.fjsistemas.gamerating.enuns.ContactType;
import br.com.fjsistemas.gamerating.enuns.Role;
import br.com.fjsistemas.gamerating.model.Contact;
import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.HashSet;
import java.util.Set;


@PageTitle("Usuários: Cadastrar & Editar")
@Route(value = "/user", layout = MainLayout.class)
@RouteAlias(value = "/user/", layout = MainLayout.class)
public class UserForm extends VerticalLayout implements HasUrlParameter<String> {

    private final UserController controller;

    private final TextField txtUsername;
    private final TextField txtName;
    private final PasswordField txtPassword;
    private final EmailField txtEmail;
    private final ComboBox<Role> cbxRole;
    private  ComboBox<ContactType> cbxType;
    private TextField txtValue;

    private Button btnVoltar;
    private Button btnSalvar;
    private Set<Contact> contacts = new HashSet<Contact>();

    private BeanValidationBinder<User> userBinder;

    private User user;
    private Contact contact;


    public UserForm(UserController controller) {
        this.controller = controller;


        txtUsername = new TextField("Usuário");
        txtName = new TextField("Nome");
        txtPassword = new PasswordField("Senha");
        txtEmail = new EmailField("Email");
        cbxRole = new ComboBox<>("Regra de acesso", Role.values());
        cbxType = new ComboBox<>("Tipo de contato", ContactType.values());
        txtValue = new TextField("Contato");

        btnVoltar = new Button("Voltar");
        btnVoltar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnVoltar.addClickListener(event -> {
            UI.getCurrent().navigate("/user");
        });

        try {
            createBinder();
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

        btnSalvar = new Button("Salvar");
        btnSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnSalvar.addClickListener(event -> {
            if (cbxType.getValue() != null && !txtValue.isEmpty()) {
                Contact contact = new Contact();
                contact.setType(cbxType.getValue());
                contact.setValue(txtValue.getValue());
                contact.setUser(user); // vincula ao usuário

                contacts.add(contact); // adiciona na lista local

                Notification.show("Contato adicionado com sucesso").addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                txtValue.clear();
                cbxType.clear();
            } else {
                Notification.show("Preencha o tipo e o valor do contato").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            try {
                userBinder.writeBean(user);

                // vincula a lista de contatos ao usuário
                for (Contact c : contacts) {
                    c.setUser(user);
                }
                user.setContacts(contacts);

                controller.save(user);
                clearField();
                Notification.show("Usuário cadastrado com sucesso!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                UI.getCurrent().navigate("/user");
            } catch (ValidationException e) {
                Notification.show("Erro de validação no formulário.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception ex) {
                Notification.show("Erro ao salvar no banco: " + ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });


        add(getHorizontalLayout(), createFormLayout(), new HorizontalLayout(btnVoltar,btnSalvar));
    }


    private Component createFormLayout() {
        FormLayout form = new FormLayout();
        form.add(txtName, txtUsername,txtPassword, txtEmail ,cbxRole);
        form.setColspan(txtName, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400", 3));

        return form;
    }

    // Create a horizontal layout for the button
    private HorizontalLayout getHorizontalLayout() {
        Button dialogButton = new Button("Adicionar contato",new Icon(VaadinIcon.CALC_BOOK));
        dialogButton.addClassNames(LumoUtility.Display.FLEX, LumoUtility.TextColor.ERROR);
        dialogButton.addClickListener(event -> {
            dialogComponent();
        });

        HorizontalLayout buttomContact = new HorizontalLayout(dialogButton);

        buttomContact.setJustifyContentMode(JustifyContentMode.END);
        buttomContact.setAlignItems(Alignment.CENTER);
        buttomContact.setWidthFull();
        return buttomContact;
    }


    private Component dialogComponent(){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Contato");

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);

        Div valuesContact = new Div();
        valuesContact.addClassNames(LumoUtility.Display.INLINE_FLEX, LumoUtility.Gap.MEDIUM);
        Paragraph values  = new Paragraph();

        values.addClassNames(LumoUtility.TextColor.SECONDARY,LumoUtility.FontSize.MEDIUM);
        valuesContact.add(values);

        add(dialog,valuesContact);

        dialog.open();

        getStyle()
                .set("position", "relative").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center");

        return dialog;
    }


    private VerticalLayout createDialogLayout() {

        cbxType = new ComboBox<>("Tipo de contato", ContactType.values());
        txtValue = new TextField("Contato");

        VerticalLayout dialogLayout = new VerticalLayout(cbxType,
                txtValue);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "30rem").set("max-width", "100%");

        return dialogLayout;
    }


    private void createBinder() throws ValidationException {
        userBinder = new BeanValidationBinder<>(User.class);

        userBinder.forField(txtUsername).bind("username");
        userBinder.forField(txtName).bind("name");
        userBinder.forField(txtPassword).bind("password");
        userBinder.forField(txtEmail).bind("email");
        userBinder.forField(cbxRole).bind("role");

    }

    private void clearField() {
        User user = new User();
        userBinder.readBean(user);
        userBinder.getFields().forEach(HasValue::clear);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String userId) {
        if ("new".equals(userId.trim())) {
            user = new User();
            userBinder.readBean(user);
            txtUsername.setReadOnly(false);
        } else {
            try {
                Long id = Long.valueOf(userId);
                user = controller.load(id);
                if (user != null) {
                    contacts = user.getContacts();
                    userBinder.readBean(user);

                    txtUsername.setReadOnly(true);
                } else {
                    Notification.show("Usuário não encontrado.")
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (NumberFormatException e) {
                Notification.show("ID de usuário inválido.")
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
}