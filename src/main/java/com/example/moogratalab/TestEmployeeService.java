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
import com.example.moogata.model.Employee;
import com.example.moogata.service.EmployeeService;

public class TestEmployeeService {
    public static void main(String[] args) {
        try {
            EmployeeService employeeService = new EmployeeService();

            // ล็อกอินถูกต้อง (seed data: somchai / 1234 ตำแหน่งแคชเชียร์)
            Employee employee = employeeService.login("somchai", "1234");
            if (employee != null) {
                System.out.println("Welcome user : " + employee.getName()
                        + " (" + employee.getPosition() + ")");
            } else {
                System.out.println("Error, wrong username or password");
            }

            // กรณีรหัสผ่านผิด — ต้องได้ Error
            Employee hacker = employeeService.login("somchai", "0000");
            if (hacker != null) {
                System.out.println("Welcome user : " + hacker.getName());
            } else {
                System.out.println("Error, wrong username or password");
            }
        } finally {
            DatabaseHelper.close();
        }
    }
}
