package com.andersen.crud;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class DBDAO {
    private static String query, choise;
    private static int returnFromLinkExcep;
    private static Scanner reader = new Scanner(System.in);

    private static boolean isNumber(String usingString) {
        if (usingString == null || usingString.isEmpty()) return false;
        for (int i = 0; i < usingString.length(); i++)
            if (!Character.isDigit(usingString.charAt(i))) return false;
        return true;
    }

    private static String checkDate(String date) throws NumberFormatException {
        boolean flag = false, check = false, rightDay = false;
        int year, month, day;

        do {
            try {
                while (!check)
                    check = date.length() == 10 && isNumber(date.substring(0, 4)) &&
                            date.charAt(4) == '-' && isNumber(date.substring(5, 7)) &&
                            date.charAt(7) == '-' && isNumber(date.substring(8));

                if (check) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8));
                    rightDay = (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 ||
                            month == 10 || month == 12) == (day >= 1 & day <= 31) ||
                            ((year % 4 == 0) & (month == 2) == (day >= 1 & day <= 29)) ||
                            ((year % 4 != 0) & (month == 2) == (day >= 1 & day <= 28)) ||
                            (month == 4 || month == 6 || month == 9 || month == 11) == (day >= 1 & day <= 30);
                }

                check = rightDay;
                if (!check) {
                    System.out.print("You wrote date in wrong format. Please, try again: ");
                    date = reader.nextLine();
                    continue;
                }

                flag = true;
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                System.out.println("Wrong format. Please, enter date again.");
                flag = false;
            }
        } while (!flag);

        return date;
    }

    private static String checkID(int ID1, int ID2, String name1, String name2, String DB1, String DB2,
                                  String DBforChange, String field1, String field2, Statement statement)
            throws SQLException {
        ResultSet result;
        int count;

        try {
            if (ID1 != 0) {
                if (ID2 != 0) {
                    query = "SELECT COUNT(" + field1 + ") FROM " + DBforChange + " WHERE " +
                            field1 + " = " + ID1 + " AND " + field2 + " = " + ID2;
                    result = statement.executeQuery(query);
                    while (result.next()) {
                        count = result.getInt(1);
                        if (count != 0)
                            throw new MySQLIntegrityConstraintViolationException("foundConcidience");
                    }
                    return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
                } else {
                    if (!Objects.equals(DB2, "skills"))
                        query = "SELECT id FROM " + DB2 + " WHERE name LIKE '" + name2 + "'";
                    else query = "SELECT id FROM " + DB2 + " WHERE specialty LIKE '" + name2 + "'";
                    result = statement.executeQuery(query);
                    while (result.next()) ID2 = result.getInt(1);

                    query = "SELECT COUNT(" + field1 + ") FROM " + DBforChange + " WHERE " +
                            field1 + " = " + ID1 + " AND " + field2 + " = " + ID2;
                    result = statement.executeQuery(query);
                    while (result.next()) {
                        count = result.getInt(1);
                        if (count != 0)
                            throw new MySQLIntegrityConstraintViolationException("foundConcidience");
                    }
                    return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
                }
            } else {
                if (ID2 != 0) {
                    if (!Objects.equals(DB1, "skills"))
                        query = "SELECT id FROM " + DB1 + " WHERE name LIKE '" + name1 + "'";
                    else query = "SELECT id FROM " + DB1 + " WHERE specialty LIKE '" + name1 + "'";
                    result = statement.executeQuery(query);
                    while (result.next()) ID1 = result.getInt(1);

                    query = "SELECT COUNT(" + field1 + ") FROM " + DBforChange + " WHERE " +
                            field1 + " = " + ID1 + " AND " + field2 + " = " + ID2;
                    result = statement.executeQuery(query);
                    while (result.next()) {
                        count = result.getInt(1);
                        if (count != 0)
                            throw new MySQLIntegrityConstraintViolationException("foundConcidience");
                    }
                    return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
                } else {
                    if (!Objects.equals(DB1, "skills"))
                        query = "SELECT id FROM " + DB1 + " WHERE name LIKE '" + name1 + "'";
                    else query = "SELECT id FROM " + DB1 + " WHERE specialty LIKE '" + name1 + "'";
                    result = statement.executeQuery(query);
                    while (result.next()) ID1 = result.getInt(1);

                    if (!Objects.equals(DB2, "skills"))
                        query = "SELECT id FROM " + DB2 + " WHERE name LIKE '" + name2 + "'";
                    else query = "SELECT id FROM " + DB2 + " WHERE specialty LIKE '" + name2 + "'";
                    result = statement.executeQuery(query);
                    while (result.next()) ID2 = result.getInt(1);

                    query = "SELECT COUNT(" + field1 + ") FROM " + DBforChange + " WHERE " +
                            field1 + " = " + ID1 + " AND " + field2 + " = " + ID2;
                    result = statement.executeQuery(query);
                    while (result.next()) {
                        count = result.getInt(1);
                        if (count != 0)
                            throw new MySQLIntegrityConstraintViolationException("foundConcidience");
                    }
                    return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
                }
            }
        } catch (MySQLIntegrityConstraintViolationException e) {
            if (Objects.equals(e.getMessage(), "foundConcidience")) {
                System.out.println("\nThis pair of values is already contained.");
                throw new MySQLIntegrityConstraintViolationException("alreadyExist");
            } else System.out.println("\nUnexpected exception");
            return "";
        }
    }

    private static int linkExcep() {
        System.out.println("Do you want to try again? (Y/N)");

        while (true) {
            choise = reader.nextLine();
            switch (choise) {
                case "y":
                case "Y":
                    return 1;
                case "n":
                case "N":
                    return 2;
                default:
                    System.out.println("Enter Y or N.");
                    break;
            }
        }
    }

    private static boolean isExistName(String name, String table, Statement statement)
            throws SQLException {
        ResultSet result;
        int count = 0;

        if (Objects.equals(table, "skills"))
            query = "SELECT COUNT(specialty) FROM skills WHERE specialty LIKE '" + name + "'";
        else query = "SELECT COUNT(name) FROM " + table + " WHERE name LIKE '" + name + "'";

        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);

        return count > 0;
    }

    private static boolean isExistID(int ID, String table, Statement statement)
            throws SQLException {
        ResultSet result;
        int count = 0;

        if (Objects.equals(table, "skills"))
            query = "SELECT COUNT(specialty) FROM skills WHERE id = " + ID;
        else query = "SELECT COUNT(name) FROM " + table + " WHERE id = " + ID;

        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);

        return count > 0;
    }

    static void checkQueryAndOut(String table, String testQuery, Statement statement)
            throws SQLException {
        ResultSet result;

        if (!Objects.equals(testQuery, "")) {
            int count = 0, quantity = 0;
            result = statement.executeQuery("SELECT COUNT(column_name) FROM INFORMATION_SCHEMA.COLUMNS" +
                    " WHERE table_schema = 'task_sql' AND table_name = '" + table + "'");
            while (result.next())
                quantity = result.getInt(1);
            result = statement.executeQuery(testQuery);
            while (result.next()) {
                while (count++ < quantity) {
                    System.out.print(result.getString(count));
                    System.out.print(" | ");
                }
                count = 0;
                System.out.println();
            }
        }
    }


    /**
     * @param table
     * @param statement
     * @throws SQLException This method adds a new records in a one of the main tables
     *                      Main tables: developers, skills, projects, companies, customers
     */
    static void addRecords(String table, Statement statement)
            throws SQLException {
        boolean flag = false;
        String name;
        StringBuilder builder;

        while (!flag) {
            builder = new StringBuilder();
            builder.append("INSERT INTO ").append(table).append(" VALUES (NULL, '");

            if (Objects.equals(table, "skills"))
                System.out.print("Enter the specialty: ");
            else System.out.print("Enter the name: ");

            name = reader.nextLine();
            if (!isExistName(name, table, statement)) {

                switch (table) {
                    // Add in the table "developers"!
                    case "developers":
                        System.out.println("Do you want to add a salary? (Y/N)");
                        choise = reader.nextLine();

                        if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {

                            System.out.print("Enter a salary: ");
                            int salary = reader.nextInt();
                            reader.nextLine();

                            builder.append(name).append("', ").append(salary).append(")");
                            System.out.println(statement.executeUpdate(builder.toString()) +
                                    " row(-s) changed.");
                        } else {

                            builder.append(name).append("')");
                            System.out.println(statement.executeUpdate(builder.toString()) +
                                    " row(-s) changed.");
                        }
                        break;

                    // Add in the table "projects"!
                    case "projects":
                        String description = "NULL";
                        System.out.println("Do you want to add a description about the project? (Y/N)");
                        choise = reader.nextLine();

                        if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {
                            System.out.println("Write the description below:");
                            description = reader.nextLine();
                            description = "'" + description + "'";
                        }

                        System.out.println("You should enter the start and the deadline of the project. " +
                                "Please, write it in format 'YYYY-MM-DD'.\nIf you don't know accurately day" +
                                " of start or end of the projects, you should enter the first day of the month.");

                        System.out.print("Enter the date of start of the project: ");
                        String start = reader.nextLine();
                        start = checkDate(start);


                        System.out.print("Enter the date of end of the project: ");
                        String end = reader.nextLine();
                        end = checkDate(end);

                        // entering cost of project
                        System.out.print("Enter the cost of the project: ");
                        int cost = reader.nextInt();

                        builder.append(name).append("', ").append(description);
                        builder.append(", '").append(start).append("', '").append(end);
                        builder.append("', ").append(cost).append(")");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");
                        break;

                    // Add in the table "companies"!
                    case "companies":
                        System.out.print("Enter an address: ");
                        String address = reader.nextLine();

                        builder.append(name).append("', '").append(address).append("')");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");
                        break;


                    // Other tables!
                    default:
                        builder.append(name).append("')");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");
                        break;
                }

                flag = true;
            } else {
                System.out.println("This record is already contained in the table.");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }


    /**
     * @param table1
     * @param table2
     * @param statement
     * @throws SQLException
     * @throws NumberFormatException This method adds a link between a table1 and a table2 in a resultTable
     *                               table1 and table2 are some of the main tables
     *                               resultTable is the link table
     *                               Link tables:
     *                               developers_skills
     *                               developers_projects
     *                               developers_companies
     *                               projects_skills
     *                               companies_projects
     *                               companies_customers
     *                               customers_projects
     */

    static void addLinkRecords(String table1, String table2, Statement statement)
            throws SQLException, NumberFormatException {
        ResultSet result;
        int ID1 = 0, ID2 = 0, count;
        String name1, name2, fields[], resultTable = table1 + "_" + table2;
        boolean flag = false;

        while (!flag) {
            try {
                System.out.print("Enter the name or the ID from the first table: ");
                name1 = reader.nextLine();
                if (isNumber(name1))
                    ID1 = Integer.parseInt(name1);
                if (ID1 == 0) {
                    if (!isExistName(name1, table1, statement))
                        throw new MySQLIntegrityConstraintViolationException();
                } else if (!isExistID(ID1, table1, statement))
                    throw new MySQLIntegrityConstraintViolationException();

                System.out.print("Enter the name or the ID from the second table: ");
                name2 = reader.nextLine();
                if (isNumber(name2))
                    ID2 = Integer.parseInt(name2);
                if (ID2 == 0) {
                    if (!isExistName(name2, table2, statement))
                        throw new MySQLIntegrityConstraintViolationException();
                } else if (!isExistID(ID2, table2, statement))
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


                query = checkID(ID1, ID2, name1, name2, table1, table2,
                        resultTable, fields[0], fields[1], statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (NullPointerException e) {
                System.out.println("\nAdding is not success!");
                return;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Skill")) {
                    System.out.println("\nChosen developer hasn't a necessary skills for working with chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "Company")) {
                    System.out.println("\nChosen developer doesn't work in the company which works with this project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "Customer")) {
                    System.out.println("\nChosen company doesn't work with the customer which ordered the chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
            }
        }
    }


    /**
     * @param table
     * @param statement
     * @return
     * @throws SQLException This method lets to search records in any table
     */

    static String findRecords(String table, Statement statement)
            throws SQLException {
        ResultSet result;
        String fields, fieldsArr[], conditionsArr[];
        StringBuilder allConditions = new StringBuilder();
        boolean flag = false;

        while (!flag) {
            try {
                System.out.print("Enter the fields for searching " +
                        "(separated by a comma and a space).\nColumns should be written " +
                        "with lowercase latter.\nEnter: ");
                fields = reader.nextLine();

                fieldsArr = fields.split(", ");
                conditionsArr = new String[fieldsArr.length];

                // check the containing of the fields in the table
                int i = 0;
                while (i++ < fieldsArr.length) {
                    int count = 0;
                    query = "SELECT COUNT(column_name) FROM INFORMATION_SCHEMA.COLUMNS " +
                            "WHERE table_schema = 'task_sql' AND table_name = '" + table +
                            "' AND column_name = '" + fieldsArr[i - 1] + "'";
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                    if (count == 0)
                        throw new SQLException("Field");
                }

                System.out.println("Enter consistency the conditions for searching" +
                        "(separated by a line separator).\nConditions should be written in" +
                        " the following format: field + =/LIKE/>/</>=/<=/<> + some value.\n" +
                        "If you want to use subquery, you need to remember the SQL-syntax." +
                        "\nIf you compare string field, value for comparing must be enclosed " +
                        "in quotation marks.\nEnter:");
                i = 0;

                while (i++ < fieldsArr.length) {
                    conditionsArr[i - 1] = reader.nextLine();
                }

                for (i = 0; i < conditionsArr.length; i++) {
                    if ((i + 1) < conditionsArr.length) {
                        allConditions.append(conditionsArr[i]).append(" AND ");
                    } else allConditions.append(conditionsArr[i]);
                }

                flag = true;
                return "SELECT * FROM " + table + " WHERE " + allConditions.toString();
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Field")) {
                    System.out.println("\nYou wrote nonexistent field");
                } else System.out.println("\nSyntax error was made. Maybe, " +
                        "you wrote some fields or conditions wrong.");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return "";
            }
        }

        return "";
    }
}



/*static void addingDeveloper(Statement statement) throws SQLException {
        System.out.print("Enter the name of the developer: ");
        name_dev = reader.nextLine();

        if (!isExistName(name_dev, "developers", statement)) {

            System.out.println("Do you want to add salary? (Y/N)");
            choise = reader.next();

            if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {

                System.out.print("Enter a salary: ");
                int salary = reader.nextInt();
                reader.nextLine();
                query = "INSERT INTO developers VALUES (NULL, '" + name_dev + "', " + salary + ")";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
            } else {

                query = "INSERT INTO developers (id, name) VALUES (NULL, '" + name_dev + "')";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
            }
        } else System.out.println("This developer is already contained in the table.");
    }


    static void addingSkill(Statement statement) throws SQLException {
        System.out.print("Enter the name of the specialty: ");
        specialty = reader.nextLine();

        if (!isExistName(specialty, "skills", statement)) {
            query = "INSERT INTO skills VALUES (NULL, '" + specialty + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed");
        } else System.out.println("This skill is already contained in the table.");
    }


    static void addingProject(Statement statement) throws SQLException {
        System.out.print("Enter the name of the project: ");
        name_project = reader.nextLine();

        if (!isExistName(name_project, "projects", statement)) {

            // entering description (optional)
            System.out.println("Do you want to add a description about the project? (Y/N)");
            choise = reader.nextLine();

            String description = "";
            if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {
                System.out.println("Write the description below:");
                description = reader.nextLine();
            }


            // entering dates
            System.out.println("You should enter the start and the deadline of the project. " +
                    "Please, write it in format 'YYYY-MM-DD'.\nIf you don't know accurately day" +
                    " of start or end of the projects, you should enter the first day of the month.");

            System.out.print("Enter the date of start of the project: ");
            String start = reader.nextLine();
            start = checkDate(start);


            System.out.print("Enter the date of end of the project: ");
            String end = reader.nextLine();
            end = checkDate(end);

            // entering cost of project
            System.out.print("Enter the cost of the project: ");
            int cost = reader.nextInt();


            if (!Objects.equals(description, "")) {
                query = "INSERT INTO projects VALUE (NULL, '" + name_project + "', '" + description +
                        "', '" + start + "', '" + end + "', " + cost + ")";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
            } else {
                query = "INSERT INTO projects VALUE (NULL, '" + name_project + "', NULL, '" + start +
                        "', '" + end + "', " + cost + ")";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
            }
        } else System.out.println("This project is already contained in the table.");
    }


    static void addingCompany(Statement statement) throws SQLException {
        System.out.print("Enter the name of the company: ");
        name_company = reader.nextLine();

        if (!isExistName(name_company, "companies", statement)) {

            // entering address
            System.out.print("Enter an address of the company: ");
            String address = reader.nextLine();

            query = "INSERT INTO companies VALUES (NULL, '" + name_company + "', '" + address + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed");
        } else System.out.println("This company is already contained in the table.");
    }


    static void addingCustomer(Statement statement) throws SQLException {
        System.out.print("Enter the name of the customer: ");
        name_customer = reader.nextLine();

        if (!isExistName(name_customer, "customers", statement)) {

            query = "INSERT INTO customers VALUES (NULL, '" + name_customer + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed");
        } else System.out.println("This customer is already contained in the table.");
    }*/

/*

    static void addLinkDevSkill(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                int ID = 0, ID_skill = 0;
                System.out.print("Enter name or ID of the developer: ");
                name_dev = reader.nextLine();
                if (isNumber(name_dev))
                    ID = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the specialty: ");
                specialty = reader.nextLine();
                if (isNumber(specialty))
                    ID_skill = Integer.parseInt(specialty);

                query = checkID(ID, ID_skill, name_dev, specialty, "developers",
                        "skills", "developers_skills", "id_developer",
                        "id_skill", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (NullPointerException e) {
                System.out.println("\nAdding is not success!");
                return;
            }
        }
    }


    static void addLinkDevProject(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;
        ResultSet result;

        while (!flag) {
            try {
                int ID_dev = 0, ID_project = 0;
                System.out.print("Enter name or ID of developer: ");
                name_dev = reader.nextLine();
                if (isNumber(name_dev))
                    ID_dev = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the project: ");
                name_project = reader.nextLine();

                if (isNumber(name_project))
                    ID_project = Integer.parseInt(name_project);

                query = "SELECT id_developer FROM developers_skills WHERE id_developer = " + ID_dev + " AND " +
                        "id_skill IN (SELECT id_skill FROM projects_skills WHERE id_project = " + ID_project + ")";
                result = statement.executeQuery(query);
                if (!result.next())
                    throw new SQLException("Has not skill.");

                query = "SELECT id_developer FROM developers_companies WHERE id_developer = " + ID_dev + " AND " +
                        "id_company IN (SELECT id_company FROM companies_projects WHERE id_project = " + ID_project + ")";
                result = statement.executeQuery(query);
                if (!result.next())
                    throw new SQLException("There is not that company.");

                query = checkID(ID_dev, ID_project, name_dev, name_project, "developers",
                        "projects", "developers_projects", "id_developer",
                        "id_project", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Has not skill.")) {
                    System.out.println("\nChosen developer hasn't a necessary skills for working with chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "There is not that company.")) {
                    System.out.println("\nChosen developer doesn't work in the company which works with this project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
            }
        }
    }


    static void addLinkDevCompany(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                int ID_dev = 0, ID_company = 0;
                System.out.print("Enter name or ID of developer: ");
                name_dev = reader.nextLine();
                if (isNumber(name_dev))
                    ID_dev = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the company: ");
                name_company = reader.nextLine();
                if (isNumber(name_company))
                    ID_company = Integer.parseInt(name_company);

                query = checkID(ID_dev, ID_company, name_dev, name_company, "developers",
                        "companies", "developers_companies", "id_developer",
                        "id_companies", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }


    static void addLinkCompanyProject(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;
        ResultSet result;

        while (!flag) {
            try {
                int ID_company = 0, ID_project = 0;
                System.out.print("Enter name or ID of the company: ");
                name_company = reader.nextLine();
                if (isNumber(name_company))
                    ID_company = Integer.parseInt(name_company);

                System.out.print("Enter name or ID of the project: ");
                name_project = reader.nextLine();
                if (isNumber(name_project))
                    ID_project = Integer.parseInt(name_project);

                query = "SELECT id_company FROM companies_customers WHERE id_company = " + ID_company + " AND " +
                        "id_customer IN (SELECT id_customer FROM customers_projects WHERE id_project = " + ID_project + ")";
                result = statement.executeQuery(query);
                if (!result.next())
                    throw new SQLException("Has not customer.");

                query = checkID(ID_company, ID_project, name_company, name_project, "companies",
                        "projects", "companies_projects", "id_company",
                        "id_projects", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Has not customer.")) {
                    System.out.println("\nChosen company doesn't work with the customer which ordered the chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
            }
        }
    }

    static void addLinkProjectSkill(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                int ID_project = 0, ID_skill = 0;
                System.out.print("Enter name or ID of the project: ");
                name_project = reader.nextLine();
                if (isNumber(name_project))
                    ID_project = Integer.parseInt(name_project);

                System.out.print("Enter name or ID of the specialty: ");
                specialty = reader.nextLine();
                if (isNumber(specialty))
                    ID_skill = Integer.parseInt(specialty);

                query = checkID(ID_project, ID_skill, name_project, specialty,
                        "projects", "skills", "projects_skills",
                        "id_project", "id_skill", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }

    static void addLinkCustomerProject(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                int ID_customer = 0, ID_project = 0;
                System.out.print("Enter name or ID of the customer: ");
                name_customer = reader.nextLine();
                if (isNumber(name_customer))
                    ID_customer = Integer.parseInt(name_customer);

                System.out.print("Enter name or ID of the project: ");
                name_project = reader.nextLine();
                if (isNumber(name_project))
                    ID_project = Integer.parseInt(name_project);

                query = checkID(ID_customer, ID_project, name_customer, name_project,
                        "customers", "projects", "customers_projects",
                        "id_customer", "id_project", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }

    static void addLinkCompanyCustomer(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                int ID_company = 0, ID_customer = 0;
                System.out.print("Enter name or ID of the company: ");
                name_company = reader.nextLine();
                if (isNumber(name_company))
                    ID_company = Integer.parseInt(name_company);

                System.out.print("Enter name or ID of the customer: ");
                name_customer = reader.nextLine();
                if (isNumber(name_customer))
                    ID_customer = Integer.parseInt(name_customer);

                query = checkID(ID_company, ID_customer, name_company, name_customer,
                        "companies", "customers", "companies_customers",
                        "id_company", "id_customer", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                if (!Objects.equals(e.getMessage(), "alreadyExist"))
                    System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }*/
