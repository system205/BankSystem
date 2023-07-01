package oop.course.storage.migrations;

public class DatabaseStartUp implements Initializer {


    private final Sql[] sqls;
    private final SqlExecutor executor;

    public DatabaseStartUp(SqlExecutor executor, Sql... sqls) {
        this.executor = executor;
        this.sqls = sqls;
    }

    @Override
    public void init() {
        System.out.println("Start migrating");
        for (Sql sql : this.sqls) {
            this.executor.perform(sql.query());
        }
        System.out.println("Migration finished. Database is initialized");
    }
}
