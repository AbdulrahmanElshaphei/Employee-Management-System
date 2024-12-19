/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ems;

/**
 *
 * @author abdo
 */
public class Department {
     private int id;
    private String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and toString()
    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Department: " + name;
    }
}
