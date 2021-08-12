package ar.edu.unnoba.pdyc.apprest.service;

import ar.edu.unnoba.pdyc.apprest.exceptions.UnavailableEmailException;
import ar.edu.unnoba.pdyc.apprest.model.User;
import ar.edu.unnoba.pdyc.apprest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImp implements UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public User create(User user) throws UnavailableEmailException {
        if (usersRepository.existsByEmail(user.getEmail())) {
            throw new UnavailableEmailException("Ya existe un usuario registrado con el correo " +
                    user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return usersRepository.save(user);
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
