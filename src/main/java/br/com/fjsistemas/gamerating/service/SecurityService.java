package br.com.fjsistemas.gamerating.service;

import br.com.fjsistemas.gamerating.model.User;
import br.com.fjsistemas.gamerating.repository.UserRepository;
import com.vaadin.flow.server.VaadinSession;
import br.com.fjsistemas.gamerating.exception.LogoutException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    public SecurityService(HttpServletRequest request, UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.request = request;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public boolean login(String username, String password) {
        try {
            var authRequest = new UsernamePasswordAuthenticationToken(username, password);
            var authentication = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    public void logout() throws LogoutException {
        try {
            request.logout();
            VaadinSession.getCurrent().close();
            SecurityContextHolder.clearContext();
        } catch (ServletException e) {
            throw new LogoutException("Erro ao fazer logout", e);
        }
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                return userRepository.findByUsername(username);
            }
        }
        return null;
    }

}