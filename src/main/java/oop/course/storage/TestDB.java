//package oop.course.storage;
//
//import oop.course.entity.*;
//import oop.course.storage.migrations.*;
//import oop.course.tools.implementations.*;
//
//import java.sql.*;
//
//public class TestDB {
//    public static void main(String[] args) throws InterruptedException, SQLException {
//        Connection connection = new Postgres(
//                new SimpleNetAddress("127.0.0.1", 5432),
//                new SimpleCredentials("postgres", "arsen"),
//                "bank"
//        ).connect();
//        connection.setAutoCommit(false);
//
//
//
//        // should be in init phase of an application (before while loop)
//        new DatabaseStartUp(new SimpleSqlExecutor(connection),
//                new CostumerTable(
//                        new TextFromFile("migrations/init-customer-table.sql")
//                ),
//                new CheckingAccountTable(
//                        new TextFromFile("migrations/init-checking-account-table.sql")
//                )
//        ).init();
//
//
////        System.out.println( // Start - retrieve customer with id = 1
////                new Customer(
////                        new CustomerDB(
////                                new Postgres(
////                                        new SimpleNetAddress("127.0.0.1", 5432),
////                                        new SimpleCredentials("postgres", "arsen"),
////                                        "bank"
////                                ).connect(),
////                                "customer"),
////                        1)
////        );
//
//        // Store customer -
//
//
//        connection.close();
//    }
//}
