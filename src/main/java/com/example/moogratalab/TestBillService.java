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
import com.example.moogratalab.moogata.dao.MenuItemDao;
import com.example.moogratalab.moogata.model.Bill;
import com.example.moogratalab.moogata.model.DiningTable;
import com.example.moogratalab.moogata.model.Employee;
import com.example.moogratalab.moogata.model.MenuItem;
import com.example.moogratalab.moogata.service.BillService;
import com.example.moogratalab.moogata.service.TableService;
import java.sql.Connection;
import java.sql.Statement;

public class TestBillService {

    private static int pass = 0;
    private static int total = 0;

    private static void check(String label, boolean ok, String detail) {
        total++;
        if (ok) {
            pass++;
        }
        System.out.println("[" + total + "] " + label + " ... " + (ok ? "PASS" : "FAIL")
                + (detail.isEmpty() ? "" : "  (" + detail + ")"));
    }

    private static void resetDatabase() {
        Connection conn = DatabaseHelper.getConnect();
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM bill_item");
            stmt.executeUpdate("DELETE FROM bill");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name IN ('bill', 'bill_item')");
            stmt.executeUpdate("UPDATE dining_table SET status = 'ว่าง', headcount = 0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            resetDatabase();
            TableService tableService = new TableService();
            BillService billService = new BillService();
            MenuItemDao menuItemDao = new MenuItemDao();

            Employee emp = new Employee(1, "สมชาย", "แคชเชียร์");
            DiningTable table4 = tableService.getById(4);
            Bill bill = tableService.openTable(table4, 2, emp);

            check("เปิดบิลใหม่ที่โต๊ะ 4", bill != null, bill == null ? "ล้มเหลว" : "bill_id=" + bill.getId());

            MenuItem m1 = menuItemDao.get(1); // บุฟเฟ่ต์หมูกะทะ (289.-)
            MenuItem m3 = menuItemDao.get(3); // น้ำซุปหม่าล่า (49.-)
            MenuItem m7 = menuItemDao.get(7); // ชาเย็น (25.-)

            billService.addItem(bill, m1, 2);
            double totalRound1 = billService.getTotal(bill.getId());
            check("สั่งรอบที่ 1: บุฟเฟ่ต์หมูกะทะ x2 บันทึกและรวม 578.00 บาท",
                    totalRound1 == 578.0, "ยอด " + totalRound1);

            billService.addItem(bill, m3, 1);
            billService.addItem(bill, m7, 2);
            int itemCount = billService.getItemsByBillId(bill.getId()).size();
            check("สั่งรอบที่ 2: หม่าล่า x1 + ชาเย็น x2 ตารางบิลต้องมี 3 แถว",
                    itemCount == 3, "ได้ " + itemCount + " แถว");

            double totalAll = billService.getTotal(bill.getId());
            check("ยอดรวมทั้งบิล 677.00 บาท", totalAll == 677.0, String.valueOf(totalAll));

            Bill checkoutFail = billService.checkout(bill, 600.0);
            check("ชำระด้วย 600.00 ต้องถูกปฏิเสธ (เงินไม่พอ)", checkoutFail == null, "checkout คืน null");

            Bill checkoutSuccess = billService.checkout(bill, 700.0);
            check("ชำระด้วย 700.00 ปิดบิลสำเร็จ: ยอด 677.00 ทอน 23.00 สถานะ 'ปิด'",
                    checkoutSuccess != null && checkoutSuccess.isClosed() && checkoutSuccess.getChangeAmount() == 23.0,
                    checkoutSuccess == null ? "ล้มเหลว" : "ทอน " + checkoutSuccess.getChangeAmount());

            DiningTable table4After = tableService.getById(4);
            check("โต๊ะ 4 หลังปิดบิลต้องกลับเป็น 'ว่าง'",
                    table4After != null && table4After.isVacant(),
                    table4After == null ? "ไม่พบโต๊ะ" : table4After.getStatus() + " " + table4After.getHeadcount() + " คน");

            System.out.println("\nสรุป: ผ่าน " + pass + "/" + total + " รายการ");
        } finally {
            DatabaseHelper.close();
        }
    }
}
