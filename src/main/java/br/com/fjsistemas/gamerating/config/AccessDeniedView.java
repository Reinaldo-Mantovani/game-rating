package br.com.fjsistemas.gamerating.config;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("access-denied")
public class AccessDeniedView extends VerticalLayout {

    public AccessDeniedView() {
        add(new H1("Acesso Negado"));
        add(new Paragraph("Você não tem permissão para acessar esta página."));

        // Se quiser, pode adicionar um link para a página principal
        Button backToHomeButton = new Button("Voltar para a página inicial", event ->
                UI.getCurrent().navigate(""));
        add(backToHomeButton);
    }
}
