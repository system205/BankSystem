package oop.course.storage;

import oop.course.storage.migrations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public final class SimpleSqlExecutor implements SqlExecutor {
    private static final Logger log = LoggerFactory.getLogger(SimpleSqlExecutor.class);

    private final Connection connection;

    public SimpleSqlExecutor(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void perform(String sql) {
        try (Statement statement = this.connection.createStatement()) {
            log.debug("Start executing: {}", sql);
            statement.execute(sql);
            this.connection.commit();
            log.debug("Committed");
        } catch (SQLException e) {
            log.error("SQL exception when executing: {}", sql);
            try {
                this.connection.rollback();
            } catch (SQLException ex) {
                log.error("Failed to rollback after commit.");
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
