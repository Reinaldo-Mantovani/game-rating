package br.com.fjsistemas.gamerating.repository;

import br.com.fjsistemas.gamerating.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
