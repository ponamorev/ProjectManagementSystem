package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

public class CustomersView {
    private String query;


    Customers findCustomer(Connection connection, Scanner reader)
            throws SQLException {
        String query = "SELECT * FROM customers";
        Customers customer = null;

        CommonMethods.checkQueryAndOut(query, new String[] {"id", "name"}, connection);
        System.out.print("Choose the ID of the customer: ");
        int id = reader.nextInt();
        reader.nextLine();

        query = "SELECT * FROM customers WHERE id = " + id;
        ResultSet result = connection.createStatement().executeQuery(query);
        while (result.next())
            customer = new Customers(result.getInt("id"),
                    result.getString("name"));

        return customer;
    }


    private LinkedList<Customers> findManyCustomers(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Customers> customersList = new LinkedList<>();
        Scanner reader = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        ResultSet result;
        String value, choice;
        boolean flag = false;

        System.out.println("Output all table? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y")) {
            query = "SELECT * FROM customers";
            result = statement.executeQuery(query);
            while (result.next()) {
                customersList.add(new Customers(result.getInt("id"), result.getString("name")));
            }

            return customersList;
        }
        else {

            CommonMethods.printTableColumns("customers", connection);

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
                query = "SELECT * FROM customers";
            else
                query = "SELECT * FROM customers WHERE " + builder.toString();

            result = statement.executeQuery(query);
            while (result.next()) {
                customersList.add(new Customers(result.getInt("id"), result.getString("name")));
            }

            return customersList;
        }
    }

    LinkedList<Customers> readCustomers(Connection connection, Statement statement)
            throws SQLException {
        LinkedList<Customers> customersList;
        StringBuilder builder = new StringBuilder();
        customersList = findManyCustomers(connection, statement);

        Customers[] arrayOfCustomers;
        arrayOfCustomers = customersList.toArray(new Customers[0]);

        for (int i = 0; i < arrayOfCustomers.length; i++) {
            builder.append("id = ").append(arrayOfCustomers[i].getID());
            if (i + 1 < arrayOfCustomers.length)
                builder.append(" OR ");
        }

        query = "SELECT * FROM customers WHERE " + builder.toString();

        CommonMethods.checkQueryAndOut(query, new String[]{"id", "name", "address"}, connection);

        return customersList;
    }
}
