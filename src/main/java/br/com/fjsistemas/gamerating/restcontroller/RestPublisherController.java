package br.com.fjsistemas.gamerating.restcontroller;

import br.com.fjsistemas.gamerating.controller.PublisherController;
import br.com.fjsistemas.gamerating.model.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/publisher")
public class RestPublisherController {

    private PublisherController controller;

    public RestPublisherController(PublisherController controller) {
        this.controller = controller;
    }

    @GetMapping
    public ResponseEntity<List<Publisher>> getAll(){
        List<Publisher> list = controller.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id){
        Publisher existPublisher = controller.load(id);
        if (existPublisher == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Nenhuma publisher com este id: " + id + " foi encontrado!");
        }
        controller.load(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Publisher com id: "+ id + " encontrado com sucesso!");
    }

    @PostMapping
    public ResponseEntity<Publisher> save(@RequestBody Publisher publisher) {
        Publisher newPublisher = controller.save(publisher);
        return new ResponseEntity<>(newPublisher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher publisher) {
        Publisher existingPublisher = controller.load(id);
        if (existingPublisher == null) {
            return ResponseEntity.notFound().build();
        }
        existingPublisher.setName(publisher.getName());
        existingPublisher.setWebsite(publisher.getWebsite());


        Publisher updatedPublisher = controller.save(existingPublisher);
        return ResponseEntity.ok().body(updatedPublisher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Publisher existPublisher = controller.load(id);
        if (existPublisher == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Publisher não encontrado com id: " + id);
        }

        controller.delete(existPublisher);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Publisher com id: " + id + " foi deletado com sucesso.");
    }

}
