package br.com.fjsistemas.gamerating.ui.views;

import br.com.fjsistemas.gamerating.config.AuthenticatedUser;
import br.com.fjsistemas.gamerating.controller.GameController;


import br.com.fjsistemas.gamerating.exception.LogoutException;
import br.com.fjsistemas.gamerating.service.SecurityService;
import br.com.fjsistemas.gamerating.ui.views.component.ThemeToggle;
import br.com.fjsistemas.gamerating.ui.views.favorites.FavoritesView;
import br.com.fjsistemas.gamerating.ui.views.game.GameView;
import br.com.fjsistemas.gamerating.ui.views.home.HomeView;
import br.com.fjsistemas.gamerating.ui.views.publisher.PublisherView;
import br.com.fjsistemas.gamerating.ui.views.review.ReviewView;
import br.com.fjsistemas.gamerating.ui.views.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import org.springframework.security.core.userdetails.UserDetailsService;


@CssImport("./../frontend/themes/default/styles.css")
@CssImport("./../frontend/themes/default/resposive-layout.css")
@JavaScript("./../frontend/themes/default/js/responsive-menu.js")
@PermitAll
public class MainLayout extends AppLayout {

    private static final Logger log = LoggerFactory.getLogger(MainLayout.class);
    private H1 viewTitle;
    private final GameController controller;
    private final ThemeToggle themeToggle;
    private HorizontalLayout navMenu;
    private HorizontalLayout mobileBottomNav;
    private final SecurityService securityService;

    private final UserDetailsService userDetailsService;
    private final AuthenticatedUser authenticatedUser;
    //private final SessionService sessionService;


    public MainLayout(@Autowired GameController controller, SecurityService securityService,
                      UserDetailsService userDetailsService, AuthenticatedUser authenticatedUser) {
        this.controller = controller;
        this.securityService = securityService;
        this.userDetailsService = userDetailsService;
        this.authenticatedUser = authenticatedUser;
        this.themeToggle = new ThemeToggle();


        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());

        addToNavbar(false, mobileBottomNav);

        initResponsiveBehavior();
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");
        toggle.addClassName("menu-toggle");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getStyle()
                .setBackground("#000")
                .set("display", "flex")
                .set("padding", "10px")
                .set("align-items", "center")
                .set("justify-content","center");

