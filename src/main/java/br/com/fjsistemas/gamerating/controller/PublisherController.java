package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.Publisher;
import br.com.fjsistemas.gamerating.repository.PublisherRepository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Component
public class PublisherController extends GenericController<Publisher, Long, PublisherRepository>{

    public PublisherController(PublisherRepository publisherRepository) {
       super(publisherRepository);
    }

}
