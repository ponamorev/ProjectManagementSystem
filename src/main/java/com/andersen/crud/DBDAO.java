package com.andersen.crud;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class DBDAO {
    private static String query, choise;
    static int returnFromLinkExcep;
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

    static int linkExcep() {
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

    static void checkQueryAndOut(String testQuery, String[] columns, Connection connection)
            throws SQLException {
        ResultSet result;
        String fieldName[], fieldResult[][], addString;

        if (!Objects.equals(testQuery, "")) {
            int count, max_lenght[], row;

            //
            // Search number of the columns in the table
            //
            fieldName = new String[columns.length];
            max_lenght = new int[columns.length];

            //
            // Record of the name of the columns from the table
            //
            for (int i = 0; i < columns.length; i++) {
                fieldName[i] = columns[i];
                max_lenght[i] = fieldName[i].length() + 1;
            }
            System.out.println();


            //
            // Get number of rows from the table
            //
            result = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery(testQuery);
            result.last();
            row = result.getRow();
            fieldResult = new String[row][columns.length];

            //
            // Find max wide for each column
            // And get values from the rows
            //
            for (int a = 0; a < 2; a++) {
                result.beforeFirst();
                row = 0;
                while (result.next() && row <= fieldResult.length) {
                    count = 0;
                    while (count++ < columns.length) {
                        fieldResult[row][count - 1] = result.getString(count);
                        if (!Objects.equals(fieldResult[row][count - 1], null))
                            if (max_lenght[count - 1] <= fieldResult[row][count - 1].length()) {
                                max_lenght[count - 1] = fieldResult[row][count - 1].length();
                            } else {
                                addString = "";
                                for (int i = 0; i < (max_lenght[count - 1] - fieldResult[row][count - 1].length()); i++)
                                    addString += " ";
                                fieldResult[row][count - 1] = fieldResult[row][count - 1].concat(addString);
                            }
                        else {
                            char[] nullChar = new char[max_lenght[count - 1]];
                            int i = 0;
                            while (i < nullChar.length)
                                nullChar[i++] = ' ';
                            fieldResult[row][count - 1] = new String(nullChar);
                        }
                    }
                    row++;
                }
            }


            count = 0;
            while (count < columns.length)
                if (fieldName[count].length() < max_lenght[count]) {
                    addString = "";
                    for (int i = 0; i < (max_lenght[count] - fieldName[count].length()); i++)
                        addString += " ";
                    fieldName[count] = fieldName[count++].concat(addString);
                }

            //
            // Output to the console results is got
            //
            count = 0;
            System.out.print("| ");
            while (count < columns.length) {
                System.out.print(fieldName[count]);
                if (count + 1 <= columns.length)
                    System.out.print(" | ");
                count++;
            }
            count = 0;
            int len = 2;
            while (count < columns.length) {
                len += max_lenght[count] + 2;
                if (count++ + 1 < columns.length)
                    len++;
            }
            count = 0;
            addString = "";
            while (count++ < len)
                addString += "-";
            System.out.println("\n" + addString);

            for (row = 0; row < fieldResult.length; row++) {
                System.out.print("| ");
                for (count = 0; count < fieldResult[row].length; count++) {
                    System.out.print(fieldResult[row][count]);
                    if (count + 1 <= fieldResult[row].length)
                        System.out.print(" | ");
                }
                System.out.println();
            }

            System.out.println(addString);

        } else System.out.println("The query is empty!");
    }


    /**
     * @param table
     * @param statement
     * @throws SQLException This method adds a new records in a one of the main tables
     *                      Main tables: developers, skills, projects, companies, customers
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
            if (!isExistName(name, table, statement)) {

                switch (table) {
                    // Add in the table "developers"!
                    case "developers":
                        boolean more = false;
                        String createDev[] = new String[2];
                        System.out.println("Do you want to add a salary? (Y/N)");
                        choise = reader.nextLine();

                        if (Objects.equals(choise, "Y") || Objects.equals(choise, "y")) {

                            System.out.print("Enter a salary: ");
                            int salary = reader.nextInt();
                            reader.nextLine();

                            builder.append(name).append("', ").append(salary).append(")");
                        } else
                            builder.append(name).append("')");
                        // Adding in the table "developers"
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more skills for the developer.\n" +
                                "List of skills is shown below.");
                        query = "SELECT * FROM skills";
                        String columnsSkill[] = { "id", "specialty" };
                        checkQueryAndOut(query, columnsSkill, connection);
                        builder.append("INSERT INTO developers_skills VALUES (");
                        query = "SELECT id FROM developers WHERE name = '" + name + "'";
                        result = statement.executeQuery(query);
                        int ID_dev = 0;
                        while (result.next())
                            ID_dev = result.getInt(1);
                        while (!more) {
                            System.out.print("\nID or name of the skill: ");
                            String skill = reader.nextLine();
                            if (!isNumber(skill)) {
                                query = "SELECT id FROM skills WHERE specialty = '" + skill + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    skill = result.getString(1);
                            }
                            builder.append(ID_dev).append(", ").append(skill).append(")");
                            System.out.print("Do you want to add another skill? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Adding in the table "developers_skills"
                        createDev[0] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more companies where the developer is working.\n" +
                                "List of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        String columnsCompany[] = { "id", "name", "address" };
                        checkQueryAndOut(query, columnsCompany, connection);
                        builder.append("INSERT INTO developers_companies VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the company: ");
                            String company = reader.nextLine();
                            if (!isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_dev).append(", ").append(company).append(")");
                            System.out.print("Do you want to add another company? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Addind in the table "developers_companies"
                        createDev[1] = builder.toString();
                        builder.delete(0, builder.length());

                        System.out.println(statement.executeUpdate(createDev[0]) +
                                statement.executeUpdate(createDev[1]) + " row(-s) changed.");
                        break;

                    // Add in the table "projects"!
                    case "projects":
                        String description = "NULL", createProject[] = new String[3];
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
                        // Adding in the table "projects"
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more skills which are necessary" +
                                " for working with the project.\nList of skills is shown below.");
                        query = "SELECT * FROM skills";
                        columnsSkill = new String[] { "id", "specialty" };
                        checkQueryAndOut(query, columnsSkill, connection);
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
                            if (!isNumber(skill)) {
                                query = "SELECT id FROM skills WHERE specialty = '" + skill + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    skill = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(skill).append(")");
                            System.out.print("Do you want to add another skill? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Adding in the table "developers_skills"
                        createProject[0] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more companies which work with the project.\n" +
                                "List of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        columnsCompany = new String[] { "id", "name", "address" };

                        checkQueryAndOut(query, columnsCompany, connection);
                        builder.append("INSERT INTO companies_projects VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the company: ");
                            String company = reader.nextLine();
                            if (!isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(company).append(")");
                            System.out.print("Do you want to add another company? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Addind in the table "developers_companies"
                        createProject[1] = builder.toString();
                        builder.delete(0, builder.length());


                        System.out.println("Please, add one or more customers which ordered this project.\n" +
                                "List of the customer is shown below.");
                        query = "SELECT * FROM customers";
                        String columnsCustomer[] = { "id", "name" };
                        checkQueryAndOut(query, columnsCustomer, connection);
                        builder.append("INSERT INTO customers_projects VALUES (");
                        more = false;
                        while (!more) {
                            System.out.print("ID or name of the customer: ");
                            String customer = reader.nextLine();
                            if (!isNumber(customer)) {
                                query = "SELECT id FROM customers WHERE name = '" + customer + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    customer = result.getString(1);
                            }
                            builder.append(ID_project).append(", ").append(customer).append(")");
                            System.out.print("Do you want to add another customer? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Addind in the table "developers_companies"
                        createProject[2] = builder.toString();
                        builder.delete(0, builder.length());

                        System.out.println(statement.executeUpdate(createProject[0]) +
                                statement.executeUpdate(createProject[1]) + statement.executeUpdate(createProject[2]) +
                                " row(-s) changed.");
                        break;


                    // Add in the table "customers"
                    case "customers":
                        String createCustomer[] = new String[1];
                        builder.append(name).append(")");
                        System.out.println(statement.executeUpdate(builder.toString()) +
                                " row(-s) changed.");


                        System.out.println("Please, add one or more companies which are working" +
                                "with this customer.\nList of the companies is shown below.");
                        query = "SELECT * FROM companies";
                        columnsCompany = new String[]{"id", "name", "address"};
                        checkQueryAndOut(query, columnsCompany, connection);
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
                            if (!isNumber(company)) {
                                query = "SELECT id FROM companies WHERE name = '" + company + "'";
                                result = statement.executeQuery(query);
                                while (result.next())
                                    company = result.getString(1);
                            }
                            builder.append(ID_customer).append(", ").append(company).append(")");
                            System.out.print("Do you want to add another company? (Y/N)\t");
                            choise = reader.nextLine();
                            more = Objects.equals(choise, "y") || Objects.equals(choise, "Y");
                            if (more) builder.append(", (");
                        }
                        // Addind in the table "companies_customers"
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
     * @param connection
     * @param statement
     * @return
     * @throws SQLException This method lets to search records in any table
     */

    static void readTables(Connection connection, Statement statement)
            throws SQLException {
        ResultSet result;
        String table, allTables[], chosenTablesArr[], chosenTables, column, allColumns[], chosenColumnsArr[],
                chosenColumns, condition, allConditionsArr[], chosenConditions;
        boolean flag = false;
        int count = 0;

        while (!flag) {
            try {
                StringBuilder builder = new StringBuilder();
                // Select tables

                // Output all tables
                System.out.println("Select table(-s) to search.\nList of tables is shown below.");
                query = "SELECT DISTINCT table_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = 'task_sql'";
                result = statement.executeQuery(query);
                while (result.next()) {
                    table = result.getString(1);
                    builder.append(table).append("\n");
                    System.out.println(table);
                }
                allTables = builder.toString().split("\n");
                builder.delete(0, builder.length());

                // Enter needed tables
                System.out.println("Enter the table(-s). For stop enter 'q'.");
                table = reader.nextLine();
                while (!Objects.equals(table, "q")) {
                    count = 0;
                    for (String tab : allTables)
                         if (Objects.equals(table, tab))
                             count++;
                    if (count > 0)
                        builder.append(table).append("\n");
                    else
                        throw new SQLException("Table");
                    table = reader.nextLine();
                }

                chosenTablesArr = builder.toString().split("\n");
                builder.delete(0, builder.length());
                chosenTables = "";
                for (int i = 0; i < chosenTablesArr.length; i++) {
                    chosenTables += chosenTablesArr[i];
                    if (i + 1 < chosenTablesArr.length)
                        chosenTables += ", ";
                }



                // Output all columns from chosen tables
                System.out.println("Select columns to output.\nList of columns is shown below.");
                query = "SELECT DISTINCT table_name, column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE table_schema = 'task_sql'";
                query = query.concat(" AND ");
                for (int i = 0; i < chosenTablesArr.length; i++) {
                    query = query.concat("table_name = '" + chosenTablesArr[i] + "'");
                    if (i + 1 < chosenTablesArr.length)
                        query = query.concat(" OR ");
                }
                result = statement.executeQuery(query);
                while (result.next()) {
                    column = result.getString(1) + "." + result.getString(2);
                    builder.append(column).append("\n");
                    System.out.println(column);
                }
                allColumns = builder.toString().split("\n");
                builder.delete(0, builder.length());

                // Enter needed columns
                System.out.println("\nEnter columns. For stop enter 'q'.");
                column = reader.nextLine();

                while (!Objects.equals(column, "q")) {
                    count = 0;
                    for (String col : allColumns)
                        if (Objects.equals(column, col))
                            count++;
                    if (count > 0)
                        builder.append(column).append("\n");
                    else throw new SQLException("Column");
                    column = reader.nextLine();
                }

                chosenColumnsArr = builder.toString().split("\n");
                builder.delete(0, builder.length());
                chosenColumns = "";
                for (int i = 0; i < chosenColumnsArr.length; i++) {
                    chosenColumns += chosenColumnsArr[i];
                    if (i + 1 < chosenColumnsArr.length)
                        chosenColumns += ", ";
                }



                // Select to conditions to search
                System.out.println("Enter consistently the conditions to search" +
                        "(separated by a line separator).\nThe conditions must be written in" +
                        " the following format: 'field =/LIKE/>/</>=/<=/<>/IN some_value' " +
                        "\nor 'field BETWEEN min_value AND max_value')." +
                        "\nFor combine conditions must use 'AND' or 'OR'." +
                        "\nIf you want to use subquery, you need to remember the SQL-syntax." +
                        "\nIf you compare string field, value for comparing must be enclosed " +
                        "in quotation marks.\nFor stop enter 'q'.");

                condition = reader.nextLine();
                while (!Objects.equals(condition, "q")) {
                    builder.append(condition).append("\n");
                    condition = reader.nextLine();
                }
                allConditionsArr = builder.toString().split("\n");
                builder.delete(0, builder.length());
                chosenConditions = "";
                for (int i = 0; i < allConditionsArr.length; i++) {
                    chosenConditions += allConditionsArr[i];
                    if (i + 1 < allConditionsArr.length)
                        if (!Objects.equals(allConditionsArr[i].charAt(allConditionsArr[i].length() - 1), ' ') &
                                !Objects.equals(allConditionsArr[i + 1].charAt(0), ' '))
                            chosenConditions += " ";
                }



                // Query!
                query = "SELECT " + chosenColumns + " FROM " + chosenTables + " WHERE " + chosenConditions;
                checkQueryAndOut(query, chosenColumnsArr, connection);
                flag = true;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Column")) {
                    System.out.println("\nYou wrote nonexistent column.");
                } else if (Objects.equals(e.getMessage(), "Table")) {
                    System.out.println("\nYou wrote nonexistent table.");
                } else System.out.println("\nSyntax error was made. Maybe, " +
                        "you wrote some column or condition wrong.");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }
}
