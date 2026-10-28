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
import com.example.moogratalab.moogata.model.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillDao implements Dao<Bill>{

    private static final Logger LOG = Logger.getLogger(BillDao.class.getName());
    
    @Override
    public Bill get(int id) {
        String sql = "SELECT * FROM bill WHERE bill_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Bill.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "get ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }

    @Override
    public List<Bill> getAll() {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill ORDER BY bill_id DESC";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Bill.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public List<Bill> getAll(String where, String order) {
        List<Bill> list = new ArrayList<>();
        String sql = "SELECT * FROM bill";
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
                list.add(Bill.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll(where, order) ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    // --- Method ธงประจำ Step 4.3 (save INSERT แค่ 3 ค่า) ---
    @Override
    public Bill save(Bill obj) {
        String sql = "INSERT INTO bill (table_id, status, opened_by) VALUES (?, ?, ?)";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getTableId());
            stmt.setString(2, obj.getStatus());
            stmt.setInt(3, obj.getOpenedBy());
            stmt.executeUpdate();
            obj.setId(DatabaseHelper.getInsertedId(stmt));
            return obj;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "save(bill_table_id=" + obj.getTableId() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public Bill update(Bill obj) {
        String sql = "UPDATE bill SET table_id = ?, total = ?, cash_received = ?, "
                + "change_amount = ?, status = ?, opened_by = ? WHERE bill_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getTableId());
            stmt.setDouble(2, obj.getTotal());
            stmt.setDouble(3, obj.getCashReceived());
            stmt.setDouble(4, obj.getChangeAmount());
            stmt.setString(5, obj.getStatus());
            stmt.setInt(6, obj.getOpenedBy());
            stmt.setInt(7, obj.getId());
            int affected = stmt.executeUpdate();
            return affected > 0 ? obj : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "update(bill_id=" + obj.getId() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(Bill obj) {
        String sql = "DELETE FROM bill WHERE bill_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "delete(bill_id=" + obj.getId() + ") ล้มเหลว", ex);
            return 0;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    // --- Method ธงประจำ Step 4.3 ---
    public Bill getOpenByTableId(int tableId) {
        String sql = "SELECT * FROM bill WHERE table_id = ? AND status = 'เปิด' ORDER BY bill_id DESC LIMIT 1";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, tableId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Bill.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getOpenByTableId ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }

    // --- Method ธงประจำ Step 4.3 ---
    public Bill close(int billId, double total, double cashReceived, double changeAmount) {
        String sql = "UPDATE bill SET status = 'ปิด', total = ?, cash_received = ?, "
                + "change_amount = ?, closed_at = datetime('now', 'localtime') WHERE bill_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, total);
            stmt.setDouble(2, cashReceived);
            stmt.setDouble(3, changeAmount);
            stmt.setInt(4, billId);
            int affected = stmt.executeUpdate();
            return affected > 0 ? get(billId) : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "close(bill_id=" + billId + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    private void closeQuietly(ResultSet rs, Statement stmt) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ex) { LOG.log(Level.SEVERE, null, ex); }
        }
    }
}
