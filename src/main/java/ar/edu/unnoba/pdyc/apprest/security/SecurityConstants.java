package ar.edu.unnoba.pdyc.apprest.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
    public static final String SECRET = "AppRestSecretJWTKey";
    public static final long EXPIRATION_TIME = TimeUnit.DAYS.toMillis(10);
    public static final String TOKEN_PREFIX = "JWTPrefix ";
    public static final String HEADER_STRING = "Authorization";
}
