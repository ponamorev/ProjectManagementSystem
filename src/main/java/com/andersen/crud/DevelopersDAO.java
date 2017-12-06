package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class DevelopersDAO {
    private Scanner reader = new Scanner(System.in);
    private String query, choice;
    private ResultSet result;
    private int count;
    private Developers developer;

    DevelopersDAO(Developers developer) {
        this.developer = developer;
    }


    private Developers checkDeveloper(int id, String name, int salary, Statement statement)
            throws SQLException, NullPointerException {
        Developers dev = null;

        if (id != 0) {
            query = "SELECT * FROM developers WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                dev = new Developers(result.getInt("id"),
                        result.getString("name"), result.getInt("salary"));
            return dev;
        } else if (!name.equals("") ||
                !Objects.equals(name, null)) {
            query = "SELECT * FROM developers WHERE name = '" + name + "'";
            result = statement.executeQuery(query);
            while (result.next())
                dev = new Developers(result.getInt("id"),
                        result.getString("name"), result.getInt("salary"));
            return dev;
        } else if (salary != 0) {
            query = "SELECT * FROM developers WHERE salary = " + salary + " LIMIT 1";
            result = statement.executeQuery(query);
            while (result.next())
                dev = new Developers(result.getInt("id"),
                        result.getString("name"), result.getInt("salary"));
            return dev;
        }

        return null;
    }

    private void unsuccessUpOrDel(Connection connection, Statement statement)
            throws SQLException {
        Developers dev;
        if (choice.equals("y") || choice.equals("Y")) {
            System.out.print("Enter the name: ");
            dev = new Developers(0, reader.nextLine(), 0);
            System.out.println("Add salary? (Y/any key)");
            choice = reader.nextLine();
            if (choice.equals("y") || choice.equals("Y")) {
                System.out.print("Enter the salary: ");
                dev.setSalary(reader.nextInt());
                reader.nextLine();
            }
            createDeveloper(dev, connection, statement);
        }
    }

    void addDeveloperLink(Developers developer, String table,
                          Connection connection, Statement statement) throws SQLException {
        String tableObjectName = "", columns[] = new String[1];
        switch (table) {
            case "skills":
                tableObjectName = "skill";
                columns = new String[]{"id", "specialty"};
                break;
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
                query = "INSERT INTO developers_" + table + " VALUES (" + developer.getID() +
                        ", " + choice + ")";
                System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                choice = reader.nextLine();
            } else {
                if (table.equals("skills"))
                    query = "SELECT id FROM " + table + " WHERE specialty = '" + choice + "'";
                else query = "SELECT id FROM " + table + " WHERE name = '" + choice + "'";

                result = statement.executeQuery(query);
                int id = 0;
                while (result.next())
                    id = result.getInt(1);

                if (id != 0) {
                    query = "INSERT INTO developers_" + table + " VALUES (" + developer.getID() +
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


    void createDeveloper(Developers developer, Connection connection, Statement statement)
            throws SQLException, NullPointerException {
        String ID, name;
        int salary, id;

        if (developer == null) {

            System.out.print("Enter ID (optional, click enter for skip): ");
            ID = reader.nextLine();
            if (ID.equals(""))
                ID = "0";
            id = Integer.parseInt(ID);

            System.out.print("Enter the name: ");
            name = reader.nextLine();

            System.out.println("Would you like to add a salary? (Y/any key)");
            choice = reader.nextLine();
            if (choice.equals("y") || choice.equals("Y")) {
                System.out.print("Enter a salary: ");
                salary = reader.nextInt();
                reader.nextLine();
            } else salary = 0;

            developer = new Developers(id, name, salary);

        } else id = developer.getID();

        // Check name
        query = "SELECT COUNT(name) FROM developers WHERE name = '" + developer.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);
        if (count > 0) {
            System.out.println("The table already exists the developer" +
                    " which has name '" + developer.getName() + "'.");
            return;
        }

        // Check ID
        if (id != 0) {
            count = 0;
            query = "SELECT COUNT(id) FROM developers WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                while (count != 0) {
                    query = "SELECT COUNT(id) FROM developers WHERE id = " + ++id;
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                }
                System.out.println("Chosen ID is busied." +
                        " The new developer will have ID = " + id);
                developer.setID(id);

                query = "INSERT INTO developers VALUES (" + id + ", '" + developer.getName() +
                        "', " + developer.getSalary() + ")";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
            }
        } else {
            query = "INSERT INTO developers VALUES (NULL, '" + developer.getName() +
                    "', " + developer.getSalary() + ")";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
        }

        query = "SELECT * FROM developers WHERE name = '" + developer.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            this.developer = new Developers(result.getInt("id"),
                    result.getString("name"), result.getInt("salary"));

        addDeveloperLink(this.developer, "skills", connection, statement);

        addDeveloperLink(this.developer, "companies", connection, statement);

        System.out.println("Would you like to add another developer? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y"))
            createDeveloper(null, connection, statement);
    }


    void updateDeveloper(Connection connection, Statement statement)
            throws SQLException {
        if (developer == null) {
            DevelopersView view = new DevelopersView();
            developer = view.findDeveloper(connection, reader);
        }

        StringBuilder builder = new StringBuilder();
        boolean flag = false;

        CommonMethods.printTableColumns("developers", connection);
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

        if (developer != null) {
            query = "UPDATE developers SET " + builder.toString() + " WHERE id = " + developer.getID();
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            System.out.println("The developer wasn't found. Would you like " +
                    "to add a new developer? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }

    }


    // return the developer which was deleted
    void deleteDeveloper(Developers developer, Connection connection, Statement statement)
            throws SQLException {

        if (developer == null)
            if (this.developer != null)
                developer = this.developer;
            else
                developer = null;

        if (developer != null) {
            query = "SELECT * FROM developers WHERE id = " + developer.getID();
            result = statement.executeQuery(query);
            while (result.next()) {
                this.developer = new Developers(result.getInt("id"),
                        result.getString("name"), result.getInt("salary"));
            }

            query = "DELETE FROM developers_skills WHERE id_developer = " + this.developer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developers_" +
                    "skills..");
            query = "DELETE FROM developers_projects WHERE id_developer = " + this.developer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developers_" +
                    "projects..");
            query = "DELETE FROM developers_companies WHERE id_developer = " + this.developer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developers_" +
                    "companies..");

            query = "DELETE FROM developers WHERE id = " + this.developer.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from the developers..");
        } else {
            System.out.println("The developer wasn't found. Would you like " +
                    "to add a new developer? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }
    }
}
