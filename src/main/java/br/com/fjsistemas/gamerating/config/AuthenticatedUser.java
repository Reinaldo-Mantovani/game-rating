package br.com.fjsistemas.gamerating.config;

import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getAuthenticatedUser () {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                System.out.println("Usuário autenticado: " + userDetails.getUsername());
                return userRepository.findByUsername(userDetails.getUsername());
            }
        }
        System.out.println("Nenhum usuário autenticado.");
        return Optional.empty();
    }

  public boolean isAuthenticated() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      return authentication != null && authentication.isAuthenticated();
  }




    public boolean hasRole(String roleName) {
        Optional<User> user = getAuthenticatedUser();
        return user.isPresent() && user.get().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(roleName));
    }

}
