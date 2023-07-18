package oop.course.implementations;

import oop.course.storage.migrations.*;
import org.slf4j.*;

public class Background implements Initializer {
    private static final Logger log = LoggerFactory.getLogger(Background.class);
    private final Initializer[] inits;

    public Background(Initializer... inits) {
        this.inits = inits;
    }

    @Override
    public void init() {
        for (Initializer task : inits) {
            task.init();
        }
        log.info("Initialized with {} inits", inits.length);
    }
}
