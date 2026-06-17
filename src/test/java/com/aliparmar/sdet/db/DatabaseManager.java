package com.aliparmar.sdet.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages a local SQLite database used to demonstrate JDBC testing.
 * Creates the database file, seeds sample banking data, and hands out connections.
 */
public class DatabaseManager {

    // Stored under target/ so it is rebuilt cleanly and never committed.
    private static final String DB_URL = "jdbc:sqlite:target/parabank_demo.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Creates the schema and seeds sample data. Safe to run repeatedly.
    public static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS accounts");
            stmt.execute("DROP TABLE IF EXISTS transactions");

            stmt.execute("""
                CREATE TABLE accounts (
                    id INTEGER PRIMARY KEY,
                    customer_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    balance REAL NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE transactions (
                    id INTEGER PRIMARY KEY,
                    account_id INTEGER NOT NULL,
                    type TEXT NOT NULL,
                    amount REAL NOT NULL,
                    FOREIGN KEY (account_id) REFERENCES accounts(id)
                )
            """);

            // Seed sample accounts for customer 12212 (mirrors ParaBank's John Smith)
            stmt.execute("INSERT INTO accounts (id, customer_id, type, balance) VALUES (13344, 12212, 'CHECKING', 1500.00)");
            stmt.execute("INSERT INTO accounts (id, customer_id, type, balance) VALUES (13355, 12212, 'SAVINGS', 5000.00)");

            // Seed sample transactions
            stmt.execute("INSERT INTO transactions (id, account_id, type, amount) VALUES (1, 13344, 'Debit', 100.00)");
            stmt.execute("INSERT INTO transactions (id, account_id, type, amount) VALUES (2, 13344, 'Credit', 250.00)");
            stmt.execute("INSERT INTO transactions (id, account_id, type, amount) VALUES (3, 13355, 'Credit', 5000.00)");
        }
    }
}