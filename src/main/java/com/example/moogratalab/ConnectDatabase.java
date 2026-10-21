/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab;

/**
 *
 * @author USER
 */
import com.example.moogratalab.helper.DatabaseHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:" + DatabaseHelper.DB_FILE;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connected to " + DatabaseHelper.DB_FILE + " successfully");
        } catch (SQLException ex) {
            System.out.println("Connect failed: " + ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close conn failed: " + closeEx.getMessage());
                }
            }
        }
    }
}
