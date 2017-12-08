package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

class ProjectsView {
    private String query;


    Projects findProject(Connection connection, Scanner reader)
            throws SQLException {
        String query = "SELECT * FROM projects";
        Projects project = null;

        CommonMethods.checkQueryAndOut(query, new String[] {"id", "name", "description",
                "start", "deadline", "cost"}, connection);
        System.out.print("Choose the ID of the project: ");
        int id = reader.nextInt();
        reader.nextLine();

        query = "SELECT * FROM projects WHERE id = " + id;
        ResultSet result = connection.createStatement().executeQuery(query);
        while (result.next())
            project = new Projects(result.getInt("id"),
                    result.getString("name"), result.getString("description"),
                    result.getString("start"), result.getString("deadline"),
                    result.getInt("cost"));

        return project;
    }


    private LinkedList<Projects> findManyProjects(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Projects> projectsList = new LinkedList<>();
        Scanner reader = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        ResultSet result;
        String value, choice;
        boolean flag = false;

        System.out.println("Output all table? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            query = "SELECT * FROM projects";
            result = statement.executeQuery(query);
            while (result.next()) {
                projectsList.add(new Projects(result.getInt("id"),
                        result.getString("name"), result.getString("description"),
                        result.getString("start"), result.getString("deadline"),
                        result.getInt("cost")));
            }

            return projectsList;
        }
        else {

            CommonMethods.printTableColumns("projects", connection);

            System.out.println("Enter one and more values or range of values.\n" +
                    "If you enter string or date value you will write this value " +
                    "in follow format: string_column = 'some_value'.\nFor example, " +
                    "'column = value' or 'column1 = value1, column2 > value2' or " +
                    "'column BETWEEN min_value AND max_value'..");
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
                query = "SELECT * FROM projects";
            else
                query = "SELECT * FROM projects WHERE " + builder.toString();

            result = statement.executeQuery(query);
            while (result.next()) {
                projectsList.add(new Projects(result.getInt("id"),
                        result.getString("name"), result.getString("description"),
                        result.getString("start"), result.getString("deadline"),
                        result.getInt("cost")));
            }

            return projectsList;
        }
    }

    LinkedList<Projects> readProjects(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Projects> projectsList;
        StringBuilder builder = new StringBuilder();
        projectsList = findManyProjects(connection, statement);

        Projects[] arrayOfProjects;
        arrayOfProjects = projectsList.toArray(new Projects[0]);

        for (int i = 0; i < arrayOfProjects.length; i++) {
            builder.append("id = ").append(arrayOfProjects[i].getID());
            if (i + 1 < arrayOfProjects.length)
                builder.append(" OR ");
        }

        query = "SELECT * FROM projects WHERE " + builder.toString();

        CommonMethods.checkQueryAndOut(query, new String[]{"id", "name", "description",
                "start", "deadline", "cost"}, connection);

        return projectsList;
    }
}
