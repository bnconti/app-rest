package ar.edu.unnoba.pdyc.apprest.dto;

import java.io.Serializable;

/* utilizado para la autenticación de usuarios */
public class UserDTO implements Serializable {
    private Long id;
    private String email;
    private String password;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
