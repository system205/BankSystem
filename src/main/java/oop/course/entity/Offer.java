package oop.course.entity;

import oop.course.tools.*;
import org.slf4j.*;

public class Offer implements JSON {
    private static final Logger log = LoggerFactory.getLogger(Offer.class);
    private final long id;

    public Offer(long id) {
        this.id = id;
        log.trace("Created offer with id {}", this.id);
    }

    @Override
    public String json() {
        return String.format("{%n\"id\":\"%s\"%n}", this.id);
    }
}
