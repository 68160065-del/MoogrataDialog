package com.example.moogata.dao;

import com.example.moogata.model.Menu;
import com.example.moogratalab.helper.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuDao implements Dao<Menu> {

    private static final Logger LOG = Logger.getLogger(MenuDao.class.getName());

    @Override
    public Menu get(int id) {
        String sql = "SELECT * FROM menu_item WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Menu.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "get ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }

    @Override
    public List<Menu> getAll() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_item";
        Connection conn = DatabaseHelper.getConnect();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(Menu.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public List<Menu> getAll(String where, String order) {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_item WHERE " + where + " ORDER BY " + order;
        Connection conn = DatabaseHelper.getConnect();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(Menu.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll(where, order) ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public Menu save(Menu obj) {
        String sql = "INSERT INTO menu_item (item_name, item_price, category_id) VALUES (?, ?, ?)";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setDouble(2, obj.getPrice());
            stmt.setInt(3, Integer.parseInt(obj.getCategory()));
            stmt.executeUpdate();
            obj.setId(DatabaseHelper.getInsertedId(stmt));
            return obj;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "save ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public Menu update(Menu obj) {
        String sql = "UPDATE menu_item SET item_name = ?, item_price = ?, category_id = ? WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setDouble(2, obj.getPrice());
            stmt.setInt(3, Integer.parseInt(obj.getCategory()));
            stmt.setInt(4, obj.getId());
            int affected = stmt.executeUpdate();
            return affected > 0 ? obj : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "update ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(Menu obj) {
        String sql = "DELETE FROM menu_item WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "delete ล้มเหลว", ex);
            return -1;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    // เมธอดค้นหาตาม category_id โดยสามารถส่งคำว่า "บุฟเฟ่ต์" หรือ ID "1" เข้ามาก็ได้
    public List<Menu> findByCategory(String category) {
        List<Menu> list = new ArrayList<>();
        // ใช้ JOIN ตาราง menu_category เพื่อค้นหาด้วยชื่อหมวดหมู่ (เช่น "บุฟเฟ่ต์") ได้โดยตรง
        String sql = "SELECT m.* FROM menu_item m " +
                     "JOIN menu_category c ON m.category_id = c.category_id " +
                     "WHERE c.category_name LIKE ? OR m.category_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + category + "%");
            int catId = -1;
            try { catId = Integer.parseInt(category); } catch (NumberFormatException ignored) {}
            stmt.setInt(2, catId);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Menu.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "findByCategory ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    private void closeQuietly(ResultSet rs, Statement stmt) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) { System.err.println("Close rs failed: " + e.getMessage()); }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) { System.err.println("Close stmt failed: " + e.getMessage()); }
        }
    }
}