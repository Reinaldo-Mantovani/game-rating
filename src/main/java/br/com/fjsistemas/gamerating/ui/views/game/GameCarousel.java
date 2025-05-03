package br.com.fjsistemas.gamerating.ui.views.game;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

import static com.vaadin.flow.component.ComponentUtil.addListener;
import static com.vaadin.flow.component.ComponentUtil.fireEvent;
import static javax.swing.UIManager.getUI;


@CssImport("/../frontend/themes/default/carousel.css")
public class GameCarousel extends Composite<Div> {

    private final Div carouselContainer = new Div();
    private final Div slidesContainer = new Div();
    private final FlexLayout paginationContainer = new FlexLayout();
    private final Button prevButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));

    private final List<GameBanner> banners = new ArrayList<>();
    private int currentIndex = 0;
    private int totalSlides = 0;

    public GameCarousel() {
        initializeComponent();
        setupEventListeners();
    }

    /**
     * Cria o container do card  .
     *
     * @return
     */
    private void initializeComponent() {
        Div root = getContent();
        root.addClassName("game-carousel");

        carouselContainer.addClassName("carousel-container");
        slidesContainer.addClassName("slides-container");
        paginationContainer.addClassName("pagination-container");

        prevButton.addClassName("nav-button");
        prevButton.addClassName("prev-button");
        nextButton.addClassName("nav-button");
        nextButton.addClassName("next-button");

        carouselContainer.add(slidesContainer, prevButton, nextButton);
        root.add(carouselContainer, paginationContainer);
    }

    /**
     * Configura os listeners de eventos para os botões de navegação.
     */
    private void setupEventListeners() {
        prevButton.addClickListener(e -> {
            currentIndex = (currentIndex - 1 + totalSlides) % totalSlides;
            updateActiveSlide();
        });

        nextButton.addClickListener(e -> {
            currentIndex = (currentIndex + 1) % totalSlides;
            updateActiveSlide();
        });
    }

    /**
     * Adiciona um banner ao carrossel.
     *
     * @param banner O banner a ser adicionado.
     */
    public void addBanner(GameBanner banner) {
        banners.add(banner);
        slidesContainer.add(banner);
        totalSlides++;

        // Create pagination indicator
        Div indicator = new Div();
        indicator.addClassName("pagination-indicator");
        int index = totalSlides - 1;
        indicator.addClickListener(e -> {
            currentIndex = index;
            updateActiveSlide();
        });
        paginationContainer.add(indicator);

        // If this is the first banner, make it active
        if (totalSlides == 1) {
            banner.addClassName("active");
            indicator.addClassName("active");
        }
    }

    private void updateActiveSlide() {
        // Update slides
        for (int i = 0; i < banners.size(); i++) {
            GameBanner banner = banners.get(i);
            if (i == currentIndex) {
                banner.addClassName("active");
            } else {
                banner.removeClassName("active");
            }
        }

        // Update pagination indicators
        for (int i = 0; i < paginationContainer.getComponentCount(); i++) {
            Component indicator = paginationContainer.getComponentAt(i);
            if (i == currentIndex) {
                indicator.getElement().getClassList().add("active");
            } else {
                indicator.getElement().getClassList().remove("active");
            }
        }

        // Fire event
        fireEvent(new SlideChangeEvent(this, currentIndex));
    }

    // Event for slide changes
    public static class SlideChangeEvent extends ComponentEvent<GameCarousel> {
        private final int slideIndex;

        public SlideChangeEvent(GameCarousel source, int slideIndex) {
            super(source, false);
            this.slideIndex = slideIndex;
        }

        public int getSlideIndex() {
            return slideIndex;
        }
    }

    public Registration addSlideChangeListener(ComponentEventListener<SlideChangeEvent> listener) {
        return addListener(SlideChangeEvent.class, listener);
    }

    /**
     * Método para AutoPlay.
     *
     * @param component Autoplay.
     */
    private int autoPlayDelay = 5000; // 5 seconds by default
    private boolean autoPlay = false;
    private Thread autoPlayThread;

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
        if (autoPlay && (autoPlayThread == null || !autoPlayThread.isAlive())) {
            startAutoPlay();
        } else if (!autoPlay && autoPlayThread != null) {
            stopAutoPlay();
        }
    }


    public void setAutoPlayDelay(int milliseconds) {
        this.autoPlayDelay = milliseconds;
    }

    private void startAutoPlay() {
        autoPlayThread = new Thread(() -> {
            while (autoPlay) {
                try {
                    Thread.sleep(autoPlayDelay);
                    getUI().ifPresent(ui -> ui.access(() -> {
                        currentIndex = (currentIndex + 1) % totalSlides;
                        updateActiveSlide();
                    }));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        autoPlayThread.setDaemon(true);
        autoPlayThread.start();
    }

    private void stopAutoPlay() {
        if (autoPlayThread != null) {
            autoPlayThread.interrupt();
            autoPlayThread = null;
        }
    }
}
