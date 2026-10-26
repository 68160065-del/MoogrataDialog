/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab.moogata.model;

/**
 *
 * @author USER
 */
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillItem {

    private int id;
    private int billId;
    private int menuItemId;
    private String itemName;
    private int qty;
    private double unitPrice;

    public BillItem(int id, int billId, int menuItemId, String itemName, int qty, double unitPrice) {
        this.id = id;
        this.billId = billId;
        this.menuItemId = menuItemId;
        this.itemName = itemName;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BillItem() {
        this.id = -1;
    }

    /**
     * * * สร้างจากเมนู — ลอกชื่อและราคาจาก menu_item มาเก็บเอง (snapshot)
     */
    public BillItem(int billId, MenuItem menuItem, int qty) {
        this(-1, billId, menuItem.getId(), menuItem.getName(), qty, menuItem.getPrice());
    }

    /**
     * ยอดของบรรทัดนี้ = จำนวน × ราคาต่อหน่วย (ตรงกับ SQL: qty * unit_price ใน
     * Step 2)
     */
    public double getLineTotal() {
        return qty * unitPrice;
    }

    /**
     * ยอดพร้อมทศนิยม 2 ตำแหน่ง ไว้โชว์ในตารางบิล
     */
    public String getLineTotalText() {
        return String.format("%.2f", getLineTotal());
    }

    public static BillItem fromRS(ResultSet rs) throws SQLException {
        BillItem item = new BillItem();
        item.setId(rs.getInt("bill_item_id"));
        item.setBillId(rs.getInt("bill_id"));
        item.setMenuItemId(rs.getInt("menu_item_id"));
        item.setItemName(rs.getString("item_name"));
        item.setQty(rs.getInt("qty"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        return item;
    }
}
