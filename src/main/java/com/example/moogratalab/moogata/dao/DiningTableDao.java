/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab.moogata.dao;

/**
 *
 * @author USER
 */
import com.example.moogratalab.helper.DatabaseHelper;
import com.example.moogratalab.moogata.model.DiningTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiningTableDao implements Dao<DiningTable> {

    private static final Logger LOG = Logger.getLogger(DiningTable.class.getName());

    @Override
    public DiningTable get(int id) {
        String sql = "SELECT * FROM dining_table WHERE table_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return DiningTable.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "get ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }

    @Override
    public List<DiningTable> getAll() {
        List<DiningTable> list = new ArrayList<>();
        String sql = "SELECT * FROM dining_table ORDER BY table_number";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(DiningTable.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public List<DiningTable> getAll(String where, String order) {
        List<DiningTable> list = new ArrayList<>();
        String sql = "SELECT * FROM dining_table";
        if (where != null && !where.trim().isEmpty()) {
            sql += " WHERE " + where;
        }
        if (order != null && !order.trim().isEmpty()) {
            sql += " ORDER BY " + order;
        }
        LOG.log(Level.WARNING, "getAll(where, order) มีความเสี่ยง SQL Injection: {0}", sql);
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(DiningTable.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll(where, order) ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public DiningTable save(DiningTable obj) {
        String sql = "INSERT INTO dining_table (table_number, status, headcount) VALUES (?, ?, ?)";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getNumber());
            stmt.setString(2, obj.getStatus());
            stmt.setInt(3, obj.getHeadcount());
            stmt.executeUpdate();
            obj.setId(DatabaseHelper.getInsertedId(stmt));
            return obj;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "save(table_number=" + obj.getNumber() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public DiningTable update(DiningTable obj) {
        String sql = "UPDATE dining_table SET table_number = ?, status = ?, headcount = ? WHERE table_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getNumber());
            stmt.setString(2, obj.getStatus());
            stmt.setInt(3, obj.getHeadcount());
            stmt.setInt(4, obj.getId());
            int affected = stmt.executeUpdate();
            return affected > 0 ? obj : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "update(table_id=" + obj.getId() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(DiningTable obj) {
        String sql = "DELETE FROM menu_item WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            int affected = stmt.executeUpdate();
            return affected;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "delete(menu_item_id=" + obj.getId() + ") ล้มเหลว", ex);
            return 0;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    public DiningTable updateStatus(int tableId, String status, int headcount) {
        String sql = "UPDATE dining_table SET status = ?, headcount = ? WHERE table_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, headcount);
            stmt.setInt(3, tableId);
            int affected = stmt.executeUpdate();
            return affected > 0 ? get(tableId) : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "updateStatus(table_id=" + tableId + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    private void closeQuietly(ResultSet rs, Statement stmt) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }
}
