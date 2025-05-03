package br.com.fjsistemas.gamerating.repository;

import br.com.fjsistemas.gamerating.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
