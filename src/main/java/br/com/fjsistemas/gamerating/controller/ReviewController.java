package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.Review;
import br.com.fjsistemas.gamerating.repository.ReviewRepository;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReviewController extends GenericController<Review, Long, ReviewRepository> {

    public ReviewController(ReviewRepository reviewRepository) {
        super(reviewRepository);
    }

}
