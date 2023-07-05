package oop.course;

import oop.course.auth.*;
import oop.course.implementations.*;
import oop.course.routes.*;
import oop.course.server.*;
import oop.course.storage.*;
import oop.course.storage.migrations.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import static java.util.Map.entry;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, SQLException {
        logger.debug("Create postgres connection");
        Connection connection = new Postgres(
                new SimpleNetAddress("127.0.0.1", 5432),
                new SimpleCredentials("postgres", "postgres"),
                "bank"
        ).connect();
        logger.debug("Turn off auto commit");
        connection.setAutoCommit(false);
        logger.info("The connection to database is set up");

        new DatabaseStartUp(new SimpleSqlExecutor(connection),
                new MigrationDirectory("migrations")
                        .scan()
        ).init();

        // Processes
        logger.debug("Start creating processes");
        final Authorization authorization = new Authorization(
                Optional.of(
                        new Fork(
                                new SimpleUrl(),
                                new MainRoute(),
                                new LoginRoute(
                                        new TokenReturn(
                                                "mySecretKey",
                                                24 * 60 * 60 * 1000,
                                                connection
                                        )
                                ),
                                new RegisterRoute(
                                        connection
                                ),
                                new TransferRoute(
                                        new MakeTransaction(
                                                connection
                                        )
                                ),
                                new CheckAccountRoute(
                                        new GetAccount(
                                                connection
                                        ),
                                        new PutAccount(
                                                connection
                                        )
                                ),
                                new AllAccounts(connection),
                                new NotFoundRoute()
                        )
                ),
                new AuthSecurityConfiguration(
                        connection,
                        new RolesConfiguration(
                                Map.ofEntries(
                                        entry("/login", List.of("customer", "admin")),
                                        entry("/admin", List.of("admin"))
                                )
                        )
                )
        );
        logger.debug("All processes are created");

        final int port = 6666;
        try (ServerSocket socket = new ServerSocket(port)) {
            logger.info("Server started on port {}", port);
            while (true) {
                logger.debug("Waiting for new client");
                new Thread(
                        new Server(
                                socket.accept(),
                                authorization))
                        .start();
            }
        } catch (IOException e) {
            logger.error("Failed to accept a client", e);
            throw new RuntimeException(e);
        }
    }
}
