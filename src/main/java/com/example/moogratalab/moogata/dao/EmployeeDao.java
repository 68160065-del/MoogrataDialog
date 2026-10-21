package com.example.moogata.dao;

import com.example.moogratalab.helper.DatabaseHelper;
import com.example.moogata.model.Employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDao implements Dao<Employee>{
    private static final Logger LOG = Logger.getLogger(EmployeeDao.class.getName());

    @Override
    public Employee get(int id) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Employee.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "get ล้มเหลว", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close rs failed: " + closeEx.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
        return null;
    }

    public Employee getByName(String name) {
        String sql = "SELECT * FROM employee WHERE employee_name = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Employee.fromRS(rs);
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getByName ล้มเหลว", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close rs failed: " + closeEx.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        Connection conn = DatabaseHelper.getConnect();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(Employee.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll() ล้มเหลว", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close rs failed: " + closeEx.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
        return list;
    }

    @Override
    public List<Employee> getAll(String where, String order) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE " + where + " ORDER BY " + order;
        LOG.warning("getAll(where, order) ต่อสตริงเข้า SQL ตรง ๆ — ใช้กับข้อมูลผู้ใช้จริงไม่ได้ (SQL injection)");
        Connection conn = DatabaseHelper.getConnect();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(Employee.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "getAll(where, order) ล้มเหลว: " + sql, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close rs failed: " + closeEx.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
        return list;
    }

    @Override
    public Employee save(Employee obj) {
        String sql = "INSERT INTO employee (employee_name, employee_position, employee_password, employee_role) "
                + "VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getPosition());
            stmt.setString(3, obj.getPassword());
            stmt.setInt(4, obj.getRole());
            stmt.executeUpdate();
            obj.setId(DatabaseHelper.getInsertedId(stmt));
            return obj;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "save(employee=" + obj.getName() + ") ล้มเหลว", ex);
            return null;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
    }

    @Override
    public Employee update(Employee obj) {
        String sql = "UPDATE employee "
                + "SET employee_name = ?, employee_position = ?, employee_password = ?, employee_role = ? "
                + "WHERE employee_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getPosition());
            stmt.setString(3, obj.getPassword());
            stmt.setInt(4, obj.getRole());
            stmt.setInt(5, obj.getId());
            int affected = stmt.executeUpdate();
            return affected > 0 ? obj : null;
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "update(employee_id=" + obj.getId() + ") ล้มเหลว", ex);
            return null;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
    }

    @Override
    public int delete(Employee obj) {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, obj.getId());
            return stmt.executeUpdate();
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "delete(employee_id=" + obj.getId() + ") ล้มเหลว", ex);
            return -1;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
    }

    // เมธอดคัดค้นตามตำแหน่ง (ใช้โครงเดียวกับ getByName)
    public List<Employee> findByPosition(String position) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE employee_position = ?";
        Connection conn = DatabaseHelper.getConnect();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, position);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(Employee.fromRS(rs));
            }
        } catch (SQLException ex) {
            LOG.log(Level.WARNING, "findByPosition ล้มเหลว", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close rs failed: " + closeEx.getMessage());
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException closeEx) {
                    System.err.println("Close stmt failed: " + closeEx.getMessage());
                }
            }
        }
        return list;
    }
}