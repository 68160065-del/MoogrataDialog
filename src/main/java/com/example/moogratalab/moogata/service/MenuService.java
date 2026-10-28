/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.moogratalab.moogata.service;

/**
 *
 * @author USER
 */
import com.example.moogratalab.moogata.dao.MenuItemDao;
import com.example.moogratalab.moogata.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class MenuService {

    private final MenuItemDao menuItemDao = new MenuItemDao();

    public List<MenuItem> getAll() {
        return menuItemDao.getAll();
    }
    public List<MenuItem> getAvailable() {
        List<MenuItem> available = new ArrayList<>();
        for (MenuItem item : menuItemDao.getAll()) {
            if ("มี".equals(item.getStatus())) {
                available.add(item);
            }
        }
        return available;
    }
}
