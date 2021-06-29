package ar.edu.unnoba.pdyc.apprest.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import ar.edu.unnoba.pdyc.apprest.AppRestApplication;
import ar.edu.unnoba.pdyc.apprest.service.UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        /* almacenar las contraseñas encriptadas con BCrypt */
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    /* Overrides de WebSecurityConfigurerAdapter */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* configurar filtros de seguridad */
        http.cors()
            .and()
                .csrf().disable().authorizeRequests()
                /* endpoints públicos */
                .antMatchers(HttpMethod.GET, AppRestApplication.APP_PATH + "/songs").permitAll()
                .antMatchers(HttpMethod.GET, AppRestApplication.APP_PATH + "/playlists").permitAll()
                .antMatchers(HttpMethod.GET, AppRestApplication.APP_PATH + "/playlists/*").permitAll()
                .antMatchers(HttpMethod.GET, AppRestApplication.APP_PATH + "/playlists/*/*").permitAll()
                .antMatchers("/**").fullyAuthenticated()
            .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                /* no crear sesiones en Spring Security */
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /* crear el gestor de autenticación,
         * gestionando usuarios con userService usando BCrypt */
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
