//package br.com.fjsistemas.gamerating.service;
//
//
//import br.com.fjsistemas.gamerating.model.User;
//import com.vaadin.flow.server.VaadinSession;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SessionService {
//
//    private static final String CURRENT_USER_KEY = "currentUser";
//    private  int timeout;
//
//    public SessionService(int timeout) {
//        this.timeout = timeout;
//    }
//
//    public User getCurrentUser() {
//        // Tenta pegar da sessão
//        User user = (User) VaadinSession.getCurrent().getAttribute(CURRENT_USER_KEY);
//
//        // Se não estiver em sessão, tenta carregar do Spring Security
//        if (user == null && SecurityContextHolder.getContext().getAuthentication() != null) {
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal instanceof User) {
//                user = (User) principal;
//                setCurrentUser(user); // Salva na sessão
//            }
//        }
//
//        return user;
//    }
//
//    public void setCurrentUser(User user) {
//        VaadinSession.getCurrent().setAttribute(CURRENT_USER_KEY, user);
//    }
//
//    public void clear() {
//        VaadinSession.getCurrent().setAttribute(CURRENT_USER_KEY, null);
//        VaadinSession.getCurrent().close();
//    }
//
//    public boolean isAuthenticated() {
//        return getCurrentUser() != null;
//    }
//
//    // SessionTimeout
//    public void setSessionTimeout(int timeout) {
//        this.timeout = timeout;
//        VaadinSession.getCurrent().getSession();
//    }
//
//
//}
