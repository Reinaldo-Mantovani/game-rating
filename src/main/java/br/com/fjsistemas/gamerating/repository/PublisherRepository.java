package br.com.fjsistemas.gamerating.repository;

import br.com.fjsistemas.gamerating.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
