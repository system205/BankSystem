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
                new Fork(
                        new SimpleUrl(),
                        new MainRoute(),
                        new LoginRoute(
                                connection,
                                new TokenReturn(
                                        "mySecretKey",
                                        24L * 60 * 60 * 1000,
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
                        new ManagerFork( // /manager
                                new CustomerRequestsRoute(
                                        new ListRequests(connection),
                                        new PostRequests(connection)
                                )
                        ),
                        new RequestsRoute(
                                new GetRequests(connection),
                                new PutRequests(connection)
                        ),
                        new JobRoute(
                                new PutOffer(connection)
                        ),
                        new AdminFork( // /admin
                                new ApplicantsRoute( // / offers
                                        new ListApplicants(connection),
                                        new PostOffer(connection)
                                )
                        ),
                        new NotFoundRoute()
                ),
                connection,
                new RolesConfiguration(
                        Map.ofEntries(
                                entry("/manager", List.of("manager", "admin")),
                                entry("/admin", List.of("admin"))
                        )
                )
        );
        final ErrorResponsesProcess errorResponsesProcess = new ErrorResponsesProcess(authorization);
        logger.debug("All processes are created");

        final int port = 6666;
        try (ServerSocket socket = new ServerSocket(port)) {
            logger.info("Server started on port {}", port);
            while (true) {
                logger.debug("Waiting for new client");
                new Thread(
                        new Server(
                                socket.accept(),
                                errorResponsesProcess))
                        .start();
            }
        } catch (IOException e) {
            logger.error("Failed to accept a client", e);
            throw new RuntimeException(e);
        }
    }
}
