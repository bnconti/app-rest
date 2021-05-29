package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /* Override de UserDetailsService */
    @Override
    /* Username = email */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("No se encontró el usuario " + email);
        }
    }
}
