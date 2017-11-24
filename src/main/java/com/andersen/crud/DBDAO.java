package com.andersen.crud;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class DBDAO {
    private static String query, name_dev, name_project, name_company, name_customer,
            choice, specialty;
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
                                  String DBforChange, Statement statement) throws SQLException {
        ResultSet result;

        if (ID1 != 0) {
            if (ID2 != 0) return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
            else {
                if (!Objects.equals(DB2, "skills"))
                    query = "SELECT id FROM " + DB2 + " WHERE name LIKE '" + name2 + "'";
                else query = "SELECT id FROM " + DB2 + " WHERE specialty LIKE '" + name2 + "'";
                result = statement.executeQuery(query);
                while (result.next()) ID2 = result.getInt(1);
                return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
            }
        } else {
            if (ID2 != 0) {
                if (!Objects.equals(DB1, "skills"))
                    query = "SELECT id FROM " + DB1 + " WHERE name LIKE '" + name1 + "'";
                else query = "SELECT id FROM " + DB1 + " WHERE specialty LIKE '" + name1 + "'";
                result = statement.executeQuery(query);
                while (result.next()) ID1 = result.getInt(1);
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

                return "INSERT INTO " + DBforChange + " VALUES (" + ID1 + ", " + ID2 + ")";
            }
        }
    }

    private static int linkExcep() {
        System.out.println("Do you want to try again? (Y/N)");

        while (true) {
            choice = reader.nextLine();
            switch (choice) {
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



    /**
     * @param statement
     * @throws SQLException The methods for adding in main tables are written below
     */
    static void addingDeveloper(Statement statement) throws SQLException {
        System.out.print("Enter the name of the developer: ");
        name_dev = reader.nextLine();

        System.out.println("Do you want to add salary? (Y/N)");
        choice = reader.next();

        if (Objects.equals(choice, "Y") || Objects.equals(choice, "y")) {

            System.out.print("Enter a salary: ");
            int salary = reader.nextInt();
            reader.nextLine();
            query = "INSERT INTO developers VALUES (NULL, '" + name_dev + "', " + salary + ")";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed");
        } else {

            query = "INSERT INTO developers (id, name) VALUES (NULL, '" + name_dev + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed");
        }
    }


    static void addingSkill(Statement statement) throws SQLException {
        System.out.print("Enter the name of the specialty: ");
        specialty = reader.nextLine();

        query = "INSERT INTO skills VALUES (NULL, '" + specialty + "')";
        System.out.println(statement.executeUpdate(query) + " row(-s) changed");
    }


    static void addingProject(Statement statement) throws SQLException {
        System.out.print("Enter the name of the project: ");
        name_project = reader.nextLine();


        // entering description (optional)
        System.out.println("Do you want to add a description about the project? (Y/N)");
        choice = reader.nextLine();

        String description = "";
        if (Objects.equals(choice, "Y") || Objects.equals(choice, "y")) {
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
    }


    static void addingCompany(Statement statement) throws SQLException {
        System.out.print("Enter the name of the company: ");
        name_company = reader.nextLine();

        // entering address
        System.out.print("Enter an address of the company: ");
        String address = reader.nextLine();

        query = "INSERT INTO companies VALUES (NULL, '" + name_company + "', '" + address + "')";
        System.out.println(statement.executeUpdate(query) + " row(-s) changed");
    }


    static void addingCustomer(Statement statement) throws SQLException {
        System.out.print("Enter the name of the customer: ");
        name_customer = reader.nextLine();

        query = "INSERT INTO customers VALUES (NULL, '" + name_customer + "')";
        System.out.println(statement.executeUpdate(query) + " row(-s) changed");
    }


    /**
     * @param statement
     * @throws SQLException The methods for adding links are written below
     */
    static void addLinkDevSkill(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;

        while (!flag) {
            try {
                System.out.print("Enter name or ID of the developer: ");
                name_dev = reader.nextLine();
                int ID = 0, ID_skill = 0;
                if (isNumber(name_dev))
                    ID = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the specialty which will be added to developer: ");
                specialty = reader.nextLine();
                if (isNumber(specialty))
                    ID_skill = Integer.parseInt(specialty);

                query = checkID(ID, ID_skill, name_dev, specialty, "developers",
                        "skills", "developers_skills", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            }
        }
    }


    static void addLinkDevProject(Statement statement)
            throws SQLException, NumberFormatException {
        boolean flag = false;
        ResultSet result;

        while (!flag) {
            try {
                System.out.print("Enter name or ID of developer: ");
                name_dev = reader.nextLine();
                int ID_dev = 0, ID_project = 0;
                if (isNumber(name_dev))
                    ID_dev = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the project where the developer is working: ");
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
                        "projects", "developers_projects", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Has not skill.")) {
                    System.out.println("Chosen developer hasn't a necessary skills for working with chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
                if (Objects.equals(e.getMessage(), "There is not that company.")) {
                    System.out.println("Chosen developer doesn't work in the company which works with this project.");
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
                System.out.print("Enter name or ID of developer: ");
                name_dev = reader.nextLine();
                int ID_dev = 0, ID_company = 0;
                if (isNumber(name_dev))
                    ID_dev = Integer.parseInt(name_dev);

                System.out.print("Enter name or ID of the company in which the developer is working: ");
                name_company = reader.nextLine();
                if (isNumber(name_company))
                    ID_company = Integer.parseInt(name_company);

                query = checkID(ID_dev, ID_company, name_dev, name_company, "developers",
                        "companies", "developers_companies", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
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
                System.out.print("Enter name or ID of the company: ");
                name_company = reader.nextLine();
                int ID_company = 0, ID_project = 0;
                if (isNumber(name_company))
                    ID_company = Integer.parseInt(name_company);

                System.out.print("Enter name or ID of the project with which the company will work: ");
                name_project = reader.nextLine();
                if (isNumber(name_project))
                    ID_project = Integer.parseInt(name_project);

                query = "SELECT id_company FROM companies_customers WHERE id_company = " + ID_company + " AND " +
                        "id_customer IN (SELECT id_customer FROM customers_projects WHERE id_project = " + ID_project + ")";
                result = statement.executeQuery(query);
                if (!result.next())
                    throw new SQLException("Has not customer.");

                query = checkID(ID_company, ID_project, name_company, name_project, "companies",
                        "projects", "companies_projects", statement);
                System.out.println(statement.executeUpdate(query) + " row(-s) changed");
                flag = true;
            } catch (MySQLIntegrityConstraintViolationException e) {
                System.out.println("\nYou wrote nonexistent ID(-s) or name(-s).");
                System.out.println("Do you want to try again? (Y/N)");
                returnFromLinkExcep = linkExcep();
                if (returnFromLinkExcep == 2) return;
            } catch (SQLException e) {
                if (Objects.equals(e.getMessage(), "Has not customer.")) {
                    System.out.println("Chosen company doesn't work with the customer which ordered the chosen project.");
                    returnFromLinkExcep = linkExcep();
                    if (returnFromLinkExcep == 2) return;
                }
            }
        }
    }
}
