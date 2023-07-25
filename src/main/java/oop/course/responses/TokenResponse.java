package oop.course.responses;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;

import java.io.*;
import java.util.*;

public final class TokenResponse implements Response {
    private final String subject;
    private final String secretKey;
    private final long duration;

    public TokenResponse(String subject, String secretKey, long duration) {
        this.subject = subject;
        this.secretKey = secretKey;
        this.duration = duration;

    }

    public TokenResponse(String subject, String secretKey) {
        this(subject, secretKey, 24L * 60 * 60 * 1000);
    }

    @Override
    public void print(PrintWriter out) {
        String token = JWT.create()
            .withSubject(this.subject)
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + this.duration))
            .sign(Algorithm.HMAC256(this.secretKey));

        String body = String.format("{%n\"token\":\"%s\"%n}", token);

        out.println(body);
    }
}
