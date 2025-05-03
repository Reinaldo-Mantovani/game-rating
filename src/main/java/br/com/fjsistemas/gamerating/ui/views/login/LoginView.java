package br.com.fjsistemas.gamerating.ui.views.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationManager authenticationProvider;
    private final LoginForm login;

    public LoginView(AuthenticationManager authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        this.login = new LoginForm();

        login.setAction("login");
        login.addLoginListener(e -> login(e.getUsername(), e.getPassword()));
        login.setForgotPasswordButtonVisible(true);
        login.addForgotPasswordListener(e -> UI.getCurrent().navigate("forgot-password"));

        add(createLoginForm());
    }

    private Component createLoginForm() {
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("login-form");
        layout.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("height", "100vh")
                .set("width", "100%");

        layout.add(login);
        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }

    private void login(String username, String password) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(username, password);
            var authenticated = authenticationProvider.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            UI.getCurrent().navigate("home");

            login.setError(false);
            Notification.show("Login realizado com sucesso!",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (AuthenticationException e) {
            login.setError(true);
            Notification.show("Usuário ou senha incorretos.",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
