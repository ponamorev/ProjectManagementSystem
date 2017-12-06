package com.andersen.crud;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

class DBTest {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/task_sql" +
            "?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "2130";
    private static String choice;

    private static Scanner reader = new Scanner(System.in);
    private static LinkedList listFromTable = new LinkedList();

    private static int choosingAction(String table) {
        System.out.println("What action do you want to do?");
        System.out.println("\t1. Create\n\t2. Read\n\t3. Update\n\t4. Delete");
        switch (table) {
            case "developers":
                System.out.println("\t5. Add skill to developer\n\t" +
                        "6. Add project to developer\n\t7. Add company to developer");
                break;
            case "skills":
                System.out.println("\t5. Add skill to developer\n\t" +
                        "6. Add skill to project");
                break;
            case "projects":
                System.out.println("\t5. Add developer to project\n\t" +
                        "6. Add skill to project\n\t7. Add project to company\n\t" +
                        "8. Add project from customer");
                break;
            case "companies":
                System.out.println("\t5. Add developer to company\n\t" +
                        "6. Add project to company\n\t7. Add customer to company");
                break;
            case "customers":
                System.out.println("\t5. Add project from customer\n\t" +
                        "6. Add customer to company");
                break;
            default:
                return 0;
        }

        while (true) {
            try {
                System.out.print("Enter: ");
                return reader.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("You enter not number. Try again.");
                reader.nextLine();
            }
        }
    }

    private static void action(String table, int numberOfAction,
                               Connection connection, Statement statement)
            throws SQLException {
        reader.nextLine();

        if (numberOfAction == 0) {
            System.out.println("You wrote nonexistent table.");
            return;
        }

        switch (table) {
            case "developers":
                Developers developer;
                DevelopersDAO devDAO = new DevelopersDAO(null);
                DevelopersView devView = new DevelopersView();
                switch (numberOfAction) {
                    case 1:
                        devDAO.createDeveloper(null, connection, statement);

                        break;
                    case 2:
                        listFromTable.clear();
                        listFromTable = devView.readDevelopers(connection, statement);

                        break;
                    case 3:
                        devDAO.updateDeveloper(connection, statement);

                        break;
                    case 4:
                        developer = devView.findDeveloper(connection, reader);

                        devDAO.deleteDeveloper(developer, connection, statement);

                        break;
                    case 5:
                        developer = devView.findDeveloper(connection, reader);

                        devDAO.addDeveloperLink(developer, "skills", connection, statement);

                        break;
                    case 6:
                        developer = devView.findDeveloper(connection, reader);

                        devDAO.addDeveloperLink(developer, "projects", connection, statement);

                        break;
                    case 7:
                        developer = devView.findDeveloper(connection, reader);

                        devDAO.addDeveloperLink(developer, "companies", connection, statement);

                        break;
                    default:
                        System.out.println("Anyone action hasn't this number. Do you want to try again? (Y/any key)");
                        choice = reader.nextLine();
                        if (choice.equals("y") || choice.equals("Y")) {
                            System.out.print("Enter the action number from list above: ");
                            action("developers", reader.nextInt(), connection, statement);
                        }
                        break;
                }
                break;
            case "skills":
                Skills skill;
                SkillsDAO skillDAO = new SkillsDAO(null);
                SkillsView skillView = new SkillsView();
                switch (numberOfAction) {
                    case 1:
                        skillDAO.createSkill(null, connection, statement);

                        break;
                    case 2:
                        listFromTable.clear();
                        listFromTable = skillView.readSkills(connection, statement);

                        break;
                    case 3:
                        skillDAO.updateSkill(connection, statement);

                        break;
                    case 4:
                        skill = skillView.findSkill(connection, reader);

                        skillDAO.deleteSkill(skill, connection, statement);

                        break;
                    case 5:
                        skill = skillView.findSkill(connection, reader);

                        skillDAO.addSkillLink(skill, "developers", connection, statement);

                        break;
                    case 6:
                        skill = skillView.findSkill(connection, reader);

                        skillDAO.addSkillLink(skill, "projects", connection, statement);

                        break;
                    default:
                        System.out.println("Anyone action hasn't this number. Do you want to try again? (Y/any key)");
                        choice = reader.nextLine();
                        if (choice.equals("y") || choice.equals("Y")) {
                            System.out.print("Enter the action number from list above: ");
                            action("skills", reader.nextInt(), connection, statement);
                        }
                        break;
                }
                break;
            case "projects":
                Projects project;
                ProjectsDAO prDAO = new ProjectsDAO(null);
                ProjectsView prView = new ProjectsView();
                switch (numberOfAction) {
                    case 1:
                        prDAO.createProject(null, connection, statement);

                        break;
                    case 2:
                        listFromTable.clear();
                        listFromTable = prView.readProjects(connection, statement);

                        break;
                    case 3:
                        prDAO.updateProject(connection, statement);

                        break;
                    case 4:
                        project = prView.findProject(connection, reader);

                        prDAO.deleteProject(project, connection, statement);

                        break;
                    case 5:
                        project = prView.findProject(connection, reader);

                        prDAO.addProjectLink(project, "developers", connection, statement);

                        break;
                    case 6:
                        project = prView.findProject(connection, reader);

                        prDAO.addProjectLink(project, "skills", connection, statement);

                        break;
                    case 7:
                        project = prView.findProject(connection, reader);

                        prDAO.addProjectLink(project, "companies", connection, statement);

                        break;
                    case 8:
                        project = prView.findProject(connection, reader);

                        prDAO.addProjectLink(project, "customers", connection, statement);

                        break;
                    default:
                        System.out.println("Anyone action hasn't this number. Do you want to try again? (Y/any key)");
                        choice = reader.nextLine();
                        if (choice.equals("y") || choice.equals("Y")) {
                            System.out.print("Enter the action number from list above: ");
                            action("projects", reader.nextInt(), connection, statement);
                        }
                        break;
                }
                break;
            case "companies":
                Companies company;
                CompaniesDAO compDAO = new CompaniesDAO(null);
                CompaniesView compView = new CompaniesView();
                switch (numberOfAction) {
                    case 1:
                        compDAO.createCompany(null, connection, statement);

                        break;
                    case 2:
                        listFromTable.clear();
                        listFromTable = compView.readCompanies(connection, statement);

                        break;
                    case 3:
                        compDAO.updateCompany(connection, statement);

                        break;
                    case 4:
                        company = compView.findCompany(connection, reader);

                        compDAO.deleteCompany(company, connection, statement);

                        break;
                    case 5:
                        company = compView.findCompany(connection, reader);

                        compDAO.addCompanyLink(company, "developers", connection, statement);

                        break;
                    case 6:
                        company = compView.findCompany(connection, reader);

                        compDAO.addCompanyLink(company, "projects", connection, statement);

                        break;
                    case 7:
                        company = compView.findCompany(connection, reader);

                        compDAO.addCompanyLink(company, "customers", connection, statement);

                        break;
                    default:
                        System.out.println("Anyone action hasn't this number. Do you want to try again? (Y/any key)");
                        choice = reader.nextLine();
                        if (choice.equals("y") || choice.equals("Y")) {
                            System.out.print("Enter the action number from list above: ");
                            action("companies", reader.nextInt(), connection, statement);
                        }
                        break;
                }
                break;
            case "customers":
                Customers customer;
                CustomersDAO custDAO = new CustomersDAO(null);
                CustomersView custView = new CustomersView();
                switch (numberOfAction) {
                    case 1:
                        custDAO.createCustomer(null, connection, statement);

                        break;
                    case 2:
                        listFromTable.clear();
                        listFromTable = custView.readCustomers(connection, statement);

                        break;
                    case 3:
                        custDAO.updateCustomer(connection, statement);

                        break;
                    case 4:
                        customer = custView.findCustomer(connection, reader);

                        custDAO.deleteCustomer(customer, connection, statement);

                        break;
                    case 5:
                        customer = custView.findCustomer(connection, reader);

                        custDAO.addCustomerLink(customer, "projects", connection, statement);

                        break;
                    case 6:
                        customer = custView.findCustomer(connection, reader);

                        custDAO.addCustomerLink(customer, "companies", connection, statement);

                        break;
                    default:
                        System.out.println("Anyone action hasn't this number. Do you want to try again? (Y/any key)");
                        choice = reader.nextLine();
                        if (choice.equals("y") || choice.equals("Y")) {
                            System.out.print("Enter the action number from list above: ");
                            action("customers", reader.nextInt(), connection, statement);
                        }
                        break;
                }
                break;
        }
    }

