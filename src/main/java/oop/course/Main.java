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

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException, SQLException {
//        System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {try {return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();} catch (IOException e) {throw new RuntimeException(e);}}).reduce(Long::sum).orElse(0L));

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
                Optional.of(new Fork(
                        new SimpleUrl(),
                        new MainRoute(),
                        new LoginRoute(
                                new CredentialsAccess(
                                        new DBLoginCheck(connection),
                                        new TokenReturn("mySecretKey")
                                )
                        ),
                        new RegisterRoute(
                                connection
                        ),
                        new TransferRoute(new MakeTransaction(
                                connection)
                        ),
                        new CheckAccountRoute(
                                new GetAccount(
                                        connection
                                ),
                                new PutAccount(
                                        connection
                                )
                        ),
                        new NotFoundRoute()
                )));
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
