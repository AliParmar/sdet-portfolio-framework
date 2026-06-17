package com.aliparmar.sdet.base;

import com.aliparmar.sdet.db.DatabaseManager;
import org.testng.annotations.BeforeClass;

import java.sql.SQLException;

/**
 * Base class for JDBC database tests.
 * Builds and seeds the SQLite demo database once before the test class runs.
 */
public class BaseDbTest {

    @BeforeClass(alwaysRun = true)
    public void setUpDatabase() throws SQLException {
        DatabaseManager.initializeDatabase();
    }
}