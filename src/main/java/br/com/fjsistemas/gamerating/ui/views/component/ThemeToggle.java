package br.com.fjsistemas.gamerating.ui.views.component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Component for toggling between light and dark themes
 */
public class ThemeToggle extends Button {

    private boolean isDarkMode = true;

    public ThemeToggle() {
        setIcon(new Icon(VaadinIcon.MOON));
       // addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addClassName("theme-toggle");

        addClickListener(event -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                setIcon(new Icon(VaadinIcon.SUN_O));
                isDarkMode = false;
            } else {
                themeList.add(Lumo.DARK);
                setIcon(new Icon(VaadinIcon.MOON));
                isDarkMode = true;
            }
        });
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }
}
