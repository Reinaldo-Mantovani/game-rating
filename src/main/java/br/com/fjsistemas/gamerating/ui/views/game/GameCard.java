package br.com.fjsistemas.gamerating.ui.views.game;

import br.com.fjsistemas.gamerating.enuns.Platform;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;


@CssImport("/../frontend/themes/default/styles.css")
public class GameCard extends Div {

    private final String title;
    private final byte[] imageUrl;
    private final LocalDate createdAt;
    private final String website;
    private final Set<Platform> platform;

    public GameCard(byte[] imageUrl, String title, LocalDate createdAt, String website, Set<Platform> platform) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.website = website;
        this.platform = platform;

        addClassName("game-card");
        setWidth("300px");
        setHeight("350px");
        getStyle().set("position", "relative");
        getStyle().set("overflow", "hidden");
        getStyle().set("box-shadow"," 0 2px 8px rgba(0, 0, 0, 0.3)");
        getStyle().set("border-radius", "4px");
        getStyle().set("transition", "transform 0.3s ease");
        getStyle().set("cursor", "pointer");

        // Hover effect
        getElement().addEventListener("mouseover", e ->
                getStyle().set("transform", "scale(1.03)"));
        getElement().addEventListener("mouseout", e ->
                getStyle().set("transform", "scale(1)"));

        add(createCardContent());
    }


    /**
     * Cria o container do card .
     *
     * @return O conteudo do card.
     */
    private Component createCardContent() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setMargin(false);

        // Game image
        Image gameImage = new Image(Arrays.toString(imageUrl), title);
        gameImage.setSrc("data:image/png;base64," + Base64.getEncoder().encodeToString(imageUrl));
        gameImage.setWidth("100%");
        gameImage.setHeight("150px");
        gameImage.getStyle().set("object-fit", "cover");


        // Game info section
        Div infoSection = new Div();
        infoSection.getStyle()
                .set("display", "flex")
                .set("width", "95%")
                .set("flex-direction","column")
                .set("padding", "12px");

        Div divTitle = new Div();
        divTitle.getStyle()
                .set("display", "flex")
                .set("align-items","center")
                .set("justify-content","space-between")
                .set("text-align","center");


        // Game title
        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("display", "block")
                .set("font-weight", "light")
                .set("margin-bottom", "8px")
                .set("white-space", "nowrap")
                .set("overflow", "hidden")
                .set("text-overflow", "ellipsis");


        // Usa diretamente os campos da classe
        String platformsText = this.platform.stream()
                .map(Platform::getDesc)
                .collect(Collectors.joining(", "));

        Span spanPlatform = new Span(platformsText);
        spanPlatform.getStyle()
                .set("display","flex")
                .set("font-size", "11px")
                .set("align-items","end")
                .set("flex-direction","flex-end")
                .set("color","gray")
                .set("padding","5px")
                .set("border"," 1px solid #555555 ")
                .set("border-radius","4px")
                .set("background","transparent");

        divTitle.add(titleSpan, spanPlatform);

        /**
         * Cria o lyout com as informações .
         * @return O conteudo com as informações.
         */
        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.setSpacing(true);
        infoLayout.setPadding(false);
        infoLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        Span date = new Span("Postado: " + createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.getStyle()
                .set("font-weight", "light")
                .set("font-size", "12px");

        Span site = new Span("site: " + website);
        site.getStyle()
                .set("font-weight", "light")
                .set("font-size", "12px");

        infoLayout.add(site, date);
        infoLayout.getStyle()
                .set("display", "flex")
                .set("flex-direction","column")
                .set("align-items","start");

        // Buttons card
        Div btnLinks = new Div();
        btnLinks.addClassName("btn-Links");
        btnLinks.getStyle()
                .set("display", "flex")
                .set("width", "94%")
                .set("justify-content","center")
                .set("align-items","center")
                .set("padding","10px")
                .set("margin-top","5px");


        Button btnByNow = new Button("Saiba mais");
        btnByNow.getStyle()
                .set("display", "flex")
                .set("align-items","center")
                .set("justify-content","center")
                .set("width", "100%")
                .set("padding","10px")
                .set("color","#013483")
                .set("cursor","pointer")
                .set("border"," 1px solid #013483 ")
                .set("border-radius","4px")
                .set("background-color","transparent");

        btnLinks.add(btnByNow);

        infoSection.add(divTitle, infoLayout);

        layout.add(gameImage, infoSection, btnLinks);

        RouterLink link = new RouterLink("", GameDetailsView.class, title);
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("color", "inherit");
        link.add(layout);

        return link;
    }
}