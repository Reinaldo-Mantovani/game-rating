package br.com.fjsistemas.gamerating.restcontroller;

import br.com.fjsistemas.gamerating.controller.UserController;

import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.service.SecurityService;
import br.com.fjsistemas.gamerating.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class RestUserController {

    private final UserService userService;
    UserController controller;

    public RestUserController(UserService userService, UserController controller) {
        this.userService = userService;
        this.controller = controller;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> list = controller.list();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id){
        User userExist = controller.load(id);
        if (userExist == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userExist);

    }


    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user){
        User userExist = controller.loadTwo(user.getId());
        if (userExist != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(null);
        }
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        User existUser = controller.load(id);
        if (existUser == null){
            return ResponseEntity.notFound().build();
        }

        existUser.setUsername(user.getUsername());
        existUser.setName(user.getName());
        existUser.setPassword(user.getPassword());
        existUser.setRole(user.getRole());

        User updatedUser = controller.save(existUser);
        return  new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        User userExist = controller.load(id);
        if (userExist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuário com id: " + id + " não encontrado!");
        }

        controller.delete(userExist);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Usuário com id: " + id + " foi deletado com sucesso.");
    }

}
