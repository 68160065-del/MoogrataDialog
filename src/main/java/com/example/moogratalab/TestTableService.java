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
import com.example.moogratalab.moogata.model.Bill;
import com.example.moogratalab.moogata.model.DiningTable;
import com.example.moogratalab.moogata.model.Employee;
import com.example.moogratalab.moogata.service.TableService;
import java.sql.Connection;
import java.sql.Statement;

public class TestTableService {

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
            System.out.println("[ตั้งต้น] ล้าง bill_item / bill / ตัวนับ id และคืนโต๊ะทั้งหมดเป็นว่างแล้ว\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            resetDatabase();
            TableService tableService = new TableService();
            Employee emp = new Employee(1, "สมชาย", "พนักงาน");

            DiningTable table4 = tableService.getById(4);
            Bill bill1 = tableService.openTable(table4, 2, emp);
            DiningTable table4After = tableService.getById(4);
            check("เปิดโต๊ะ 4 / 2 คน สำเร็จ และโต๊ะเป็น 'ใช้งาน 2 คน'",
                    bill1 != null && "ใช้งาน".equals(table4After.getStatus()) && table4After.getHeadcount() == 2,
                    bill1 == null ? "openTable คืน null" : "bill_id=" + bill1.getId());

            Bill bill1Retry = tableService.openTable(table4After, 3, emp);
            check("เปิดโต๊ะ 4 ซ้ำต้องถูกปฏิเสธ [กฎ SC-4]",
                    bill1Retry == null,
                    bill1Retry == null ? "openTable คืน null" : "หลุดเปิดซ้ำได้ bill_id=" + bill1Retry.getId());

            DiningTable table5 = tableService.getById(5);
            Bill bill2 = tableService.openTable(table5, 3, emp);
            check("เปิดโต๊ะ 5 / 3 คน สำเร็จ",
                    bill2 != null,
                    bill2 == null ? "openTable คืน null" : "bill_id=" + bill2.getId());

            DiningTable table1 = tableService.getById(1);
            Bill billFail = tableService.openTable(table1, 0, emp);
            check("เปิดโต๊ะ 1 ด้วยลูกค้า 0 คน ต้องถูกปฏิเสธ",
                    billFail == null,
                    billFail == null ? "openTable คืน null" : "เปิดสำเร็จผิดกฎ");

            System.out.println("\nสรุป: ผ่าน " + pass + "/" + total + " รายการ");
        } finally {
            DatabaseHelper.close();
        }
    }
}
