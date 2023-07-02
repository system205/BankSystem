package oop.course.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import oop.course.auth.interfaces.SecurityConfiguration;

public class AuthSecurityConfiguration implements SecurityConfiguration {
    private final String secretKey;

    public AuthSecurityConfiguration(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isAccessibleUrl(String url) {
        // Check whether the user is allowed to access the specified URL
        return url.endsWith("/login") || url.endsWith("/register");
    }

    @Override
    public boolean isValidToken(String token, String url) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).withIssuer("Test").build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            String username = decodedJWT.getSubject();
            // TODO - fetch user role from database
            // TODO - check whether the user role is correct for this url
            return true;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
