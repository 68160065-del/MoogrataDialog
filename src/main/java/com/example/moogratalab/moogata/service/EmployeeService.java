/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab.moogata.service;
/**
 *
 * @author USER
 */
import com.example.moogratalab.moogata.dao.EmployeeDao;
import com.example.moogratalab.moogata.model.Employee;
import java.util.List;

public class EmployeeService {

    private final EmployeeDao employeeDao = new EmployeeDao();

    public Employee login(String name, String password) {
        Employee employee = employeeDao.getByName(name);
        if (employee != null && employee.getPassword().equals(password)) {
            return employee;
        }
        return null;
    }
    // ===== เพิ่มใน Lab 11 — เปิดบริการ CRUD ที่ EmployeeDao รองรับอยู่แล้ว ให้หน้าจอใช้ =====

    /**
     * พนักงานทุกคน — ใช้โดย EmployeeFrame เพื่อเติมตาราง
     */
    public List<Employee> getAll() {
        return employeeDao.getAll();
    }

    /**
     * พนักงาน 1 คนจาก id — ใช้โดย EmployeeFrame ตอนคลิกเลือกแถวในตาราง
     */
    public Employee get(int id) {
        return employeeDao.get(id);
    }

    /**
     * เพิ่มพนักงานใหม่ — สำเร็จได้ object กลับมาพร้อม id, ล้มเหลว (ชื่อซ้ำ) ได้
     * null
     */
    public Employee save(Employee employee) {
        return employeeDao.save(employee);
    }

    /**
     * แก้ไขพนักงาน — สำเร็จได้ object กลับมา, ไม่พบแถว/ล้มเหลวได้ null
     */
    public Employee update(Employee employee) {
        return employeeDao.update(employee);
    }

    /**
     * ลบพนักงานตาม id — คืนจำนวนแถวที่ถูกลบ (0 = ไม่พบ, -1 = ผิดพลาด)
     */
    public int delete(int id) {
        Employee target = new Employee();
        target.setId(id);
        return employeeDao.delete(target);
    }
}
