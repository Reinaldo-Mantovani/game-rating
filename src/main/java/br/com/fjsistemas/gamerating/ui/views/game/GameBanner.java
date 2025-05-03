package br.com.fjsistemas.gamerating.ui.views.game;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class GameBanner extends Div {

    public GameBanner(byte[] imageBytes, String title, String description) {
        addClassName("game-banner");


        // Create and configure image
        Image bannerImage = new Image();
        if (imageBytes != null && imageBytes.length > 0) {
            bannerImage.setSrc("data:image/png;base64," +
                    java.util.Base64.getEncoder()
                            .encodeToString(imageBytes));
        } else {
            bannerImage.setSrc("images/banner-placeholder.png");
        }
        bannerImage.setAlt(title);
        bannerImage.setWidthFull();

        // Create content container for text overlay
        Div contentContainer = new Div();

        contentContainer.addClassName("banner-content");

        // Game title
        H2 gameTitle = new H2(title);
        gameTitle.getStyle()
                .set("margin-top", "0")
                .set("color", "rgba(255, 255, 255, 0.9)")
                .set("font-size", "24px")
                .set("font-weight", "bold");

        // Game description
        Paragraph gameDesc = null;
        if (description != null && !description.isEmpty()) {
            gameDesc = new Paragraph(description);
            gameDesc.getStyle()
                    .set("margin-top", "5px")
                    .set("color", "rgba(255, 255, 255, 0.9)");
        }

        // Add elements to content container
        contentContainer.add(gameTitle);
        if (gameDesc != null) {
            contentContainer.add(gameDesc);
        }

        // Add all components to banner
        add(bannerImage, contentContainer);
    }
}
