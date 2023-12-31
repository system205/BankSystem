package oop.course.entity;

import oop.course.errors.exceptions.*;
import org.slf4j.*;

import java.sql.*;
import java.util.*;

public final class Manager {
    private static final Logger log = LoggerFactory.getLogger(Manager.class);
    private final Connection connection;

    public Manager(Connection connection) {
        this.connection = connection;
    }

    public List<CustomerRequest> requests() throws Exception {
        log.debug("Manager retrieves requests from the database");
        final String sql = "SELECT id FROM requests WHERE status = 'pending'";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

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
            throw new InternalErrorException(e);
        }
    }
}
