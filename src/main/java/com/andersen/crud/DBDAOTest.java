package com.andersen.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class DBDAOTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = GetConnection.getConnection();
        Statement statement = connection.createStatement();

        String mainMenu = "Choose the action with database:\n\t1. Create data\n\t" +
                "2. Read data\n\t3. Update data\n\t4. Delete data\n\t5. Exit from the app\n\t";
        String createMenu = "Choose a table where you will create a record(s):\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. developers_skills\n\t7. developers_projects\n\t8. developers_companies\n\t" +
                "9. projects_skills\n\t10. companies_projects\n\t11. companies_customers\n\t" +
                "12. customers_projects\n\t13. Cancel, back to main menu";
        String res;

        boolean flag = true;

        Scanner consoleReader = new Scanner(System.in);

        System.out.println("Welcome to the app! You can work with database \"task_sql\" here.");

        while (flag) {
            try {
                System.out.println(mainMenu);
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
                        DBDAO.readTables(connection, statement);
                        break;

                    case 3:
                        DBDAO.updateRecords(connection, statement);
                        break;

                    case 4:
                        DBDAO.deleteRecords(connection, statement);
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
            }
        }
    }
}
