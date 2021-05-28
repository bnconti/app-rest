package ar.edu.unnoba.pdyc.apprest.dto;

import java.io.Serializable;

/* utilizado para la autenticación de usuarios */
public class UserDto implements Serializable {
    private String email;
    private String password;

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
