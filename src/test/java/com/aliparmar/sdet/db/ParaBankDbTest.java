package com.aliparmar.sdet.db;

import com.aliparmar.sdet.base.BaseDbTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.*;

/**
 * JDBC tests against the seeded SQLite demo database.
 * Demonstrates connection handling, parameterized queries, and result-set assertions.
 */

public class ParaBankDbTest extends BaseDbTest {

    @Test(groups = {"smoke", "db"}, description = "Customer 12212 has the expected number of accounts")
    public void verifyCustomerAccountCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS account_count FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 12212);
            try (ResultSet rs = stmt.executeQuery()) {
                Assert.assertTrue(rs.next(), "Query should return a result row");
                int count = rs.getInt("account_count");
                Assert.assertEquals(count, 2, "Customer 12212 should have 2 accounts");
            }
        }
    }

    @Test(groups = {"smoke", "db"}, description = "Sum of account balances for customer 12212 matches expected total")
    public void verifyCustomerTotalBalance() throws SQLException {
        String sql = "SELECT SUM(balance) AS total_balance FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 12212);
            try (ResultSet rs = stmt.executeQuery()) {
                Assert.assertTrue(rs.next(), "Query should return a result row");
                double total = rs.getDouble("total_balance");
                Assert.assertEquals(total, 6500.00, 0.001, "Total balance across customer 12212's accounts should be 6500.00");
            }
        }
    }

    @Test(groups = {"sanity", "db"}, description = "Checking account balance matches the seeded value")
    public void verifyCheckingAccountBalance() throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 13344);
            try (ResultSet rs = stmt.executeQuery()) {
                Assert.assertTrue(rs.next(), "Account 13344 should exist");
                double balance = rs.getDouble("balance");
                Assert.assertEquals(balance, 1500.00, 0.001, "Checking account balance should be 1500.00");
            }
        }
    }

    @Test(groups = {"sanity", "db"}, description = "Account 13344 has the expected number of transactions")
    public void verifyTransactionCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS txn_count FROM transactions WHERE account_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, 13344);
            try (ResultSet rs = stmt.executeQuery()) {
                Assert.assertTrue(rs.next(), "Query should return a result row");
                int count = rs.getInt("txn_count");
                Assert.assertEquals(count, 2, "Account 13344 should have 2 transactions");
            }
        }
    }

    @Test(groups = {"sanity", "db"}, description = "Every transaction references a valid, existing account (no orphaned rows)")
    public void verifyTransactionsReferenceValidAccounts() throws SQLException {
        String sql = """
        SELECT t.id, t.account_id
        FROM transactions t
        LEFT JOIN accounts a ON t.account_id = a.id
        WHERE a.id IS NULL
    """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            Assert.assertFalse(rs.next(), "Found a transaction referencing a nonexistent account — referential integrity violated");
        }
    }
}