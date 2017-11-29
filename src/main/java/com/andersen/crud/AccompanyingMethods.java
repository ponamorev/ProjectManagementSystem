package com.andersen.crud;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class AccompanyingMethods {
    private static String query, choise;
    private static Scanner reader = new Scanner(System.in);


    static boolean isNumber(String usingString) {
        if (usingString == null || usingString.isEmpty()) return false;
        for (int i = 0; i < usingString.length(); i++)
            if (!Character.isDigit(usingString.charAt(i))) return false;
        return true;
    }

    static String checkDate(String date) throws NumberFormatException {
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

    static String checkID(int ID1, int ID2, String name1, String name2, String DB1, String DB2,
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

    static boolean isExistName(String name, String table, Statement statement)
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

    static boolean isExistID(int ID, String table, Statement statement)
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

    static String[] selectTables(Statement statement)
            throws SQLException, ClassNotFoundException {
        ResultSet result;
        StringBuilder builder = new StringBuilder();
        String table, allTables[], chosenTablesArr[], chosenTables;
        int count;
        // Select tables

        // Output all tables
        System.out.println("Select table(-s) to search.\nList of tables is shown below.");
        query = "SELECT DISTINCT table_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = 'task_sql'";
        checkQueryAndOut(query, new String[]{"table_name"}, GetConnection.getConnection());
        result = statement.executeQuery(query);
        while (result.next()) {
            table = result.getString(1);
            builder.append(table).append("\n");
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
        String cloneArr[] = new String[chosenTablesArr.length];
        System.arraycopy(chosenTablesArr, 0, cloneArr, 0, chosenTablesArr.length);
        chosenTablesArr = new String[chosenTablesArr.length + 1];
        chosenTablesArr[0] = chosenTables;
        System.arraycopy(cloneArr, 0, chosenTablesArr, 1, cloneArr.length);

        return chosenTablesArr;
    }

    static String[] selectColumns(String[] tables, Statement statement)
            throws SQLException, ClassNotFoundException {
        ResultSet result;
        StringBuilder builder = new StringBuilder();
        String column, allColumns[], chosenColumnsArr[], chosenColumns;
        int count;

        // Output all columns from chosen tables
        System.out.println("Select columns to output.\nList of columns is shown below.");
        query = "SELECT DISTINCT table_name, column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE table_schema = 'task_sql'";
        query = query.concat(" AND ");
        for (int i = 1; i < tables.length; i++) {
            query = query.concat("table_name = '" + tables[i] + "'");
            if (i + 1 < tables.length)
                query = query.concat(" OR ");
        }
        checkQueryAndOut(query, new String[]{"table_name", "column_name"}, GetConnection.getConnection());
        result = statement.executeQuery(query);
        while (result.next()) {
            column = result.getString(1) + "." + result.getString(2);
            builder.append(column).append("\n");
        }
        allColumns = builder.toString().split("\n");
        builder.delete(0, builder.length());

        // Enter needed columns
        System.out.println("\nEnter columns. For stop enter 'q'. For add all columns enter 'all'.");
        column = reader.nextLine();

        while (!Objects.equals(column, "q")) {
            if (!Objects.equals(column, "all")) {
                count = 0;
                for (String col : allColumns)
                    if (Objects.equals(column, col))
                        count++;
                if (count > 0)
                    builder.append(column).append("\n");
                else throw new SQLException("Column");
                column = reader.nextLine();
            } else {
                builder.delete(0, builder.length());
                for (String col : allColumns)
                    builder.append(col).append("\n");
                column = "q";
            }
        }

        chosenColumnsArr = builder.toString().split("\n");
        builder.delete(0, builder.length());
        chosenColumns = "";
        for (int i = 0; i < chosenColumnsArr.length; i++) {
            chosenColumns += chosenColumnsArr[i];
            if (i + 1 < chosenColumnsArr.length)
                chosenColumns += ", ";
        }

        String cloneArr[] = new String[chosenColumnsArr.length];
        System.arraycopy(chosenColumnsArr, 0, cloneArr, 0, chosenColumnsArr.length);
        chosenColumnsArr = new String[chosenColumnsArr.length + 1];
        chosenColumnsArr[0] = chosenColumns;
        System.arraycopy(cloneArr, 0, chosenColumnsArr, 1, cloneArr.length);

        return chosenColumnsArr;
    }

}
