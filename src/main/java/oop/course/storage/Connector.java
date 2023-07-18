package oop.course.storage;

import java.sql.*;

/**
 * Database connector
 * */
public interface Connector {
    Connection connect() throws Exception;
}
