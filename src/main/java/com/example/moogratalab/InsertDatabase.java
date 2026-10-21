/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab;

/**
 *
 * @author USER
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertDatabase {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:moograta.db";
        String sql = "INSERT INTO menu_category (category_name) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(url);
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, "สุกี้แห้ง");
            int affected = stmt.executeUpdate();
            System.out.println("Inserted " + affected + " row");

            ResultSet keys = null;
            try {
                keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    System.out.println("New category_id = " + keys.getInt(1));
                }
            } finally {
                if (keys != null) {
                    try {
                        keys.close();
                    } catch (SQLException closeEx) {
                        System.err.println("Close keys failed: " + closeEx.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Insert failed: " + ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
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
