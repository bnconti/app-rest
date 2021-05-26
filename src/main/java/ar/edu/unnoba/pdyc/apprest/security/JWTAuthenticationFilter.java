package ar.edu.unnoba.pdyc.apprest.security;

import ar.edu.unnoba.pdyc.apprest.dto.UserDto;
import ar.edu.unnoba.pdyc.apprest.model.User;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static ar.edu.unnoba.pdyc.apprest.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    /* procesar autenticaciones */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserDto auth = new ObjectMapper()
                    .readValue(request.getInputStream(), UserDto.class);

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    auth.getEmail(), auth.getPassword(), new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    /* autenticación exitosa */
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = JWT.create()
                /* poner email en el payload del token */
                .withSubject(((User) authResult.getPrincipal()).getEmail())
                /* expira en EXPIRATION_TIME */
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                /* firmar con HMAC512 usando la semilla SECRET */
                .sign(HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    }
}
