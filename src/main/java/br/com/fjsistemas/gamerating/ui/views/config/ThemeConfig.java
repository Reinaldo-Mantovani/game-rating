package br.com.fjsistemas.gamerating.ui.views.config;


import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Inline;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.context.annotation.Configuration;

/**
 * Theme configuration for the application
 */
@Configuration
public class ThemeConfig implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        // Set dark theme as default
        settings.addMetaTag("theme-color", "#121212");
        //settings.addInlineFromFile("./themes/js/theme-init.js", Inline.Wrapping.valueOf("javascript"));
    }
}
