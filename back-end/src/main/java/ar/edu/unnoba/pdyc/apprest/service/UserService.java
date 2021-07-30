package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.exceptions.UnavailableEmailException;
import ar.edu.unnoba.pdyc.apprest.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User create(User user) throws UnavailableEmailException;
    User getByEmail(String email);
    Boolean existsByEmail(String email);
}
