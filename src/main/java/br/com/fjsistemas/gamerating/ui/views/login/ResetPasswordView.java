package br.com.fjsistemas.gamerating.ui.views.login;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("reset-password")
@PageTitle("Redefinir Senha")
@CssImport("./../frontend/themes/default/login-view.css")
@AnonymousAllowed
public class ResetPasswordView extends VerticalLayout implements HasUrlParameter<String> {


    private final PasswordField newPasswordField;
    private final PasswordField confirmPasswordField;
    private final Button resetButton;
    private final Button backToLoginButton;
    private final VerticalLayout formLayout;
    private final VerticalLayout invalidTokenLayout;

    private String token;

    @Autowired
    public ResetPasswordView() {

        addClassNames("reset-password-view", LumoUtility.Padding.MEDIUM);
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
        H1 title = new H1("Redefinir Senha");
        title.getStyle()
                .set("color", "#293648")
                .set("text-align", "center")
                .set("margin-top", "0");

        // Form layout (shown when token is valid)
        formLayout = new VerticalLayout();
        formLayout.setPadding(false);
        formLayout.setSpacing(true);

        Paragraph formDescription = new Paragraph("Crie uma nova senha para sua conta.");
        formDescription.getStyle()
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        newPasswordField = new PasswordField("Nova Senha");
        newPasswordField.setWidthFull();
        newPasswordField.setRequired(true);
        newPasswordField.setMinLength(6);
        newPasswordField.setErrorMessage("A senha deve ter pelo menos 6 caracteres");

        confirmPasswordField = new PasswordField("Confirmar Senha");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setRequired(true);

        resetButton = new Button("Redefinir Senha");
        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidthFull();
        resetButton.addClickListener(e -> handleResetPassword());

        formLayout.add(formDescription, newPasswordField, confirmPasswordField, resetButton);

        // Invalid token layout
        invalidTokenLayout = new VerticalLayout();
        invalidTokenLayout.setPadding(false);
        invalidTokenLayout.setSpacing(true);

        Icon errorIcon = new Icon(VaadinIcon.WARNING);
        errorIcon.setSize("48px");
        errorIcon.setColor("var(--lumo-error-color)");

        Paragraph errorMessage = new Paragraph("O link de redefinição de senha é inválido ou expirou. Por favor, solicite um novo link.");
        errorMessage.getStyle()
                .set("text-align", "center")
                .set("margin-bottom", "20px");

        Button requestNewLinkButton = new Button("Solicitar Novo Link");
        requestNewLinkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        requestNewLinkButton.setWidthFull();
        requestNewLinkButton.addClickListener(e -> UI.getCurrent().navigate(ForgotPasswordView.class));

        invalidTokenLayout.add(errorIcon, errorMessage, requestNewLinkButton);
        invalidTokenLayout.setVisible(false); // Initially hidden

        // Back to login button
        backToLoginButton = new Button("Voltar para Login", new Icon(VaadinIcon.ARROW_LEFT));
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backToLoginButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        // Add components to container
        container.add(title, formLayout, invalidTokenLayout, backToLoginButton);

        // Add back to home button at the top
        HorizontalLayout topBar = new HorizontalLayout(getBackToHomeButton());
        topBar.setWidthFull();
        topBar.setJustifyContentMode(JustifyContentMode.START);

        add(topBar, container);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        // Get token from URL parameter or query parameter
        if (parameter != null && !parameter.isEmpty()) {
            token = parameter;
        } else {
            token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of("")).get(0);
        }

        // Validate token
//        if (token == null || token.isEmpty() || !passwordResetService.validatePasswordResetToken(token)) {
//            // Show invalid token message
//            formLayout.setVisible(false);
//            invalidTokenLayout.setVisible(true);
//        } else {
//            // Show reset form
//            formLayout.setVisible(true);
//            invalidTokenLayout.setVisible(false);
//        }
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

    private void handleResetPassword() {
        String newPassword = newPasswordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        // Validate passwords
        if (newPassword.isEmpty() || newPassword.length() < 6) {
            Notification notification = Notification.show("A senha deve ter pelo menos 6 caracteres.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Notification notification = Notification.show("As senhas não coincidem.");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Reset password
        ///boolean resetSuccessful = passwordResetService.resetPassword(token, newPassword);

//        if (resetSuccessful) {
//            Notification notification = Notification.show(
//                    "Senha redefinida com sucesso! Você pode fazer login agora.",
//                    5000,
//                    Notification.Position.MIDDLE);
//            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
//
//            // Redirect to login page after a short delay
//            UI.getCurrent().getPage().executeJs("setTimeout(function() { window.location.href = '/login'; }, 2000);");
//        } else {
//            Notification notification = Notification.show(
//                    "Falha ao redefinir a senha. O link pode ter expirado. Por favor, solicite um novo link.",
//                    5000,
//                    Notification.Position.MIDDLE);
//            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
//
//            // Show invalid token layout
//            formLayout.setVisible(false);
//            invalidTokenLayout.setVisible(true);
//        }
    }
}