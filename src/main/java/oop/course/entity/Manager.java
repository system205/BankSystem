package oop.course.entity;

import org.slf4j.*;

import java.sql.*;
import java.util.*;

public class Manager {
    private static final Logger log = LoggerFactory.getLogger(Manager.class);
    private final String email;
    private final Connection connection;

    public Manager(String id, Connection connection) {
        this.email = id;
        this.connection = connection;
    }

    public List<CustomerRequest> requests() {
        log.debug("Manager retrieves requests from the database");
        final String sql = "SELECT id FROM requests";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            log.debug("Executing: {}", sql);
            List<CustomerRequest> requests = new LinkedList<>();
            while (resultSet.next())
                requests.add(
                        new CustomerRequest(
                                resultSet.getLong(1),
                                this.connection
                        )
                );
            log.info("Found {} requests.", requests.size());
            return requests;
        } catch (SQLException e) {
            log.error("Error when retrieving customer requests");
            throw new RuntimeException(e);
        }
    }
}
