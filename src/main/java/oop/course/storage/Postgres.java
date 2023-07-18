package oop.course.storage;

import oop.course.exceptions.InternalErrorException;
import oop.course.storage.interfaces.*;

import java.sql.*;

public class Postgres implements Connector {
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
        try {
            return DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%s/%s", this.ip, String.valueOf(this.port), this.databaseName),
                    this.user,
                    this.password);
        } catch (SQLException e) {
            final String error = "Failed to connect to PostgresDB. Internal error: " + e;
            System.err.println(error);
            throw new RuntimeException(e);
        }

    }
}
