package oop.course;

import oop.course.auth.*;
import oop.course.entity.*;
import oop.course.entity.url.*;
import oop.course.errors.*;
import oop.course.routes.*;
import oop.course.routes.accounts.*;
import oop.course.routes.accounts.methods.*;
import oop.course.routes.admin.*;
import oop.course.routes.admin.applicants.*;
import oop.course.routes.admin.applicants.methods.*;
import oop.course.routes.autopayments.*;
import oop.course.routes.autopayments.methods.*;
import oop.course.routes.job.*;
import oop.course.routes.job.methods.*;
import oop.course.routes.login.*;
import oop.course.routes.main.*;
import oop.course.routes.manager.*;
import oop.course.routes.manager.requests.*;
import oop.course.routes.manager.requests.methods.*;
import oop.course.routes.notFound.*;
import oop.course.routes.register.*;
import oop.course.routes.requests.*;
import oop.course.routes.requests.methods.*;
import oop.course.routes.statement.*;
import oop.course.routes.transactions.*;
import oop.course.routes.transactions.methods.*;
import oop.course.routes.transfer.*;
import oop.course.routes.transfer.methods.*;
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
        // Statistics
        final long startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(
                new Thread(
                        () -> logger.info("App existed {} ms", System.currentTimeMillis() - startTime)
                )
        );

        // Database
        Connection connection = new Postgres(
                new SimpleNetAddress("127.0.0.1", 5432),
                new SimpleCredentials("postgres", "postgres"),
                "bank"
        ).connect();

        // Initialization before startup
        Admin admin = new Admin(connection);
        new Background(
                new DatabaseStartUp(
                        new SimpleSqlExecutor(connection),
                        new MigrationDirectory(
                                "migrations"
                        ).scan()
                ),
                admin
        ).init();

        // Processes
        logger.debug("Start creating processes");

        final ErrorResponsesProcess errorResponsesProcess =
                new ErrorResponsesProcess(
                        new Authorization(
                                new Fork(
                                        new MainRoute(),
                                        new LoginRoute( // /login
                                                connection,
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
                                        new AllAccounts(connection),
                                        new ManagerFork( // /manager
                                                new CustomerRequestsRoute(
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
                                                        new ListApplicants(admin),
                                                        new PostOffer(connection)
                                                )
                                        ),
                                        new NotFoundRoute()
                                ),
                                new GuardedUrl(
                                        connection,
                                        new RolesConfiguration(
                                                Map.ofEntries(
                                                        entry("/manager", List.of("manager", "admin")),
                                                        entry("/admin", List.of("admin"))
                                                )
                                        )
                                )
                        )
                );
        logger.debug("All processes are created");

        // Start server
        final int port = 6666;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port {} in {} ms", port, System.currentTimeMillis() - startTime);

            // Accept clients
            while (true) {
                logger.debug("Waiting for new client");
                new Thread(
                        new Server(
                                serverSocket,
                                errorResponsesProcess)
                ).start();
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
