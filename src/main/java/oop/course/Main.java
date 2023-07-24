package oop.course;

import oop.course.auth.*;
import oop.course.entity.*;
import oop.course.entity.url.GuardedUrl;
import oop.course.errors.ErrorResponsesProcess;
import oop.course.routes.admin.AdminFork;
import oop.course.routes.admin.routes.applicantsRoute.ApplicantsRoute;
import oop.course.routes.admin.routes.applicantsRoute.methods.ListApplicants;
import oop.course.routes.admin.routes.applicantsRoute.methods.PostOffer;
import oop.course.routes.allAccounts.AllAccounts;
import oop.course.routes.autoPayment.AutoPaymentRoute;
import oop.course.routes.checkAccount.CheckAccountRoute;
import oop.course.routes.checkAccount.methods.DeleteAccount;
import oop.course.routes.checkAccount.methods.GetAccount;
import oop.course.routes.checkAccount.methods.PutAccount;
import oop.course.routes.job.JobRoute;
import oop.course.routes.job.methods.PutOffer;
import oop.course.routes.login.LoginRoute;
import oop.course.routes.login.TokenReturn;
import oop.course.routes.main.MainRoute;
import oop.course.routes.autoPayment.methods.*;
import oop.course.routes.manager.ManagerFork;
import oop.course.routes.manager.routes.customerRequests.CustomerRequestsRoute;
import oop.course.routes.manager.routes.customerRequests.methods.ListRequests;
import oop.course.routes.manager.routes.customerRequests.methods.PostRequests;
import oop.course.routes.notFound.NotFoundRoute;
import oop.course.routes.register.RegisterRoute;
import oop.course.routes.requests.RequestsRoute;
import oop.course.routes.requests.methods.GetRequests;
import oop.course.routes.requests.methods.PutRequests;
import oop.course.routes.statement.StatementRoute;
import oop.course.routes.transactions.TransactionsRoute;
import oop.course.routes.transactions.methods.GetTransactions;
import oop.course.routes.transfer.methods.MakeTransaction;
import oop.course.routes.transfer.TransferRoute;
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
        final RolesConfiguration rolesConfiguration = new RolesConfiguration(
                Map.ofEntries(
                        entry("/manager", List.of("manager", "admin")),
                        entry("/admin", List.of("admin"))
                )
        );
        final ErrorResponsesProcess errorResponsesProcess =
                new ErrorResponsesProcess(
                        new Authorization(
                                new Fork(
                                        new GuardedUrl(connection, rolesConfiguration),
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
                                                        new ListApplicants(connection),
                                                        new PostOffer(connection)
                                                )
                                        ),
                                        new NotFoundRoute()
                                ),
                                connection,
                                rolesConfiguration
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
                                errorResponsesProcess))
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
