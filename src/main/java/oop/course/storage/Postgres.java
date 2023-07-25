package oop.course.storage;

import oop.course.storage.interfaces.*;
import org.slf4j.*;

import java.sql.*;

public final class Postgres implements Connector {
    private static final Logger log = LoggerFactory.getLogger(Postgres.class);
    private static final String NAME = "postgres";

    private final String ip;
    private final int port;
    private final String databaseName;
    private final String user;
    private final String password;

    /**
     * Primary constructor
     */
    private Postgres(String ip, int port, String user, String password, String databaseName) {
        this.ip = ip;
        this.port = port;
        this.databaseName = databaseName;
        this.user = user;
        this.password = password;
    }

    /**
     * Default connection
     * Try to connect to database postgres on localhost:5432
     * with user=postgres and password=postgres
     */
    public Postgres() {
        this("localhost", 5432, NAME, NAME, NAME);
    }

    /**
     * Full constructor
     */
    public Postgres(NetAddress address, Credentials credentials, String databaseName) {
        this(address.ip(), address.port(), credentials.username(), credentials.password(), databaseName);
    }


    public Connection connect() {
        log.debug("Create postgres connection");
        try {
            Connection connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%s/%s", this.ip, this.port, this.databaseName),
                    this.user,
                    this.password
            );
            connection.setAutoCommit(false);
            log.debug("Turn off auto commit");
            log.info("The connection to database is set up");
            return connection;
        } catch (SQLException e) {
            log.error("Failed to connect to PostgresDB");
            throw new RuntimeException("Database can't set up");
        }
    }
}
