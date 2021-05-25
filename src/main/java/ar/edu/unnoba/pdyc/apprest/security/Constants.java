package ar.edu.unnoba.pdyc.apprest.security;

/**
 * Created by jpgm on 11/05/21.
 */
public class Constants {
    public static final String SECRET = "AppRestJWTSecret";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
