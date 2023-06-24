package oop.course.storage.migrations;

public interface SqlExecutor {
    void perform(String sql);
}
