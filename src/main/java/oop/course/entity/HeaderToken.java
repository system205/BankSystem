package oop.course.entity;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.*;
import oop.course.errors.exceptions.*;
import org.slf4j.*;

import java.util.*;

public class HeaderToken implements Id<String> {
    private final Logger log = LoggerFactory.getLogger(HeaderToken.class);
    private final String token;

    public HeaderToken(Collection<String> headers) throws Exception {
        this.token = headers.stream()
                .filter(header -> header.startsWith("Authorization"))
                .map(s -> s.substring("Authorization: Bearer ".length()))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("There is not authorization when performing a transaction"));
    }

    @Override
    public String id() throws Exception {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("mySecretKey"))
                    .build().verify(this.token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            log.error("Issue when verifying token in header");
            throw new AuthorizationException("Verification of token in header failed: " + e.getMessage());
        }
    }
}
