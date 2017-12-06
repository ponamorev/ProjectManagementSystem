package com.andersen.crud;

public class Projects {
    private int ID, cost;
    private String name, description, start, deadline;

    public Projects(int ID, String name, String description, String start,
                    String deadline, int cost) {
        this.ID = ID;
        this.cost = cost;
        this.name = name;
        this.description = description;
        this.start = start;
        this.deadline = deadline;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
