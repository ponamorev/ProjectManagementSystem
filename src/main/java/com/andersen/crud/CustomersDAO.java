package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class CustomersDAO {
    private Scanner reader = new Scanner(System.in);
    private String query, choice;
    private ResultSet result;
    private int count;
    private Customers customer;

    CustomersDAO(Customers customer) {
        this.customer = customer;
    }


    private Customers checkCustomer(int id, String name, Statement statement)
            throws SQLException, NullPointerException {
        Customers cust = null;

        if (id != 0) {
            query = "SELECT * FROM customers WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                cust = new Customers(result.getInt("id"),
                        result.getString("name"));
            return cust;
        } else if (!name.equals("") ||
                !Objects.equals(name, null)) {
            query = "SELECT * FROM developers WHERE name = '" + name + "'";
            result = statement.executeQuery(query);
            while (result.next())
                cust = new Customers(result.getInt("id"),
                        result.getString("name"));
            return cust;
        }

        return null;
    }

    private void unsuccessUpOrDel(Connection connection, Statement statement)
            throws SQLException {
        if (choice.equals("y") || choice.equals("Y")) {
            System.out.print("Enter the name: ");
            customer = new Customers(0, reader.nextLine());

            createCustomer(customer, connection, statement);
        }
    }

    void addCustomerLink(Customers customer, String table,
                         Connection connection, Statement statement) throws SQLException {
        String tableObjectName = "", columns[] = new String[1];
        switch (table) {
            case "projects":
                tableObjectName = "project";
                columns = new String[]{"id", "name", "description", "start", "deadline", "cost"};
                break;
            case "companies":
                tableObjectName = "company";
                columns = new String[]{"id", "name", "address"};
                break;
        }

        query = "SELECT * FROM " + table;
        CommonMethods.checkQueryAndOut(query, columns, connection);
        do {
            System.out.println("Enter the " + tableObjectName + "..");
            choice = reader.nextLine();
            if (CommonMethods.isNumber(choice)) {
                if (table.equals("companies"))
                    query = "INSERT INTO " + table + "_customers VALUES (" + choice +
                            ", " + customer.getID() + ")";
                else query = "INSERT INTO customers_" + table + " VALUES (" + customer.getID() +
                        ", " + choice + ")";
                System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                choice = reader.nextLine();
            } else {
                query = "SELECT id FROM " + table + " WHERE name = '" + choice + "'";

                result = statement.executeQuery(query);
                int id = 0;
                while (result.next())
                    id = result.getInt(1);

                if (id != 0) {
                    if (table.equals("companies"))
                        query = "INSERT INTO " + table + "_customers VALUES (" + choice +
                                ", " + customer.getID() + ")";
                    else query = "INSERT INTO customers_" + table + " VALUES (" + customer.getID() +
                            ", " + choice + ")";
                    System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                    System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                    choice = reader.nextLine();
                } else {
                    System.out.println("You wrote non-exists " + tableObjectName + ".. ");
                    System.out.println("Would you like to try again? (Y/any key)");
                    choice = reader.nextLine();
                }
            }
        } while (choice.equals("y") || choice.equals("Y"));
    }


    void createCustomer(Customers customer, Connection connection, Statement statement)
            throws SQLException {
        String ID, name;
        int id;

        if (customer == null) {

            System.out.print("Enter ID (optional, click enter for skip): ");
            ID = reader.nextLine();
            if (ID.equals(""))
                ID = "0";
            id = Integer.parseInt(ID);

            System.out.print("Enter the name: ");
            name = reader.nextLine();

            customer = new Customers(id, name);

        } else id = customer.getID();

        // Check name
        query = "SELECT COUNT(name) FROM customers WHERE name = '" + customer.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);
        if (count > 0) {
            System.out.println("The table already exists the customer" +
                    " which has name '" + customer.getName() + "'.");
            return;
        }

        // Check ID
        if (id != 0) {
            count = 0;
            query = "SELECT COUNT(id) FROM customers WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                while (count != 0) {
                    query = "SELECT COUNT(id) FROM customers WHERE id = " + ++id;
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                }
                System.out.println("This ID is busied." +
                        " The new customer will have ID = " + id);
                customer.setID(id);

                query = "INSERT INTO customers VALUES (" + id + ", '" + customer.getName() + "')";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
            }
        } else {
            query = "INSERT INTO customers VALUES (NULL, '" + customer.getName() + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
        }

        query = "SELECT * FROM customers WHERE name = '" + customer.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            this.customer = new Customers(result.getInt("id"),
                    result.getString("name"));

        addCustomerLink(this.customer, "projects", connection, statement);

        addCustomerLink(this.customer, "companies", connection, statement);

        System.out.println("Would you like to add another customer? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y"))
            createCustomer(null, connection, statement);
    }


    void updateCustomer(Connection connection, Statement statement)
            throws SQLException {
        if (customer == null) {
            CustomersView view = new CustomersView();
            customer = view.findCustomer(connection, reader);
        }

        StringBuilder builder = new StringBuilder();

        CommonMethods.printTableColumns("customers", connection);
        System.out.println("Enter a new name. You should write it in single quotes.");
        builder.append("name").append(" = ").append(reader.nextLine());

        if (customer != null) {
            query = "UPDATE customers SET " + builder.toString() + " WHERE id = " + customer.getID();
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            System.out.println("The customer wasn't found. Would you like " +
                    "to add a new customer? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }

    }


    // return the customer which was deleted
    void deleteCustomer(Customers customer, Connection connection, Statement statement)
            throws SQLException {

        if (customer == null)
            if (this.customer != null)
                customer = this.customer;
            else customer = null;

        if (customer != null) {
            query = "SELECT * FROM customers WHERE id = " + customer.getID();
            result = statement.executeQuery(query);
            while (result.next())
                this.customer = new Customers(result.getInt("id"), result.getString("name"));

            query = "DELETE FROM customers_projects WHERE id_customer = " + this.customer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from customers_" +
                    "projects..");
            query = "DELETE FROM companies_customers WHERE id_customer = " + this.customer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from customers_" +
                    "companies..");

            query = "DELETE FROM customers WHERE id = " + this.customer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from the customers..");
        } else {
            System.out.println("The customer wasn't found. Would you like " +
                    "to add a new customer? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }
    }
}
