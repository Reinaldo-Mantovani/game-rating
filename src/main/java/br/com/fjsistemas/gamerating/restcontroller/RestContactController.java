package br.com.fjsistemas.gamerating.restcontroller;

import br.com.fjsistemas.gamerating.controller.ContactController;
import br.com.fjsistemas.gamerating.model.Contact;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contact")
public class RestContactController {

    ContactController controller;

    public RestContactController(ContactController controller){
        this.controller = controller;
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAll(){
       List<Contact> list = controller.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(@PathVariable Long id) {
        Contact existContact = controller.load(id);
        if (existContact == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
            controller.load(id);
            return ResponseEntity.status(HttpStatus.OK)
                .body("Contato com id: "+ id +
                        " encontrado com sucesso!"+ "\n" +
                        "Tipo de contato: " + existContact.getType().getDesc()+ "\n" +
                        "Contato: " + existContact.getValue()

                );
    }

    @PostMapping
    public ResponseEntity<Contact> save(@RequestBody Contact contact){
        Contact newContact =  controller.save(contact);
        return new ResponseEntity<>(newContact, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateConatc(@PathVariable Long id, @RequestBody Contact contact){
        Contact existContact = controller.load(id);
        if(existContact == null) {
            return ResponseEntity.notFound().build();
        }
        existContact.setType(contact.getType());
        existContact.setValue(contact.getValue());

        Contact updatedContact = controller.save(existContact);
        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        Contact existContact = controller.load(id);
        if (existContact == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Contato com id: " + id + " não encontrado!");
        }

        controller.delete(existContact);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Contato com id: " + id + " foi deletado com sucesso.");
    }
}
