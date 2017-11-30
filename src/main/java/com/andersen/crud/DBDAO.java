package com.andersen.crud;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

class DBDAO {
    private static String query;
    private static int returnFromLinkExcep;
    private static Scanner reader = new Scanner(System.in);
    private static final String textForConditions = "Enter consistently the conditions " +
            "(separated by a line separator).\nThe conditions must be written in" +
            " the following format: 'table.column =/LIKE/>/</>=/<=/<>/IN some_value' " +
            "\nor 'table.column BETWEEN min_value AND max_value')." +
            "\nFor combine conditions must use 'AND' or 'OR'." +
            "\nIf you want to use subquery, you need to remember the SQL-syntax." +
            "\nIf you compare string field, value for comparing must be enclosed " +
            "in quotation marks.\nFor stop enter 'q'.";


    /**
     * This method adds a new records in a one of the main tables
     * Main tables: developers, skills, projects, companies, customers
     *
     * @param table
     * @param connection
     * @param statement
     * @throws SQLException
     */

    static void createRecords(String table, Connection connection, Statement statement)
            throws SQLException {
        boolean flag = false;
        String name;
        StringBuilder builder;
        ResultSet result;

        while (!flag) {
            builder = new StringBuilder();
            builder.append("INSERT INTO ").append(table).append(" VALUES (NULL, '");

            if (Objects.equals(table, "skills"))
                System.out.print("Enter the specialty: ");
            else System.out.print("Enter the name: ");

            name = reader.nextLine();
            if (!AccompanyingMethods.isExistName(name, table, statement)) {

                switch (table) {
                    // Add in the table "developers"!
                    case "developers":
                        boolean more = false;
                        String createDev[] = new String[2];
                        System.out.println("Do you want to add a salary? (Y/N)");
                        String choise = reader.nextLine();

                        if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {

                            System.out.print("Enter a salary: ");
                            int salary = reader.nextInt();
                            reader.nextLine();

                            builder.append(name).append("', ").append(salary).append(")");
                        } else
                            builder.append(name).append("')");
                        // Adding in the table "developers"
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more skills for the developer.\n" +
                                "List of skills is shown below.");
                        query = "SELECT * FROM skills";
                        String columnsSkill[] = {"id", "specialty"};
                        AccompanyingMethods.checkQueryAndOut(query, columnsSkill, connection);
                        builder.append("INSERT INTO developers_skills VALUES (");
                        query = "SELECT id FROM developers WHERE name = '" + name + "'";
                        result = statement.executeQuery(query);
                        int ID_dev = 0;
                        while (result.next())
                            ID_dev = result.getInt(1);
                        while (!more) {
                            System.out.print("\nID or name of the skill: ");
                            String skill = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(skill)) {
                                query = "SELECT id FROM skills WHERE specialty = '" + skill + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    skill = result.getString(1);
                            }
                            builder.append(ID_dev).append(", ").append(skill).append(")");
                            System.out.println("Do you want to add another skill? (Y/N)");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Adding in the table "developers_skills"
                        createDev[0] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more companies where the developer is working.\n" +
                                "List of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        String columnsCompany[] = {"id", "name", "address"};
                        AccompanyingMethods.checkQueryAndOut(query, columnsCompany, connection);
                        builder.append("INSERT INTO developers_companies VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the company: ");
                            String company = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_dev).append(", ").append(company).append(")");
                            System.out.println("Do you want to add another company? (Y/N)");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Adding in the table "developers_companies"
                        createDev[1] = builder.toString();
                        builder.delete(0, builder.length());

                        System.out.println(statement.executeUpdate(createDev[0]) +
                                statement.executeUpdate(createDev[1]) + " row(-s) changed.\n");
                        break;

                    // Add in the table "projects"!
                    case "projects":
                        String description = "NULL", createProject[] = new String[3];
                        System.out.println("Do you want to add a description about the project? (Y/N)");
                        choise = reader.nextLine();

                        if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {
                            System.out.println("Write the description below..");
                            description = reader.nextLine();
                            description = "'" + description + "'";
                        }

                        System.out.println("You should enter the start and the deadline of the project. " +
                                "Please, write it in format 'YYYY-MM-DD'.\nIf you don't know accurately day" +
                                " of start or end of the projects, you should enter the first day of the month.");

                        System.out.print("Enter the date of start of the project: ");
                        String start = reader.nextLine();
                        start = AccompanyingMethods.checkDate(start);


                        System.out.print("Enter the date of end of the project: ");
                        String end = reader.nextLine();
                        end = AccompanyingMethods.checkDate(end);

                        // entering cost of project
                        System.out.print("Enter the cost of the project: ");
                        int cost = reader.nextInt();

                        builder.append(name).append("', ").append(description);
                        builder.append(", '").append(start).append("', '").append(end);
                        builder.append("', ").append(cost).append(")");
                        // Adding in the table "projects"
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more skills which are necessary" +
                                " for working with the project.\nList of skills is shown below.");
                        query = "SELECT * FROM skills";
                        columnsSkill = new String[]{"id", "specialty"};
                        AccompanyingMethods.checkQueryAndOut(query, columnsSkill, connection);
                        builder.append("INSERT INTO projects_skills VALUES (");
                        query = "SELECT id FROM projects WHERE name = '" + name + "'";
                        result = statement.executeQuery(query);
                        int ID_project = 0;
                        while (result.next())
                            ID_project = result.getInt(1);
                        more = false;
                        while (!more) {
                            System.out.print("\nID or name of the skill: ");
                            String skill = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(skill)) {
                                query = "SELECT id FROM skills WHERE specialty = '" + skill + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    skill = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(skill).append(")");
                            System.out.print("Do you want to add another skill? (Y/N)\t");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Adding in the table "developers_skills"
                        createProject[0] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more companies which work with the project.\n" +
                                "List of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        columnsCompany = new String[]{"id", "name", "address"};

                        AccompanyingMethods.checkQueryAndOut(query, columnsCompany, connection);
                        builder.append("INSERT INTO companies_projects VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the company: ");
                            String company = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(company).append(")");
                            System.out.println("Do you want to add another company? (Y/N)");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Adding in the table "developers_companies"
                        createProject[1] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more customers which ordered this project.\n" +
                                "List of the customer is shown below.");
                        query = "SELECT * FROM customers";
                        String columnsCustomer[] = {"id", "name"};
                        AccompanyingMethods.checkQueryAndOut(query, columnsCustomer, connection);
                        builder.append("INSERT INTO customers_projects VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the customer: ");
                            String customer = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(customer)) {
                                query = "SELECT id FROM customers WHERE name = '" + customer + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    customer = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(customer).append(")");
                            System.out.println("Do you want to add another customer? (Y/N)");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Addind in the table "developers_companies"
                        createProject[2] = builder.toString();
                        builder.delete(0, builder.length());

                        System.out.println(statement.executeUpdate(createProject[0]) +
                                statement.executeUpdate(createProject[1]) + statement.executeUpdate(createProject[2]) +
                                " row(-s) changed.\n");
                        break;


                    // Add in the table "customers"
                    case "customers":
                        String createCustomer[] = new String[1];
                        builder.append(name).append(")");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");


                        System.out.println("Please, add one or more companies which are working" +
                                "with this customer.\nList of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        columnsCompany = new String[]{"id", "name", "address"};
                        AccompanyingMethods.checkQueryAndOut(query, columnsCompany, connection);
                        query = "SELECT id FROM customers WHERE name = '" + name + "'";
                        result = statement.executeQuery(query);
                        int ID_customer = 0;
                        while (result.next())
                            ID_customer = result.getInt(1);
                        builder.append("INSERT INTO companies_customers VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the company: ");
                            String company = reader.nextLine();
                            if (!AccompanyingMethods.isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_customer).append(", ").append(company).append(")");
                            System.out.println("Do you want to add another company? (Y/N)");
                            choise = reader.nextLine();
                            more = !(Objects.equals(choise, "y") || Objects.equals(choise, "Y"));
                            if (!more) builder.append(", (");
                        }
                        // Addind in the table "companies_customers"
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");

                        break;

                    // Add in the table "companies"!
                    case "companies":
                        System.out.print("Enter an address: ");
                        String address = reader.nextLine();

                        builder.append(name).append("', '").append(address).append("')");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");
                        break;


                    // Other tables!
                    default:
                        builder.append(name).append("')");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.\n");
                        break;
                }

