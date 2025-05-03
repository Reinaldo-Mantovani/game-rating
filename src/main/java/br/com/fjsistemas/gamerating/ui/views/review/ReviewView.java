package br.com.fjsistemas.gamerating.ui.views.review;

import br.com.fjsistemas.gamerating.config.AuthenticatedUser;
import br.com.fjsistemas.gamerating.controller.GameController;
import br.com.fjsistemas.gamerating.controller.ReviewController;
import br.com.fjsistemas.gamerating.model.Review;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "reviews", layout = MainLayout.class)
@CssImport("./../frontend/themes/default/reviews-view.css")
@CssImport("./../frontend/themes/default/styles.css")
@RolesAllowed({"EDITOR", "ADM"})
@PageTitle("Reviews | GAMERATING")
public class ReviewView extends VerticalLayout {

    private final ReviewController controller;
    private final GameController gameController;
    private final AuthenticatedUser authenticatedUser;

    public ReviewView(@Autowired ReviewController controller, GameController gameController, AuthenticatedUser authenticatedUser) {
        this.gameController = gameController;
        this.controller = controller;
        this.authenticatedUser = authenticatedUser;

        if (!authenticatedUser.isAuthenticated()) {
            UI.getCurrent().navigate("login");
        }

        addClassName("reviews-view");
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        add(
                createHeader(),
                createMainContent()
        );
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("page-header");
        header.setWidthFull();
        header.setPadding(true);

        Div titleContainer = new Div();
        titleContainer.addClassName("title-container");

        H2 title = new H2("REVIEWS");
        title.addClassName("page-title");

        titleContainer.add(title);

        ComboBox<String> categoryFilter = new ComboBox<>();
        categoryFilter.setItems("Wii", "PlayStation", "Xbox", "Switch");
        categoryFilter.setPlaceholder("Selecione a categoria");
        categoryFilter.addClassName("category-filter");

        header.add(titleContainer, categoryFilter);
        header.setFlexGrow(1, titleContainer);

        return header;
    }

    private Component createMainContent() {
        HorizontalLayout content = new HorizontalLayout();
        content.addClassName("content-layout");
        content.setSizeFull();
        content.setPadding(false);
        content.setSpacing(false);

        VerticalLayout featuredReviews = new VerticalLayout();
        featuredReviews.addClassName("featured-reviews");
        featuredReviews.setPadding(true);
        featuredReviews.setSpacing(true);

        List<Review> reviews = controller.list();

        VerticalLayout reviewsLayout = new VerticalLayout();
        reviewsLayout.addClassName("reviews-layout");
        reviewsLayout.setPadding(false);
        reviewsLayout.setSpacing(true);
        reviewsLayout.setWidthFull();

        reviews.forEach(review -> {
            reviewsLayout.add(createReviewCard(review));
        });

        featuredReviews.add(reviewsLayout);

        VerticalLayout sidebar = new VerticalLayout();

        sidebar.addClassName("sidebar");
        sidebar.setPadding(true);

        Div latestReviews = new Div();
        latestReviews.addClassName("latest-reviews");
        latestReviews.getStyle().set("width", "100%");

        H3 latestReviewsTitle = new H3("ÚLTIMOS REVIEWS");
        latestReviewsTitle.addClassName("section-title");

        List<Review> latest = controller.list();

        VerticalLayout latestReviewsList = new VerticalLayout();
        latestReviewsList.addClassName("latest-reviews-list");
        latestReviewsList.setPadding(false);
        latestReviewsList.setSpacing(false);

        latest.forEach(review -> {
            latestReviewsList.add(createLatestReviewItem(review));
        });

        latestReviews.add(latestReviewsTitle, latestReviewsList);
        sidebar.add(latestReviews);

        content.add(featuredReviews, sidebar);
        content.setFlexGrow(3, featuredReviews);
        content.setFlexGrow(1, sidebar);

        return content;
    }

    private Component createReviewCard(Review review) {
        Div card = new Div();
        card.addClassName("review-card");

        VerticalLayout content = new VerticalLayout();
        content.addClassName("review-content");
        content.setPadding(false);
        content.setSpacing(false);
        content.setWidthFull();

        H4 gameName = new H4( review.getGame().getName());
        gameName.addClassName("game-name");

        H5 title = new H5(review.getTitle());
        title.addClassName("review-title");


        Paragraph description = new Paragraph(review.getContent());
        description.addClassName("review-description");

        HorizontalLayout platformContainer = new HorizontalLayout();
        platformContainer.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("width", "100%")
                .set("align-items", "center");

        Span platform = new Span(review.getPlatform().getDesc());

        platform.addClassName("platform-tag");

        Span datePost = new Span("Postado: " + review.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        datePost.getStyle()
                .set("font-size", "12px")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("color", "gray")
                .set("font-weight", "light");

        platformContainer.add(platform, datePost);
        content.add(gameName,title, description, platformContainer);
        card.add(content);

        return card;
    }

    private Component createLatestReviewItem(Review review) {
        VerticalLayout item = new VerticalLayout();
        item.addClassName("latest-review-item");
        item.setPadding(false);
        item.setSpacing(false);

        H4 gameNameSidebar = new H4(review.getGame().getName());
        gameNameSidebar.addClassName("item-game-name");

        Span title = new Span(review.getTitle());
        title.addClassName("item-title");

        Span platform = new Span(review.getPlatform().getDesc());
        platform.addClassName("item-platform");

        Span user = new Span("Por: " + review.getUser().getUsername());
        user.addClassName("item-user");

        Span date = new Span("Postado: " + review.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.addClassName("item-date");

        item.add(gameNameSidebar ,title, platform, user, date);

        return item;
    }

}