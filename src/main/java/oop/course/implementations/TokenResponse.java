package oop.course.implementations;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import oop.course.interfaces.*;
import oop.course.storage.interfaces.*;

import java.io.*;
import java.util.*;

public class TokenResponse implements Response {
    private final Credentials credentials;
    private final String secretKey;

    public TokenResponse(Credentials credentials, String secretKey) {
        this.credentials = credentials;
        this.secretKey = secretKey;
    }

    @Override
    public void print(PrintWriter out) {
        long dayMillis = 24L * 60 * 60 * 1000;
        String token = JWT.create()
                .withSubject(this.credentials.username())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + dayMillis))
                .sign(Algorithm.HMAC256(this.secretKey));

        String body = String.format("{%n\"token\":\"%s\"%n}", token);

        out.println(body);
    }
}
