package com.andersen.crud;

class Developers {
    private int ID, salary;
    private String name;

    Developers(int i, String n, int s) {
        ID = i;
        name = n;
        salary = s;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
