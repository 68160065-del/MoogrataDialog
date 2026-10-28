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
import com.example.moogratalab.moogata.model.MenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuItemDao implements Dao<MenuItem> {

    private static final Logger LOG = Logger.getLogger(MenuItemDao.class.getName());

    @Override
    public MenuItem get(int id) {
        String sql = "SELECT * FROM menu_item WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return MenuItem.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "get ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return null;
    }

    @Override
    public List<MenuItem> getAll() {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_item ORDER BY category_id, item_id";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(MenuItem.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public List<MenuItem> getAll(String where, String order) {
        List<MenuItem> list = new ArrayList<>();
        String sql = "SELECT * FROM menu_item";
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
                list.add(MenuItem.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll(where, order) ล้มเหลว", ex);
        } finally {
            closeQuietly(rs, stmt);
        }
        return list;
    }

    @Override
    public MenuItem save(MenuItem obj) {
        String sql = "INSERT INTO menu_item (item_name, item_price, item_unit, item_status, category_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setDouble(2, obj.getPrice());
            stmt.setString(3, obj.getUnit());
            stmt.setString(4, obj.getStatus());
            stmt.setInt(5, obj.getCategoryId());
            stmt.executeUpdate();
            obj.setId(DatabaseHelper.getInsertedId(stmt));
            return obj;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "save(menu_item=" + obj.getName() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public MenuItem update(MenuItem obj) {
        String sql = "UPDATE menu_item SET item_name = ?, item_price = ?, item_unit = ?, "
                + "item_status = ?, category_id = ? WHERE item_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setDouble(2, obj.getPrice());
            stmt.setString(3, obj.getUnit());
            stmt.setString(4, obj.getStatus());
            stmt.setInt(5, obj.getCategoryId());
            stmt.setInt(6, obj.getId());
            int affected = stmt.executeUpdate();
            return affected > 0 ? obj : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "update(menu_item=" + obj.getName() + ") ล้มเหลว", ex);
            return null;
        } finally {
            closeQuietly(null, stmt);
        }
    }

    @Override
    public int delete(MenuItem obj) {
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
