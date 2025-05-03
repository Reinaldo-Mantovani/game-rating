package br.com.fjsistemas.gamerating.restcontroller;

import br.com.fjsistemas.gamerating.controller.ReviewController;
import br.com.fjsistemas.gamerating.model.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/review")
public class RestReviewController {

    ReviewController controller;

    public RestReviewController(ReviewController controller) {
        this.controller = controller;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAll(){
        List<Review> list = controller.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable Long id){
        Review existReview = controller.load(id);
        if (existReview == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
            controller.load(id);
            return new ResponseEntity<>(existReview, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Review> save(@RequestBody Review review){
        Review newReview = controller.save(review);
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review){
        Review existReview = controller.load(id);
        if (existReview == null){
            return ResponseEntity.notFound().build();
        }
        existReview.setTitle(review.getTitle());
        existReview.setContent(review.getContent());
        existReview.setGame(review.getGame());
        existReview.setPlatform(review.getPlatform());
        existReview.setRating(review.getRating());
        existReview.setUser(review.getUser());

        Review updatedReview = controller.save(existReview);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Review existReview = controller.load(id);
        if (existReview == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Review com id: " + id + " não encontrado!");
        }
            controller.delete(existReview);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Review com id: " + id + " foi deletado com sucesso.");
    }

}
