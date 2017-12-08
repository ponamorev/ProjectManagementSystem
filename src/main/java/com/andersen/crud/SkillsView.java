package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class SkillsView {
    private String query;


    Skills findSkill(Connection connection, Scanner reader)
            throws SQLException {
        String query = "SELECT * FROM skills";
        Skills skill = null;

        CommonMethods.checkQueryAndOut(query, new String[] {"id", "specialty"}, connection);
        System.out.print("Choose the ID of the skill: ");
        int id = reader.nextInt();
        reader.nextLine();

        query = "SELECT * FROM skills WHERE id = " + id;
        ResultSet result = connection.createStatement().executeQuery(query);
        while (result.next())
            skill = new Skills(result.getInt("id"),
                    result.getString("specialty"));

        return skill;
    }


    private LinkedList<Skills> findManySkills(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Skills> skillsList = new LinkedList<>();
        Scanner reader = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        ResultSet result;
        String value, choice;
        boolean flag = false;

        System.out.println("Output all table? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            query = "SELECT * FROM skills";
            result = statement.executeQuery(query);
            while (result.next()) {
                skillsList.add(new Skills(result.getInt("id"), result.getString("specialty")));
            }

            return skillsList;
        }
        else {

            CommonMethods.printTableColumns("skills", connection);

            System.out.println("Enter one and more values or range of values.\nIf you enter string value you will " +
                    "write this value in follow format: string_column = 'some_value'.\nFor example, " +
                    "'column = value' or 'column1 = value1, column2 > value2' or 'column BETWEEN min_value AND max_value'..");
            value = reader.nextLine();
            builder.append(value);

            while (!flag) {

                System.out.println("Would you like to add another value to search? (Y/any key)");
                choice = reader.nextLine();

                if (choice.equals("y") || choice.equals("Y")) {

                    value = reader.nextLine();

                    System.out.println("It's required condition? (Y/any key)");
                    choice = reader.nextLine();

                    if (choice.equals("y") || choice.equals("Y"))
                        builder.append(" AND ").append(value);
                    else builder.append(" OR ").append(value);
                } else flag = true;
            }

            if (builder.toString().equals("") || builder.toString().equals("\n"))
                query = "SELECT * FROM skills";
            else
                query = "SELECT * FROM skills WHERE " + builder.toString();

            result = statement.executeQuery(query);
            while (result.next()) {
                skillsList.add(new Skills(result.getInt("id"), result.getString("name")));
            }

            return skillsList;
        }
    }

    LinkedList<Skills> readSkills(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Skills> skillsList;
        StringBuilder builder = new StringBuilder();
        skillsList = findManySkills(connection, statement);

        Skills[] arrayOfSkills;
        arrayOfSkills = skillsList.toArray(new Skills[0]);

        for (int i = 0; i < arrayOfSkills.length; i++) {
            builder.append("id = ").append(arrayOfSkills[i].getID());
            if (i + 1 < arrayOfSkills.length)
                builder.append(" OR ");
        }

        query = "SELECT * FROM skills WHERE " + builder.toString();

        CommonMethods.checkQueryAndOut(query, new String[]{"id", "specialty"}, connection);
        
        return skillsList;
    }
}
