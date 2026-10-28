/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab.moogata.service;

/**
 *
 * @author USER
 */
import com.example.moogratalab.moogata.dao.BillDao;
import com.example.moogratalab.moogata.dao.DiningTableDao;
import com.example.moogratalab.moogata.model.Bill;
import com.example.moogratalab.moogata.model.DiningTable;
import com.example.moogratalab.moogata.model.Employee;
import java.util.List;

public class TableService {

    private final DiningTableDao tableDao = new DiningTableDao();
    private final BillDao billDao = new BillDao();

    public List<DiningTable> getAll() {
        return tableDao.getAll();
    }

    public DiningTable getById(int id) {
        return tableDao.get(id);
    }

    public Bill openTable(DiningTable table, int headcount, Employee employee) {
        // กฎ 1: ต้องรู้จำนวนลูกค้าก่อนเปิดโต๊ะเสมอ
        if (table == null || employee == null || headcount < 1) {
            return null;
        }
        // กฎ 2 (SC-4): โต๊ะที่ยังใช้งานอยู่ ห้ามเปิดซ้ำ แม้เป็นพนักงานคนอื่นก็ตาม
        if (!table.isVacant()) {
            return null;
        }

        Bill bill = billDao.save(new Bill(table.getId(), employee.getId()));
        if (bill == null) {
            return null;
        }
        tableDao.updateStatus(table.getId(), DiningTable.STATUS_IN_USE, headcount);
        return bill;
    }

    /**
     * เช็คบิลสำเร็จแล้ว — คืนโต๊ะเป็น 'ว่าง' (เรียกจาก BillService.checkout)
     */
    public boolean releaseTable(int tableId) {
        return tableDao.updateStatus(tableId, DiningTable.STATUS_VACANT, 0) != null;
    }
}
