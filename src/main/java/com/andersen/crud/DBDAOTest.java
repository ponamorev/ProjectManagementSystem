package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

class DBDAOTest {
    private static Statement statement;
    private static ResultSet resultSet;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = GetConnection.getConnection();
        statement = connection.createStatement();

        DBDAO.addLinkCompanyProject(statement);

        /*String mainMenu = "Choose the action with database:\n\t1. Create data\n\t" +
                "2. Read data\n\t3. Update data\n\t4. Delete data\n\t5. Exit from the app\n\t" +
                "6. Cancel";
        String createMenu = "Choose a table where you will create a record(s):\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. Cancel";
        String readMenu = "Choose a table where the app will read records:\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. Cancel";
        String updateMenu = "Choose a table which will be updated:\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. Cancel";
        String deleteMenu = "Choose a table where you want to delete records:\n\t" +
                "1. developers\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\n\t" +
                "6. Cancel";
        String res;

        boolean temp = true;

        Scanner consoleReader = new Scanner(System.in);

        System.out.println("Welcome to the app! You can work with database \"task_sql\" here.");

        while (temp) {
            System.out.println(mainMenu);
            System.out.print("Enter an action number:");
            res = consoleReader.nextLine();

            switch (Integer.valueOf(res)) {

                case 1:
                    System.out.println(createMenu);
                    System.out.print("Enter table name or number from the list: ");
                    res = consoleReader.nextLine();

                    if (Integer.valueOf(res) == 1)
                        res = "developers";
                    else if (Integer.valueOf(res) == 2)
                        res = "skills";
                    else if (Integer.valueOf(res) == 3)
                        res = "projects";
                    else if (Integer.valueOf(res) == 4)
                        res = "companies";
                    else if (Integer.valueOf(res) == 5)
                        res = "customers";

                    switch (res) {
                        case "developers":

                            break;
                        case "skills":
                            break;
                        default:
                            System.out.println("Perhaps, it's mistake." +
                                    " Enter the word or number again");
                            break;
                    }
                    break;


                case 5:
                    temp = false;
                    break;
            }
        }*/
    }
}
