package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class CompaniesDAO {
    private Scanner reader = new Scanner(System.in);
    private String query, choice;
    private ResultSet result;
    private int count;
    private Companies company;

    CompaniesDAO(Companies company) {
        this.company = company;
    }


    private void unsuccessUpOrDel(Connection connection, Statement statement)
            throws SQLException {
        Companies comp;
        if (choice.equals("y") || choice.equals("Y")) {
            System.out.print("Enter the name: ");
            comp = new Companies(0, reader.nextLine(), "");
            System.out.println("Add address? (Y/any key)");
            choice = reader.nextLine();
            if (choice.equals("y") || choice.equals("Y")) {
                System.out.print("Enter the salary: ");
                comp.setAddress(reader.nextLine());
            }
            createCompany(comp, connection, statement);
        }
    }

    void addCompanyLink(Companies company, String table,
                        Connection connection, Statement statement) throws SQLException {
        String tableObjectName = "", columns[] = new String[1];
        switch (table) {
            case "developers":
                tableObjectName = "developer";
                columns = new String[]{"id", "name", "salary"};
                break;
            case "projects":
                tableObjectName = "project";
                columns = new String[]{"id", "name", "description", "start", "deadline", "cost"};
                break;
            case "customers":
                tableObjectName = "customer";
                columns = new String[]{"id", "name"};
                break;
        }

        query = "SELECT * FROM " + table;
        CommonMethods.checkQueryAndOut(query, columns, connection);
        do {
            System.out.println("Enter the " + tableObjectName + "..");
            choice = reader.nextLine();
            if (CommonMethods.isNumber(choice)) {
                if (table.equals("developers"))
                    query = "INSERT INTO " + table + "_companies VALUES (" + choice +
                            ", " + company.getID() + ")";
                else query = "INSERT INTO companies_" + table + " VALUES (" + company.getID() +
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
                    if (table.equals("developers"))
                        query = "INSERT INTO " + table + "_companies VALUES (" + id +
                                ", " + company.getID() + ")";
                    else query = "INSERT INTO companies_" + table + " VALUES (" + company.getID() +
                            ", " + id + ")";
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


    void createCompany(Companies company, Connection connection, Statement statement)
            throws SQLException {
        String ID, name, address;
        int id = 0;
        if (company == null) {

            do {
                System.out.print("Enter ID (optional, click enter for skip): ");
                ID = reader.nextLine();
                if (ID.equals(""))
                    ID = "0";
                if (CommonMethods.isNumber(ID))
                    id = Integer.parseInt(ID);
                else System.out.println("You didn't write a number! Try again.");
            } while (!CommonMethods.isNumber(ID));

            System.out.print("Enter the name: ");
            name = reader.nextLine();

            System.out.print("Enter the address: ");
            address = reader.nextLine();

            company = new Companies(id, name, address);
        } else
            id = company.getID();

        // Check name
        query = "SELECT COUNT(name) FROM companies WHERE name = '" + company.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);
        if (count > 0) {
            System.out.println("The table already exists the company" +
                    " which has name '" + company.getName() + "'.");
            return;
        }

        // Check ID
        if (id != 0) {
            count = 0;
            query = "SELECT COUNT(id) FROM companies WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                while (count != 0) {
                    query = "SELECT COUNT(id) FROM companies WHERE id = " + ++id;
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                }
                System.out.println("This ID is busied." +
                        " The new company will have ID = " + id);
                company.setID(id);
            }

            query = "INSERT INTO companies VALUES (" + id + ", '" + company.getName() +
                    "', '" + company.getAddress() + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            query = "INSERT INTO companies VALUES (NULL, '" + company.getName() +
                    "', '" + company.getAddress() + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
        }

        query = "SELECT * FROM companies WHERE name = '" + company.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            this.company = new Companies(result.getInt("id"),
                    result.getString("name"), result.getString("address"));

        addCompanyLink(this.company, "developers", connection, statement);

        addCompanyLink(this.company, "projects", connection, statement);

        addCompanyLink(this.company, "customers", connection, statement);

        System.out.println("Would you like to add another company? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y"))
            createCompany(null, connection, statement);
    }


    void updateCompany(Connection connection, Statement statement)
            throws SQLException {
        if (company == null) {
            CompaniesView view = new CompaniesView();
            company = view.findCompany(connection, reader);
        }

        StringBuilder builder = new StringBuilder();
        boolean flag = false;

        CommonMethods.printTableColumns("companies", connection);
        while (!flag) {
            System.out.println("Choose a column to change (except ID)..");
            builder.append(reader.nextLine());
            System.out.println("Enter a new value. If you chose a string column, you should write a new value " +
                    "in single quotes.");
            builder.append(" = ").append(reader.nextLine());
            System.out.println("Would you like to change another column? (Y/any key)");
            choice = reader.nextLine();
            if (!choice.equals("y") && !choice.equals("Y"))
                flag = true;
            else builder.append(", ");
        }

        if (company != null) {
            query = "UPDATE companies SET " + builder.toString() + " WHERE id = " + company.getID();
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            System.out.println("The company wasn't found. Would you like " +
                    "to add a new company? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }

    }


    // return the developer which was deleted
    void deleteCompany(Companies company, Connection connection, Statement statement)
            throws SQLException {

        if (company == null)
            if (this.company != null)
                company = this.company;
            else
                company = null;

        if (company != null) {
            query = "SELECT * FROM companies WHERE id = " + company.getID();
            result = statement.executeQuery(query);
            while (result.next())
                this.company = new Companies(result.getInt("id"),
                        result.getString("name"), result.getString("address"));


            query = "DELETE FROM companies_projects WHERE id_company = " + this.company.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from companies_" +
                    "projects..");
            query = "DELETE FROM developers_companies WHERE id_company = " + this.company.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developers_" +
                    "companies..");
            query = "DELETE FROM companies_customers WHERE id_company = " + this.company.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from companies_" +
                    "customers..");

            query = "DELETE FROM companies WHERE id = " + this.company.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from the companies..");
        } else {
            System.out.println("The company wasn't found. Would you like " +
                    "to add a new company? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }
    }
}
