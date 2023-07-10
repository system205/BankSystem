package oop.course.implementations;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.*;
import oop.course.interfaces.*;

import java.util.*;

// TODO Add proper exceptions
public class HeaderToken implements Id<String> {
    private final String token;

    public HeaderToken(Collection<String> headers) {
        this.token = headers.stream()
                .filter(header -> header.startsWith("Authorization"))
                .map(s -> s.substring("Authorization: Bearer ".length()))
                .findFirst().orElseThrow(() -> new RuntimeException("There is not authorization when performing a transaction"));
    }

    @Override
    public String id() {
        try {
            DecodedJWT decodedJWT = JWT.require(
                            Algorithm.HMAC256("mySecretKey"))
                    .build().verify(this.token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            System.err.println("Issue when verifying token in header. " + e.getMessage());
            throw new RuntimeException("Verification of token in header failed");
        }
    }
}
