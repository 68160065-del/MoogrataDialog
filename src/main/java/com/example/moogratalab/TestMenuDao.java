package com.example.moogratalab;

import com.example.moogata.dao.MenuDao;
import com.example.moogata.model.Menu;
import com.example.moogratalab.helper.DatabaseHelper;
import java.util.List;

public class TestMenuDao {
    public static void main(String[] args) {
        try {
            MenuDao menuDao = new MenuDao();

            System.out.println("=== Buffet Menu List ===");
            // ดึงข้อมูลรายการเมนูในหมวดหมู่บุฟเฟ่ต์
            List<Menu> buffetList = menuDao.findByCategory("บุฟเฟ่ต์");

            if (buffetList.isEmpty()) {
                System.out.println("No menu found in Buffet category");
            } else {
                for (Menu item : buffetList) {
                    System.out.printf("Name: %-20s | Price: %.2f%n", item.getName(), item.getPrice());
                }
            }

        } finally {
            DatabaseHelper.close();
        }
    }
}