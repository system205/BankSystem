package oop.course;

import oop.course.auth.*;
import oop.course.entity.*;
import oop.course.implementations.*;
import oop.course.routes.*;
import oop.course.routes.methods.*;
import oop.course.server.*;
import oop.course.storage.*;
import oop.course.storage.migrations.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

import static java.util.Map.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> logger.info("App existed {} ms", System.currentTimeMillis() - startTime)
                )
        );

        Connection connection = new Postgres(
                new SimpleNetAddress("127.0.0.1", 5432),
                new SimpleCredentials("postgres", "postgres"),
                "bank"
        ).connect();

        new Background(
                new DatabaseStartUp(
                        new SimpleSqlExecutor(connection),
                        new MigrationDirectory(
                                "migrations"
                        ).scan()
                ),
                new Admin(connection)
        ).init();

        // Processes
        logger.debug("Start creating processes");
        final Authorization authorization = new Authorization(
                Optional.of(
                        new Fork(
                                new SimpleUrl(),
                                new MainRoute(),
                                new LoginRoute( // /login
                                        new TokenReturn(
                                                "mySecretKey",
                                                24L * 60 * 60 * 1000,
                                                connection
                                        )
                                ),
                                new RegisterRoute( // /register
                                        connection
                                ),
                                new TransferRoute( // /transfer
                                        new MakeTransaction(
                                                connection
                                        )
                                ),
                                new CheckAccountRoute( // /account
                                        new GetAccount(
                                                connection
                                        ),
                                        new PutAccount(
                                                connection
                                        ),
                                        new DeleteAccount(
                                                connection
                                        )
                                ),
                                new TransactionsRoute( // /transactions
                                        new GetTransactions(
                                                connection
                                        )
                                ),
                                new StatementRoute( // /stats
                                        connection
                                ),
                                new AutoPaymentRoute( // /autopayments
                                        new ListAutoPayments(connection),
                                        new PostAutoPayment(connection),
                                        new DeleteAutoPayment(connection)
                                ),
                                new AllAccounts(connection), // /accounts
                                new ManagerFork( // /manager
                                        new CustomerRequestsRoute( // /requests
                                                new ListRequests(connection),
                                                new PostRequests(connection)
                                        )
                                ),
                                new RequestsRoute( // /requests
                                        new GetRequests(connection),
                                        new PutRequests(connection)
                                ),
                                new JobRoute( // /job
                                        new PutOffer(connection)
                                ),
                                new AdminFork( // /admin
                                        new ApplicantsRoute( // / offers
                                                new ListApplicants(connection),
                                                new PostOffer(connection)
                                        )
                                ),
                                new NotFoundRoute() // any other
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
            logger.info("Server started on port {} in {} ms", port, System.currentTimeMillis() - startTime);
            while (true) {
                logger.debug("Waiting for new client");
                new Thread(
                        new Server(
                                socket,
                                authorization))
                        .start();
            }
        } catch (IOException e) {
            logger.error("Failed to create a server socket", e);
            System.exit(2);
        } finally {
            logger.error("The app crashed");
            System.exit(1);
        }
    }
}
