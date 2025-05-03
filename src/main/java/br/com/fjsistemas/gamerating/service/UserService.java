package br.com.fjsistemas.gamerating.service;

import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }
    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    UserDetailsManager userDetailsManager;
    public UserService(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;

    }


    public void deleteUser(String username) {
        userDetailsManager.deleteUser(username);
    }

    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }
}