                flag = true;
            } else {
                System.out.println("\nThis record is already contained in the table.");
                returnFromLinkExcep = AccompanyingMethods.linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }


    /**
     * This method adds a link between a table1 and a table2 in a resultTable
     * table1 and table2 are some of the main tables resultTable is the link table
     * Link tables:
     * - developers_skills
     * - developers_projects
     * - developers_companies
     * - projects_skills
     * - companies_projects
     * - companies_customers
     * - customers_projects
     *
     * @param table1
     * @param table2
     * @param statement
     * @throws SQLException
     * @throws NumberFormatException
     */

    static void createLinkRecords(String table1, String table2, Statement statement)
            throws SQLException, NumberFormatException {
        ResultSet result;
        int ID1 = 0, ID2 = 0, count;
        String name1, name2, fields[], resultTable = table1 + "_" + table2;
        boolean flag = false;

        while (!flag) {
            try {
                System.out.print("Enter the name or the ID from the first table: ");
                name1 = reader.nextLine();
                if (AccompanyingMethods.isNumber(name1))
                    ID1 = Integer.parseInt(name1);
                if (ID1 == 0) {
                    if (!AccompanyingMethods.isExistName(name1, table1, statement))
                        throw new MySQLIntegrityConstraintViolationException();
                } else if (!AccompanyingMethods.isExistID(ID1, table1, statement))
                    throw new MySQLIntegrityConstraintViolationException();

                System.out.print("Enter the name or the ID from the second table: ");
                name2 = reader.nextLine();
                if (AccompanyingMethods.isNumber(name2))
                    ID2 = Integer.parseInt(name2);
                if (ID2 == 0) {
                    if (!AccompanyingMethods.isExistName(name2, table2, statement))
                        throw new MySQLIntegrityConstraintViolationException();
                } else if (!AccompanyingMethods.isExistID(ID2, table2, statement))
                    throw new MySQLIntegrityConstraintViolationException();


                switch (resultTable) {

                    case "developers_projects":

                        query = "SELECT id_developer FROM developers_skills WHERE id_developer = " + ID1 + " AND " +
                                "id_skill IN (SELECT id_skill FROM projects_skills WHERE id_project = " + ID2 + ")";
                        result = statement.executeQuery(query);
                        if (!result.next())
                            throw new SQLException("Skill");

                        query = "SELECT id_developer FROM developers_companies WHERE id_developer = " + ID1 + " AND " +
                                "id_company IN (SELECT id_company FROM companies_projects WHERE id_project = " + ID2 + ")";
                        result = statement.executeQuery(query);
                        if (!result.next())
                            throw new SQLException("Company");
                        break;

                    case "companies_projects":
                        query = "SELECT id_company FROM companies_customers WHERE id_company = " + ID1 + " AND " +
                                "id_customer IN (SELECT id_customer FROM customers_projects WHERE id_project = " + ID2 + ")";
                        result = statement.executeQuery(query);
                        if (!result.next())
                            throw new SQLException("Customer");
                        break;
                }

                count = 0;
                fields = new String[2];
                query = "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = 'task_sql' AND " +
                        "table_name = '" + resultTable + "'";
                result = statement.executeQuery(query);
                while (result.next())
                    fields[count++] = result.getString(1);


                query = AccompanyingMethods.checkID(ID1, ID2, name1, name2, table1, table2,
                        resultTable, fields[0], fields[1], statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (NullPointerException e) {
                System.out.println("\nAdding is not success!");
                return;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = AccompanyingMethods.linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Skill")) {
                    System.out.println("\nChosen developer hasn't a necessary skills for working with chosen project.");
                    returnFromLinkExcep = AccompanyingMethods.linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "Company")) {
                    System.out.println("\nChosen developer doesn't work in the company which works with this project.");
                    returnFromLinkExcep = AccompanyingMethods.linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "Customer")) {
                    System.out.println("\nChosen company doesn't work with the customer which ordered the chosen project.");
                    returnFromLinkExcep = AccompanyingMethods.linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
            }
        }
    }


    /**
     * This method lets you read records from the tables
     *
     * @param listTables
     * @param listColumn
     * @param columns
     * @param connection
     * @throws SQLException
     */

    static void readTables(String listTables, String listColumn, String[] columns,
                           Connection connection)
            throws SQLException {
        String condition, allConditionsArr[];
        boolean flag = false;

        while (!flag) try {
            StringBuilder builder = new StringBuilder();

            // Select to conditions to search
            System.out.println(textForConditions);

            condition = reader.nextLine();
            while (!Objects.equals(condition, "q")) {
                builder.append(condition).append("\n");
                condition = reader.nextLine();
            }
            if (!Objects.equals(builder.toString(), "")) {
                allConditionsArr = builder.toString().split("\n");
                builder.delete(0, builder.length());
                condition = "";
                for (int i = 0; i < allConditionsArr.length; i++) {
                    condition += allConditionsArr[i];
                    if (i + 1 < allConditionsArr.length)
                        if (!Objects.equals(allConditionsArr[i].charAt(allConditionsArr[i].length() - 1), ' ') &
                                !Objects.equals(allConditionsArr[i + 1].charAt(0), ' '))
                            condition += " ";
                }
            } else condition = "";


            // Query!
            if (!Objects.equals(condition, ""))
                query = "SELECT " + listColumn + " FROM " + listTables + " WHERE " + condition;
            else query = "SELECT " + listColumn + " FROM " + listTables;


            AccompanyingMethods.checkQueryAndOut(query, columns, connection);
            flag = true;

        } catch (SQLException e) {
            if (Objects.equals(e.getMessage(), "Column")) {
                System.out.println("\nYou wrote nonexistent column.");
            } else if (Objects.equals(e.getMessage(), "Table")) {
                System.out.println("\nYou wrote nonexistent table.");
            } else System.out.println("\nSyntax error was made. Maybe, " +
                    "you wrote some column or condition wrong.");
            returnFromLinkExcep = AccompanyingMethods.linkExcep();
            if (returnFromLinkExcep == 2) return;
        }
    }


    /**
     * This method updates values in the tables
     *
     * @param listTables
     * @param statement
     * @throws SQLException
     */

    static void updateRecords(String listTables, Statement statement)
            throws SQLException {
        String condition, allConditions[], value, allValues[];
        boolean flag = false;


        while (!flag) {
            try {
                StringBuilder builder = new StringBuilder();

                // Enter the condition to search records to update
                System.out.println(textForConditions);
                condition = reader.nextLine();
                while (!Objects.equals(condition, "q")) {
                    builder.append(condition).append("\n");
                    condition = reader.nextLine();
                }
                allConditions = builder.toString().split("\n");
                condition = "";
                for (int i = 0; i < allConditions.length; i++) {
                    condition += allConditions[i];
                    if (i + 1 < allConditions.length)
                        if (!Objects.equals(allConditions[i].charAt(allConditions[i].length() - 1), ' ') &
                                !Objects.equals(allConditions[i + 1].charAt(0), ' '))
                            condition += " ";
                }

                // enter a new values
                System.out.println("Enter a new value(-s). For stop enter 'q'.");
                value = reader.nextLine();
                while (!Objects.equals(value, "q")) {
                    builder.append(value).append("\n");
                    value = reader.nextLine();
                }
                allValues = builder.toString().split("\n");

                query = "UPDATE " + listTables + " SET ";
                for (int i = 0; i < allValues.length; i++) {
                    query += allValues[i];
                    if (i + 1 < allValues.length)
                        query += ", ";
                }
                query += " WHERE " + condition;
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.");
                flag = true;

            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Column")) {
                    System.out.println("\nYou wrote nonexistent column.");
                } else if (Objects.equals(e.getMessage(), "Table")) {
                    System.out.println("\nYou wrote nonexistent table.");
                } else System.out.println("\nSyntax error was made. Maybe, " +
                        "you wrote some column or condition wrong.");
                returnFromLinkExcep = AccompanyingMethods.linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }


    /**
     * This method deletes values from the tables. If tables are main, all links must be deleted automatically
     *
     * @param listTables
     * @param condition
     * @param statement
     * @throws SQLException
     */

    static int deleteRecords(String listTables, String condition, Statement statement)
            throws SQLException {
        String newCondition;
        int flag = 1;

        try {

            query = "DELETE FROM " + listTables + " WHERE " + condition;
            System.out.println(statement.executeUpdate(query) + " row(-s) deleted.");

            flag = 0;

        } catch (MySQLIntegrityConstraintViolationException e) {
            if (listTables.contains("developers,") || listTables.contains("developers ") ||
                    listTables.equals("developers")) {

                newCondition = AccompanyingMethods.newConditions(condition, "developers");

                try {

                    deleteRecords("developers_skills", "id_developer IN (SELECT id " +
                            "FROM developers WHERE " + newCondition + ")", statement);
                    deleteRecords("developers_projects", "id_developer IN (SELECT id " +
                            "FROM developers WHERE " + newCondition + ")", statement);
                    deleteRecords("developers_companies", "id_developer IN (SELECT id " +
                            "FROM developers WHERE " + newCondition + ")", statement);

                } catch (NullPointerException nullE) {
                    System.out.println("You wrote wrond comditions.");
                }

            } else if (listTables.contains(" projects,") || listTables.contains(" projects ") ||
                    listTables.equals("projects")) {

                newCondition = AccompanyingMethods.newConditions(condition, "projects");

                try {

                    deleteRecords("developers_projects", "id_project IN (SELECT id " +
                            "FROM projects WHERE " + newCondition + ")", statement);
                    deleteRecords("projects_skills", "id_project IN (SELECT id " +
                            "FROM projects WHERE " + newCondition + ")", statement);
                    deleteRecords("companies_projects", "id_project IN (SELECT id " +
                            "FROM projects WHERE " + newCondition + ")", statement);
                    deleteRecords("customers_projects", "id_project IN (SELECT id " +
                            "FROM projects WHERE " + newCondition + ")", statement);

                } catch (NullPointerException nullE) {
                    System.out.println("You wrote wrond comditions.");
                }


            } else if (listTables.contains(" skills,") || listTables.contains(" skills ") ||
                    listTables.equals("skills")) {

                newCondition = AccompanyingMethods.newConditions(condition, "skills");

                try {

                    deleteRecords("developers_skills", "id_skill IN (SELECT id " +
                            "FROM skills WHERE " + newCondition + ")", statement);
                    deleteRecords("projects_skills", "id_skill IN (SELECT id " +
                            "FROM skills WHERE " + newCondition + ")", statement);

                } catch (NullPointerException nullE) {
                    System.out.println("You wrote wrond comditions.");
                }

            } else if (listTables.contains(" companies,") || listTables.contains(" companies ") ||
                    listTables.equals("companies")) {

                newCondition = AccompanyingMethods.newConditions(condition, "companies");

                try {

                    deleteRecords("developers_companies", "id_company IN (SELECT id " +
                            "FROM companies WHERE " + newCondition + ")", statement);
                    deleteRecords("companies_projects", "id_company IN (SELECT id " +
                            "FROM companies WHERE " + newCondition + ")", statement);
                    deleteRecords("companies_customers", "id_company IN (SELECT id " +
                            "FROM companies WHERE " + newCondition + ")", statement);

                } catch (NullPointerException nullE) {
                    System.out.println("You wrote wrond comditions.");
                }

            } else if (listTables.contains(" customers,") || listTables.contains(" customers ") ||
                    listTables.equals("customers")) {

                newCondition = AccompanyingMethods.newConditions(condition, "customers");

                try {

                    deleteRecords("customers_projects", "id_customer IN (SELECT id " +
                            "FROM customers WHERE " + newCondition + ")", statement);
                    deleteRecords("companies_customers", "id_customer IN (SELECT id " +
                            "FROM customers WHERE " + newCondition + ")", statement);

                } catch (NullPointerException nullE) {
                    System.out.println("You wrote wrond comditions.");
                }

            }
        } catch (SQLException e) {
            System.out.println("\nSyntax error was made. Maybe, " +
                    "you wrote some column or condition wrong.");
            returnFromLinkExcep = AccompanyingMethods.linkExcep();
            if (returnFromLinkExcep == 2) flag = 0;
        }

        return flag;
    }
}
