package com.example.moogata.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu {
    private int id;
    private String name;
    private String category;
    private double price;

    public Menu() {
        this.id = -1;
        this.name = "";
        this.category = "";
        this.price = 0.0;
    }

    public Menu(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // แปลงชื่อคอลัมน์ให้ตรงกับ SQLiteStudio (item_id, item_name, item_price, category_id)
    public static Menu fromRS(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("item_id"));
        menu.setName(rs.getString("item_name"));
        menu.setPrice(rs.getDouble("item_price"));
        menu.setCategory(String.valueOf(rs.getInt("category_id"))); // อ่าน category_id เป็น String
        return menu;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", name=" + name + ", category=" + category + ", price=" + price + '}';
    }
}