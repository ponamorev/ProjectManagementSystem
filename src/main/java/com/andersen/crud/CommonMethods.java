package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

class CommonMethods {
    private static Scanner reader = new Scanner(System.in);


    static boolean isNumber(String usingString) {
        if (usingString == null || usingString.isEmpty()) return false;
        for (int i = 0; i < usingString.length(); i++)
            if (!Character.isDigit(usingString.charAt(i))) return false;
        return true;
    }

    static void printTableColumns(String table, Connection connection)
            throws SQLException {
        int max_wide, row;
        String columns[], addString;

        max_wide = table.length();

        String query = "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE table_schema = 'task_sql' AND table_name = '" + table + "'";
        ResultSet result = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY).executeQuery(query);

        result.last();
        columns = new String[result.getRow()];

        result.beforeFirst();
        for (int a = 0; a < 2; a++) {
            result.beforeFirst();
            row = 0;
            while (result.next()) {
                columns[row] = result.getString(1);

                if (max_wide < columns[row].length())
                    max_wide = columns[row].length();
                else {
                    addString = "";
                    for (int i = 0; i < max_wide - columns[row].length(); i++)
                        addString += " ";
                    columns[row] = columns[row].concat(addString);
                }

                row++;
            }
        }

        if (table.length() < max_wide) {
            addString = "";
            for (int i = table.length(); i < max_wide; i++)
                addString += " ";
            table = table.concat(addString);
        }

        System.out.println("| " + table + " |");
        addString = "";
        for (int i = 0; i < max_wide + 4; i++)
            addString += "-";
        System.out.println(addString);

        for (row = 0; row < columns.length; row++)
            System.out.println("| " + columns[row] + " |");
        System.out.println(addString);
    }

    static String checkDate(String date) throws NumberFormatException {
        boolean flag = false, check, rightDay = false;
        int year, month, day;

        do {
            try {
                check = date.length() == 10 && isNumber(date.substring(0, 4)) &&
                            date.charAt(4) == '-' && isNumber(date.substring(5, 7)) &&
                            date.charAt(7) == '-' && isNumber(date.substring(8));

                if (check) {
                    year = Integer.parseInt(date.substring(0, 4));
                    month = Integer.parseInt(date.substring(5, 7));
                    day = Integer.parseInt(date.substring(8));
                    rightDay = ((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 ||
                            month == 10 || month == 12) & (day >= 1 & day <= 31)) ||
                            ((year % 4 == 0) & (month == 2) & (day >= 1 & day <= 29)) ||
                            ((year % 4 != 0) & (month == 2) & (day >= 1 & day <= 28)) ||
                            ((month == 4 || month == 6 || month == 9 || month == 11) & (day >= 1 & day <= 30));
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


    static void checkQueryAndOut(String testQuery, String[] columns, Connection connection)
            throws SQLException {
        ResultSet result;
        String fieldResult[][], addString;

        if (!Objects.equals(testQuery, "")) {
            int count, max_lenght[], row;

            //
            // Search number of the columns in the table
            //
            max_lenght = new int[columns.length];

            //
            // Record of the name of the columns from the table
            //
            for (int i = 0; i < columns.length; i++) {
                max_lenght[i] = columns[i].length() + 1;
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
                if (columns[count].length() < max_lenght[count]) {
                    addString = "";
                    for (int i = 0; i < (max_lenght[count] - columns[count].length()); i++)
                        addString += " ";
                    columns[count] = columns[count++].concat(addString);
                }

            //
            // Output to the console results is got
            //
            count = 0;
            System.out.print("| ");
            while (count < columns.length) {
                System.out.print(columns[count]);
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
}
