package br.com.fjsistemas.gamerating.ui.views.login;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route("forgot-password")
@PageTitle("Esqueci a Senha")
@CssImport("./../frontend/themes/default/login-view.css")
@AnonymousAllowed
public class ForgotPasswordView extends VerticalLayout {


    private final EmailField emailField;
    private final Button submitButton;
    private final Button backToLoginButton;

    @Autowired
    public ForgotPasswordView() {


        addClassNames("forgot-password-view", LumoUtility.Padding.MEDIUM);
        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Create a container with fixed width
        VerticalLayout container = new VerticalLayout();
        container.setWidth("400px");
        container.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("background-color", "white")
                .set("border-radius", "4px")
                .set("box-shadow", "0 2px 10px rgba(0, 0, 0, 0.1)");
        container.setPadding(true);
        container.setSpacing(true);

        // Add title
        H2 title = new H2("Esqueci a Senha");
        title.getStyle()
                .set("color", "#293648")
                .set("text-align", "center")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("margin-top", "0");

        // Add description
        Paragraph description = new Paragraph("Informe seu e-mail para receber um link de redefinição de senha.");
        description.getStyle()
                .set("text-align", "center")
                .set("color", "#6c757d")
                .set("margin-bottom", "5px");

        // Email field
        emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setRequired(true);
        emailField.setErrorMessage("Por favor, insira um email válido");

        // Submit button
        submitButton = new Button("Enviar para email", new Icon(VaadinIcon.ENVELOPE));
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.setWidthFull();
        submitButton.addClickListener(e -> handleForgotPassword());

        // Back to login button
        backToLoginButton = new Button("Voltar para Login", new Icon(VaadinIcon.ARROW_LEFT));
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backToLoginButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        // Add components to container
        container.add(title, description, emailField, submitButton, backToLoginButton);

        // Add back to home button at the top
        HorizontalLayout topBar = new HorizontalLayout(getBackToHomeButton());
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.START);

        add(topBar, container);
    }

    private Div getBackToHomeButton() {
        Div div = new Div();
        div.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("width", "100%")
                .set("padding", "var(--spacing-l)");

        Button btnVoltar = new Button("Voltar para home", new Icon(VaadinIcon.HOME));
        btnVoltar.getStyle().setBoxShadow("rgba(0 0 0 0.1)");
        btnVoltar.addClickListener(event -> UI.getCurrent().navigate("/home"));

        div.add(btnVoltar);
        return div;
    }

    private void handleForgotPassword() {
        String email = emailField.getValue().trim();

        if (email.isEmpty()) {
            Notification notification = Notification.show("Por favor, insira seu email.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Request password reset


        // Always show success message even if email doesn't exist (security best practice)
        Notification notification = Notification.show(
                "Você receberá um link no seu email para redefinir sua senha.",
                5000,
                Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // Clear the field
        emailField.clear();
    }
}