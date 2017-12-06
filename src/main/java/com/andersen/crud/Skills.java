package com.andersen.crud;

class Skills {
    private int ID;
    private String specialty;
    
    Skills(int id, String specialty) {
        ID = id;
        this.specialty = specialty;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        ID = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
