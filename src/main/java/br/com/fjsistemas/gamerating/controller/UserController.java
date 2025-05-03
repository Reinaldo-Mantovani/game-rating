package br.com.fjsistemas.gamerating.controller;

import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.repository.SecurityRepository;
import br.com.fjsistemas.gamerating.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class UserController extends GenericController<User, Long, UserRepository> {

    private final SecurityRepository securityRepository;

    public UserController(UserRepository userRepository, SecurityRepository securityRepository) {
        super(userRepository);
        this.securityRepository = securityRepository;
    }

    public List<User> list() {
        return repository.findAllWithContacts();
    }

    public Optional<Optional<User>> findByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username));
    }

   public User createUser(User user) {

       return securityRepository.save(user);
   }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(repository.findByEmail(email));
    }

}
