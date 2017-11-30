package com.andersen.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class DBDAOTest {
    private static final String textForConditions = "Enter consistently the conditions " +
            "(separated by a line separator).\nThe conditions must be written in" +
            " the following format: 'table.column =/LIKE/>/</>=/<=/<>/IN some_value' " +
            "\nor 'table.column BETWEEN min_value AND max_value')." +
            "\nFor combine conditions must use 'AND' or 'OR'." +
            "\nIf you want to use subquery, you need to remember the SQL-syntax." +
            "\nIf you compare string field, value for comparing must be enclosed " +
            "in quotation marks.\nFor stop enter 'q'.";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = GetConnection.getConnection();
        Statement statement = connection.createStatement();

        final String mainMenu = "Choose the action with database:\n\t1. Create data\n\t" +
                "2. Read data\n\t3. Update data\n\t4. Delete data\n\t5. Exit from the app\n\t";
        final String createMenu = "Choose a table where you will create a record(s):\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. developers_skills\n\t7. developers_projects\n\t8. developers_companies\n\t" +
                "9. projects_skills\n\t10. companies_projects\n\t11. companies_customers\n\t" +
                "12. customers_projects\n\t13. Cancel, back to main menu";
        String res, chosenTablesArr[], chosenColumnsArr[], query;

        boolean flag = true;

        Scanner consoleReader = new Scanner(System.in);

        System.out.println("Welcome to the app! You can work with database \"task_sql\" here.");

        while (flag) {
            try {
                System.out.println("\n" + mainMenu);
                System.out.print("Enter an action number: ");
                res = consoleReader.nextLine();

                switch (Integer.valueOf(res)) {

                    case 1:
                        System.out.println(createMenu);
                        System.out.print("Enter number from the list: ");
                        res = consoleReader.nextLine();

                        switch (Integer.valueOf(res)) {
                            case 1:
                                DBDAO.createRecords("developers", connection, statement);
                                break;
                            case 2:
                                DBDAO.createRecords("skills", connection, statement);
                                break;
                            case 3:
                                DBDAO.createRecords("projects", connection, statement);
                                break;
                            case 4:
                                DBDAO.createRecords("companies", connection, statement);
                                break;
                            case 5:
                                DBDAO.createRecords("customers", connection, statement);
                                break;
                            case 6:
                                DBDAO.createLinkRecords("developers", "skills", statement);
                                break;
                            case 7:
                                DBDAO.createLinkRecords("developers", "projects", statement);
                                break;
                            case 8:
                                DBDAO.createLinkRecords("developers", "companies", statement);
                                break;
                            case 9:
                                DBDAO.createLinkRecords("projects", "skills", statement);
                                break;
                            case 10:
                                DBDAO.createLinkRecords("companies", "projects", statement);
                                break;
                            case 11:
                                DBDAO.createLinkRecords("companies", "customers", statement);
                                break;
                            case 12:
                                DBDAO.createLinkRecords("customers", "projects", statement);
                                break;
                            case 13:
                                break;
                            default:
                                System.out.println("Perhaps, it's mistake." +
                                        " Enter the number again");
                                break;
                        }

                        break;

                    case 2:
                        // Select tables
                        System.out.println("This is the list of the tables. Choose tables to search.");
                        chosenTablesArr = AccompanyingMethods.selectTables(statement);

                        // Select columns
                        chosenColumnsArr = AccompanyingMethods.selectColumns(chosenTablesArr, statement);
                        String cloneArr[] = new String[chosenColumnsArr.length - 1];
                        System.arraycopy(chosenColumnsArr, 1, cloneArr, 0, cloneArr.length);

                        DBDAO.readTables(chosenTablesArr[0], chosenColumnsArr[0], cloneArr, connection);
                        break;

                    case 3:
                        // Select table
                        System.out.println("This is the list of tables. Choose tables to update.");
                        String[] tables = AccompanyingMethods.selectTables(statement);

                        if (tables.length > 2) {
                            query = "SELECT DISTINCT table_name, column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                                    "WHERE table_schema = 'task_sql' AND ";
                            for (int i = 1; i < tables.length; i++) {
                                query += "table_name = '" + tables[i] + "'";
                                if (i + 1 < tables.length)
                                    query += " OR ";
                            }
                            AccompanyingMethods.checkQueryAndOut(query, new String[]{"table_name", "column_name"}, connection);
                        } else if (tables.length == 2) {
                            query = "SELECT DISTINCT column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                                    "WHERE table_schema = 'task_sql' AND table_name = '" + tables[1] + "'";
                            AccompanyingMethods.checkQueryAndOut(query, new String[]{"column_name"}, connection);
                        } else throw new SQLException("Table");

                        DBDAO.updateRecords(tables[0], statement);
                        break;

                    case 4:
                        boolean check = false;

                        // Select table
                        System.out.println("This is the list of tables. Choose tables, where records will be deleted.");
                        tables = AccompanyingMethods.selectTables(statement);

                        // Check tables
                        if (tables.length > 2) {
                            query = "SELECT DISTINCT table_name, column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                                    "WHERE table_schema = 'task_sql' AND ";
                            for (int i = 1; i < tables.length; i++) {
                                query += "table_name = '" + tables[i] + "'";
                                if (i + 1 < tables.length)
                                    query += " OR ";
                            }
                            AccompanyingMethods.checkQueryAndOut(query, new String[]{"table_name", "column_name"}, connection);
                        } else if (tables.length == 2) {
                            query = "SELECT DISTINCT column_name FROM INFORMATION_SCHEMA.COLUMNS " +
                                    "WHERE table_schema = 'task_sql' AND table_name = '" + tables[1] + "'";
                            AccompanyingMethods.checkQueryAndOut(query, new String[]{"column_name"}, connection);
                        } else throw new SQLException("Table");


                        StringBuilder builder = new StringBuilder();

                        while (!check) {
                            // Enter the condition to search records to update
                            System.out.println(textForConditions);
                            String condition = consoleReader.nextLine();
                            while (!Objects.equals(condition, "q")) {
                                builder.append(condition).append("\n");
                                condition = consoleReader.nextLine();
                            }
                            String[] allConditions = builder.toString().split("\n");
                            condition = "";
                            for (int i = 0; i < allConditions.length; i++) {
                                condition += allConditions[i];
                                if (i + 1 < allConditions.length)
                                    if (!Objects.equals(allConditions[i].charAt(allConditions[i].length() - 1), ' ') &
                                            !Objects.equals(allConditions[i + 1].charAt(0), ' '))
                                        condition += " ";
                            }

                            if (DBDAO.deleteRecords(tables[0], condition, statement) == 0)
                                check = true;
                        }
                        break;

                    case 5:
                        flag = false;
                        break;

                    default:
                        System.out.println("You wrote wrong number. Please, try again.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Don't press 'Enter', if you don't write a number. :)");
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Table")) {
                    System.out.println("\nYou wrote nonexistent table.");
                    int returnFromLinkExcep = AccompanyingMethods.linkExcep();
                    if (returnFromLinkExcep == 2) return;
                } else System.out.println("Some SQL exception. Please, try again.");
            }
        }
    }
}
