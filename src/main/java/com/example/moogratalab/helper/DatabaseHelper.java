package com.example.moogratalab.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHelper {
    private static final Logger LOG = Logger.getLogger(DatabaseHelper.class.getName());
    public static final String DB_FILE = "D:/MoogrataDialog/moograta.db"; 
    private static final String URL = "jdbc:sqlite:" + DB_FILE;
    private static Connection conn;

    private DatabaseHelper() {}

    public static synchronized Connection getConnect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL);
                try (Statement pragma = conn.createStatement()) {
                    pragma.execute("PRAGMA foreign_keys = ON");
                }
                LOG.info("Connected to SQLite: " + DB_FILE);
                
                // สั่งสร้างตารางและ Seed ข้อมูลอัตโนมัติเมื่อเปิดใช้งาน
                initDatabase();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "เชื่อมต่อฐานข้อมูลไม่สำเร็จ: " + URL, ex);
            }
        }
        return conn;
    }

    private static void initDatabase() {
        if (conn == null) return;
        try (Statement stmt = conn.createStatement()) {
            // 1. สร้างตาราง dining_table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS dining_table ("
                    + "table_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "table_number INTEGER UNIQUE NOT NULL, "
                    + "status TEXT NOT NULL, "
                    + "headcount INTEGER DEFAULT 0)");

            // 2. สร้างตาราง bill
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bill ("
                    + "bill_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "table_id INTEGER NOT NULL, "
                    + "total REAL DEFAULT 0, "
                    + "cash_received REAL DEFAULT 0, "
                    + "change_amount REAL DEFAULT 0, "
                    + "status TEXT NOT NULL, "
                    + "opened_by INTEGER, "
                    + "opened_at TEXT DEFAULT (datetime('now', 'localtime')), "
                    + "closed_at TEXT)");

            // 3. สร้างตาราง bill_item
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bill_item ("
                    + "bill_item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "bill_id INTEGER NOT NULL, "
                    + "menu_item_id INTEGER NOT NULL, "
                    + "item_name TEXT NOT NULL, "
                    + "qty INTEGER NOT NULL, "
                    + "unit_price REAL NOT NULL)");

            // 4. Seed ข้อมูลโต๊ะ 6 ตัว (กรณีตารางยังว่างอยู่)
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM dining_table");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (1, 'ว่าง', 0)");
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (2, 'ว่าง', 0)");
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (3, 'ใช้งาน', 4)");
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (4, 'ว่าง', 0)");
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (5, 'ว่าง', 0)");
                stmt.executeUpdate("INSERT INTO dining_table (table_number, status, headcount) VALUES (6, 'ว่าง', 0)");
                LOG.info("Seeded 6 dining tables successfully.");
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "สร้างตารางหรือ Seed ข้อมูลไม่สำเร็จ", ex);
        }
    }

    public static synchronized void close() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                LOG.info("Connection closed.");
            } catch (SQLException ex) {
                LOG.log(Level.WARNING, "ปิด connection ไม่สำเร็จ", ex);
            }
        }
    }

    public static int getInsertedId(Statement stmt) {
        ResultSet keys = null;
        try {
            keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "อ่าน generated key ไม่สำเร็จ", ex);
        } finally {
            if (keys != null) {
                try { keys.close(); } catch (SQLException closeEx) { System.err.println("Close keys failed: " + closeEx.getMessage()); }
            }
        }
        return -1;
    }
}