        layout.getStyle().setPadding("10px");
        layout.setWidthFull();
        layout.setHeightFull();
        layout.setSpacing(false);
        layout.setJustifyContentMode(JustifyContentMode.START);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.START);
        layout.addClassName("header-layout");

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.START);
        logoLayout.setJustifyContentMode(JustifyContentMode.START);
        logoLayout.addClassName("logo-layout");

        Image logo = new Image();
        logo.setSrc("./images/logo.png");
        logo.addClassNames("logo-image");
        logoLayout.add(logo);


        layout.add(toggle, logoLayout);
        return layout;
    }

    private static TextField getSearchField() {
        // Search field
        TextField searchField = new TextField();
        searchField.setPlaceholder("Buscar");
        searchField.getStyle().setColor("#253244");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.addClassNames("search-field");
        return searchField;
    }

    private static Button btnLogin() {
        // Login button
        Button btnLogin = new Button("Login");
        btnLogin.getStyle().setColor("#373737");
        btnLogin.getStyle().setBackground("white");
        btnLogin.addClickListener(event -> {
            UI.getCurrent().navigate("/login");
        });

        btnLogin.addClassName("register-button");
        return btnLogin;
    }

    private static Button btnLogout(SecurityService securityService) {
        // Login button
        Button btnLogout = new Button("Logout");
        btnLogout.getStyle().setColor("#373737");
        btnLogout.getStyle().setBackground("white");
        btnLogout.addClickListener(event -> {
            try {
                securityService.logout();
            } catch (LogoutException e) {
                throw new RuntimeException(e);
            }
        });

        btnLogout.addClassName("register-button");
        return btnLogout;
    }


    private Button getButtonTheme() {
        // Theme toggle button
        Button themeButton = new Button();
        themeButton.addClassName("theme-button");
        themeButton.getStyle().setColor("#373737");
        themeButton.getStyle().setBackground("white");
        updateThemeButtonIcon(themeButton);

        themeButton.addClickListener(e -> {
            toggleTheme();
            updateThemeButtonIcon(themeButton);
        });
        return themeButton;
    }

    private Component createDrawerContent() {
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.addClassName("drawer-content");
        drawerLayout.getStyle()
                .set("display", "block")
                .set("border-left", "none")
                .set("height", "100%");
        drawerLayout.setSpacing(false);
        drawerLayout.setPadding(true);

        drawerLayout.add(getCurrentUsername());
        drawerLayout.add(getSearchField());

        drawerLayout.add(
                createDrawerNavLink("Home", HomeView.class, VaadinIcon.HOME),
                createDrawerNavLink("Games", GameView.class, VaadinIcon.GAMEPAD),
                createDrawerNavLink("Reviews", ReviewView.class, VaadinIcon.STAR),
                createDrawerNavLink("Editoras", PublisherView.class, VaadinIcon.BUILDING),
                createDrawerNavLink("Favoritos", FavoritesView.class, VaadinIcon.BOOKMARK),
                createDrawerNavLink("Usuários", UserView.class, VaadinIcon.USERS)
        );
        drawerLayout.add(getButtonTheme());

        if (authenticatedUser.isAuthenticated()) {
            drawerLayout.add(btnLogout(securityService));
        } else {
            drawerLayout.add(btnLogin());
        }

        return drawerLayout;
    }

    /*
     * Metodo para pegar o nome do usuario logado
     */
    private Component getCurrentUsername() {
        String username = securityService.getAuthenticatedUser().get().getUsername();
        Div usernameDiv = new Div();
        usernameDiv.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("gap", "0.2rem")
                .set("margin-bottom", "1rem");
        Span hello = new Span("Olá, ");
        hello.getStyle()
                .set("font-size", "20px")
                .set("font-weight", "bold")
                .set("color", "#373737");
        Span usernameSpan = new Span(username);
        usernameSpan.getStyle()
                .set("font-size", "20px")
                .set("font-weight", "bold")
                .set("color", "#013483");
        usernameSpan.addClassName("username");
        usernameDiv.add(hello, usernameSpan);
        return usernameDiv;
    }

    private Component createDrawerNavLink(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {

        RouterLink link = new RouterLink(navigationTarget);
        link.addClassName("drawer-nav-item");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);

        Icon menuIcon = new Icon(icon);
        menuIcon.addClassName("drawer-icon");

        Span menuText = new Span(text);
        menuText.addClassName("drawer-text");

        layout.add(menuIcon, menuText);
        link.add(layout);

        return link;
    }

    private Component createBottomNavItem(String text, Class<? extends Component> navigationTarget, VaadinIcon icon) {
        RouterLink link = new RouterLink(navigationTarget);
        link.addClassName("bottom-nav-item");

        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(false);
        layout.setPadding(false);

        Icon menuIcon = new Icon(icon);
        menuIcon.addClassName("bottom-nav-icon");

        Span menuText = new Span(text);
        menuText.addClassName("bottom-nav-text");

        layout.add(menuIcon, menuText);
        link.add(layout);

        return link;
    }

    private void toggleTheme() {
        boolean isDark = getElement().getThemeList().contains(Lumo.DARK);
        if (isDark) {
            getElement().getThemeList().remove(Lumo.DARK);
        } else {
            getElement().getThemeList().add(Lumo.DARK);
        }

        // Save theme preference to local storage
        getUI().ifPresent(ui -> {
            ui.getPage().executeJs(
                    "localStorage.setItem('gamerating-theme', $0 ? 'dark' : 'light')",
                    !isDark
            );
        });
    }

    private void updateThemeButtonIcon(Button button) {
        boolean isDark = getElement().getThemeList().contains(Lumo.DARK);

        if (isDark) {
            // Show sun icon for light mode option
            button.setIcon(new Icon(VaadinIcon.SUN_O));
            button.setText("Light");
            button.getStyle()
                    .set("width","100%")
                    .setColor("#253244")
                    .setBackground("#fff");
            button.addClassName("Light");
        } else {
            // Show moon icon for dark mode option
            button.setIcon(new Icon(VaadinIcon.MOON_O));
            button.setText("Dark");
            button.getStyle()
                    .set("width","100%")
                    .setColor("white")
                    .setBackground("#253244");
            button.addClassName("Dark");
        }
    }

    private RouterLink createNavLink(String text, Class<? extends Component> navigationTarget) {
        RouterLink link = new RouterLink(text, navigationTarget);
        link.addClassName("nav-item");
        link.getStyle().setColor("white");
        return link;
    }

    private void initResponsiveBehavior() {
        // Get the current UI and page
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            int screenWidth = details.getScreenWidth();
            updateResponsiveLayout(screenWidth);

        });

        // Add resize listener
        UI.getCurrent().getPage().addBrowserWindowResizeListener(event -> {
            updateResponsiveLayout(event.getWidth());
        });

        // Add JavaScript to handle mobile navigation
        UI.getCurrent().getPage().executeJs(
                "window.addEventListener('vaadin-router-location-changed', function() { " +
                        "  const drawer = document.querySelector('vaadin-drawer-toggle').parentNode.parentNode;" +
                        "  if (drawer.getAttribute('aria-hidden') !== 'true') {" +
                        "    drawer.click();" +
                        "  }" +
                        "});"
        );
    }

    private void updateResponsiveLayout(int screenWidth) {
        boolean isMobile = screenWidth <= 768;

        // Update visibility of elements based on screen size
        if (navMenu != null) {
            navMenu.setVisible(!isMobile);
        }

        if (mobileBottomNav != null) {
            mobileBottomNav.setVisible(isMobile);
        }

        // Add or remove mobile class to the main layout
        if (isMobile) {
            addClassName("mobile-layout");
        } else {
            removeClassName("mobile-layout");
        }
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Highlight the active link
        String currentRoute = getContent().getClass().getName();

        getElement().getChildren()
                .filter(element -> element.getTag().equals("a"))
                .forEach(link -> {
                    if (link.getProperty("href", "").contains(currentRoute)) {
                        link.getClassList().add("active-link");
                    } else {
                        link.getClassList().remove("active-link");
                    }
                });

        // Also highlight active link in mobile bottom nav
        if (mobileBottomNav != null) {
            mobileBottomNav.getChildren()
                    .forEach(component -> {
                        if (component instanceof RouterLink) {
                            RouterLink link = (RouterLink) component;
                            if (link.getHref().contains(currentRoute)) {
                                link.addClassName("active-bottom-nav");
                            } else {
                                link.removeClassName("active-bottom-nav");
                            }
                        }
                    });
        }
    }
}
