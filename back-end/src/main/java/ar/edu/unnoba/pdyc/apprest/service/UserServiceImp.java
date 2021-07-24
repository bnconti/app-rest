package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.exceptions.UnavailableEmailException;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User create(User user) throws UnavailableEmailException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UnavailableEmailException("Ya existe un usuario registrado con el correo " +
                    user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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
