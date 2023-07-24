package oop.course.storage.migrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseStartUp implements Initializer {

    private static final Logger log = LoggerFactory.getLogger(DatabaseStartUp.class);
    private final Sql[] sqls;
    private final SqlExecutor executor;

    public DatabaseStartUp(SqlExecutor executor, Sql... sqls) {
        this.executor = executor;
        this.sqls = sqls;
    }

    @Override
    public void init() {
        log.debug("Start migrating");
        for (Sql sql : this.sqls) {
            this.executor.perform(sql.query());
        }
        log.debug("Migration finished. Database is initialized");
    }
}
