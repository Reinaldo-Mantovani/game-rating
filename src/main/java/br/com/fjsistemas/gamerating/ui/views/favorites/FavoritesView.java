package br.com.fjsistemas.gamerating.ui.views.favorites;

import br.com.fjsistemas.gamerating.model.Favorites;
import br.com.fjsistemas.gamerating.ui.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.Set;

@Route(value = "favorites", layout = MainLayout.class)
@PageTitle("Meus Favoritos")
@RolesAllowed({"EDITOR", "ADM"})
public class FavoritesView extends VerticalLayout {

    private final Set<Favorites> favorites;

    public FavoritesView(Set<Favorites> favorites) {
        this.favorites = favorites;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Meus Jogos Favoritos");
        title.getStyle().set("margin-bottom", "20px");
        add(title);

        if (favorites.isEmpty()) {
            add(new Text("Você ainda não adicionou jogos aos favoritos."));
        } else {
            favorites.forEach(favorite -> {
                Div favoriteCard = new Div();
                favoriteCard.getStyle()
                        .set("padding", "15px")
                        .set("margin", "10px")
                        .set("border", "1px solid #ccc")
                        .set("border-radius", "8px")
                        .set("width", "80%")
                        .set("background-color", "#f9f9f9");

                if (favorite.getFavoriteGames() != null) {
                    favoriteCard.add(new Text("🎮 " + favorite.getUser().getName()));
                } else {
                    favoriteCard.add(new Text("Jogo não disponível."));

                }
                add(favoriteCard);
            });
        }

        Button btnVoltar = new Button("Voltar para Home", event -> {
            getUI().ifPresent(ui -> ui.navigate("/home"));
        });
        add(btnVoltar);
    }
}