    public static void main(String[] args) {
        boolean flag = false;

        System.out.println("Welcome to the app.\nYou can work with database using this app.\n");
        System.out.println("You can read, write, update and delete data from database \"task_sql\"");
        System.out.println("There are 5 tables to use:");

        while (!flag) {
            try {

                Class.forName(JDBC_DRIVER);
                Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                Statement statement = connection.createStatement();

                System.out.println("\t1. developers.\n\t2. skills\n\t3. projects\n\t4. companies\n\t5. customers\nFor exit enter 'q'.");
                System.out.print("Choose a table for working: ");
                choice = reader.nextLine();
                if (CommonMethods.isNumber(choice))
                    switch (choice) {
                        case "1":
                            choice = "developers";
                            break;
                        case "2":
                            choice = "skills";
                            break;
                        case "3":
                            choice = "projects";
                            break;
                        case "4":
                            choice = "companies";
                            break;
                        case "5":
                            choice = "customers";
                            break;
                        default:
                            System.out.println("You wrote wrong number or name of the table.\n" +
                                    "Do you want to try again? (Y/any key)");
                            choice = reader.nextLine();
                            if (!choice.equals("y") && !choice.equals("Y"))
                                return;
                            break;
                    }


                if (!choice.equals("q"))
                    action(choice, DBTest.choosingAction(choice), connection, statement);
                else {
                    System.out.println("Thank you for using this application! Good bye!");
                    return;
                }

                System.out.println("Do you want to exit? (Y/any key)");
                choice = reader.nextLine();
                if (choice.equals("y") || choice.equals("Y"))
                    flag = true;

            } catch (ClassNotFoundException e) {
                System.out.println("There is a problem with JDBC driver..\n" +
                        "Please, check the driver on your PC.");
                flag = true;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException e) {
                System.out.println("There is a problem with query. Try again. If a problem is " +
                        "repeat you must inform the developer about it.");
            } catch (SQLException e) {
                if (e.getMessage().equals("No suitable driver found for")) {
                    System.out.println("There is a problem with connection. " +
                            "Check the setting of the app..");
                    flag = true;
                } else System.out.println("Unknown SQL exception. Please, try again. " +
                        "Write the developer if the situation will repeat.");
            }
        }
    }
}
