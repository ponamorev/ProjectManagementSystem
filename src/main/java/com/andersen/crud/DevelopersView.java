package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

class DevelopersView {
    private String query;

    Developers findDeveloper(Connection connection, Scanner reader)
            throws SQLException {
        String query = "SELECT * FROM developers";
        Developers developer = null;

        CommonMethods.checkQueryAndOut(query, new String[] {"id", "name", "salary"}, connection);
        System.out.print("Choose the ID of the developer: ");
        int id = reader.nextInt();
        reader.nextLine();

        query = "SELECT * FROM developers WHERE id = " + id;
        ResultSet result = connection.createStatement().executeQuery(query);
        while (result.next())
            developer = new Developers(result.getInt("id"),
                    result.getString("name"), result.getInt("salary"));

        return developer;
    }

    private LinkedList<Developers> findManyDevelopers(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Developers> developersList = new LinkedList<>();
        Scanner reader = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        ResultSet result;
        String value, choice;
        boolean flag = false;

        System.out.println("Output all table? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            query = "SELECT * FROM developers";
            result = statement.executeQuery(query);
            while (result.next()) {
                developersList.add(new Developers(result.getInt("id"), result.getString("name"),
                        result.getInt("salary")));
            }

            return developersList;
        }
        else {

            CommonMethods.printTableColumns("developers", connection);

            System.out.println("Enter one and more values or range of values.\nIf you enter string value you will " +
                    "write this value in follow format: string_column = 'some_value'.\nFor example, " +
                    "'column = value' or 'column1 = value1, column2 = value2' or 'column BETWEEN min_value AND max_value'..");
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
                query = "SELECT * FROM developers";
            else
                query = "SELECT * FROM developers WHERE " + builder.toString();

            result = statement.executeQuery(query);
            while (result.next()) {
                developersList.add(new Developers(result.getInt("id"), result.getString("name"),
                        result.getInt("salary")));
            }

            return developersList;
        }
    }

    LinkedList<Developers> readDevelopers(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Developers> developersList;
        StringBuilder builder = new StringBuilder();
        developersList = findManyDevelopers(connection, statement);

        Developers[] arrayOfDevelopers;
        arrayOfDevelopers = developersList.toArray(new Developers[0]);

        for (int i = 0; i < arrayOfDevelopers.length; i++) {
            builder.append("id = ").append(arrayOfDevelopers[i].getID());
            if (i + 1 < arrayOfDevelopers.length)
                builder.append(" OR ");
        }

        query = "SELECT * FROM developers WHERE " + builder.toString();

        CommonMethods.checkQueryAndOut(query, new String[]{"id", "name", "salary"}, connection);

        return developersList;
    }
}
