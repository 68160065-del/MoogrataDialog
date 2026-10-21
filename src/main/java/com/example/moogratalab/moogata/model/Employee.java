/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogata.model;

/**
 *
 * @author USER
 */
import java.sql.ResultSet;
import java.sql.SQLException;

public class Employee {

    public static String get(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private int id;            
    private String name;       
    private String position;   
    private String password;   
    private int role; 
    public Employee(int id, String name, String position, String password, int role) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.password = password;
        this.role = role;
    }
    public Employee(String name, String position, String password, int role) {
        this(-1, name, position, password, role);
    }
    public Employee() {
        this.id = -1;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", position=" + position
                + ", role=" + role + '}';
    }
    public static Employee fromRS(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getInt("employee_id"));
        emp.setName(rs.getString("employee_name"));
        emp.setPosition(rs.getString("employee_position"));
        emp.setPassword(rs.getString("employee_password"));
        emp.setRole(rs.getInt("employee_role"));
        return emp;
    }
}
