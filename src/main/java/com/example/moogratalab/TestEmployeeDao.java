package com.example.moogratalab;

import com.example.moogata.dao.EmployeeDao;
import com.example.moogratalab.helper.DatabaseHelper;
import com.example.moogata.model.Employee;

public class TestEmployeeDao {

    public static void main(String[] args) {
        try {
            EmployeeDao employeeDao = new EmployeeDao();

            System.out.println("--- getAll(): พนักงานทุกคน ---");
            for (Employee emp : employeeDao.getAll()) {
                System.out.println(emp);
            }

            System.out.println("--- getAll(where, order): role = 2 เรียงตามชื่อ ---");
            for (Employee emp : employeeDao.getAll("employee_role = 2", "employee_name ASC")) {
                System.out.println(emp);
            }

            // เรียกใช้ findByPosition
            System.out.println("--- findByPosition(): พนักงานเสิร์ฟ ---");
            for (Employee emp : employeeDao.findByPosition("พนักงานเสิร์ฟ")) {
                System.out.println(emp); // พิมพ์ Object พนักงานแทนข้อความ
            }

            Employee found = employeeDao.get(2);
            System.out.println("get(2) = " + found);

            Employee newbie = new Employee("NongCream", "หัวหน้ากะ", "1234", 2);
            newbie = employeeDao.save(newbie);
            System.out.println("save = " + newbie);

            if (newbie != null) {
                newbie.setPosition("หัวหน้ากะ");
                employeeDao.update(newbie);
                // เรียกผ่าน employeeDao
                System.out.println("after update = " + employeeDao.get(newbie.getId())); 
            }

            if (newbie != null) {
                System.out.println("delete rows = " + employeeDao.delete(newbie));
            }

        } finally {
            DatabaseHelper.close();
        }
    }
}