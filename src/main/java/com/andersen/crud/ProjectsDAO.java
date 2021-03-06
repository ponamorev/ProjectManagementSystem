package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

class ProjectsDAO {
    private Scanner reader = new Scanner(System.in);
    private String query, choice;
    private ResultSet result;
    private int count;
    private Projects project;

    ProjectsDAO(Projects project) {
        this.project = project;
    }


    private void unsuccessUpOrDel(Connection connection, Statement statement)
            throws SQLException, NullPointerException {
        Projects chProject;
        String start, deadline = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(startDate);
        instance.add(Calendar.YEAR, 1);
        Date endDate = instance.getTime();

        if (choice.equals("y") || choice.equals("Y")) {
            System.out.print("Enter the name: ");
            chProject = new Projects(0, reader.nextLine(), "", "", "", 0);
            System.out.println("Add description? (Y/any key)");
            choice = reader.nextLine();
            if (choice.equals("y") || choice.equals("Y")) {
                System.out.println("Enter the description..");
                chProject.setDescription(reader.nextLine());
            }
            try {
                System.out.println("Do you want to set the default deadline for the project " +
                        "execution (one year)? (Y/any key)");
                choice = reader.nextLine();
                if (choice.equals("y") || choice.equals("Y")) {
                    start = format.format(startDate);
                    deadline = format.format(endDate);
                } else {
                    boolean date = true;
                    System.out.print("Start of the project (YYYY-MM-DD): ");
                    start = reader.nextLine();
                    start = CommonMethods.checkDate(start);
                    startDate = format.parse(start);

                    while (date) {
                        System.out.print("End of the project (YYYY-MM-DD): ");
                        deadline = reader.nextLine();
                        deadline = CommonMethods.checkDate(deadline);
                        endDate = format.parse(deadline);

                        if (startDate.before(endDate))
                            date = false;
                        else System.out.println("End of the project can't be early than start! Try again.");
                    }
                }
            } catch (ParseException e) {
                System.out.println("There is a problem with parsing date. The deadline will set default.");
                start = "";
                deadline = "";
            }
            if (start.equals(""))
                start = format.format(startDate);
            if (deadline.equals(""))
                deadline = format.format(endDate);

            chProject.setStart(start);
            chProject.setDeadline(deadline);

            System.out.print("Project cost: ");
            chProject.setCost(Integer.parseInt(reader.nextLine()));

            createProject(chProject, connection, statement);
        }
    }

    void addProjectLink(Projects project, String table,
                        Connection connection, Statement statement) throws SQLException {
        String tableObjectName = "", columns[] = new String[1];
        switch (table) {
            case "developers":
                tableObjectName = "developer";
                columns = new String[]{"id", "name", "salary"};
                break;
            case "skills":
                tableObjectName = "skill";
                columns = new String[]{"id", "specialty"};
                break;
            case "companies":
                tableObjectName = "company";
                columns = new String[]{"id", "name", "address"};
                break;
            case "customers":
                tableObjectName = "customer";
                columns = new String[]{"id", "name"};
                break;
        }

        query = "SELECT * FROM " + table;
        CommonMethods.checkQueryAndOut(query, columns, connection);
        do {
            System.out.println("Enter the " + tableObjectName + "..");
            choice = reader.nextLine();
            if (CommonMethods.isNumber(choice)) {
                if (table.equals("skills"))
                    query = "INSERT INTO projects_" + table + " VALUES (" + project.getID() +
                            ", " + choice + ")";
                else query = "INSERT INTO " + table + "_projects VALUES (" + choice +
                        ", " + project.getID() + ")";
                System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                choice = reader.nextLine();
            } else {
                if (table.equals("skills"))
                    query = "SELECT id FROM " + table + " WHERE specialty = '" + choice + "'";
                else query = "SELECT id FROM " + table + " WHERE name = '" + choice + "'";

                result = statement.executeQuery(query);
                int id = 0;
                while (result.next())
                    id = result.getInt(1);

                if (id != 0) {
                    if (table.equals("skills"))
                        query = "INSERT INTO projects_" + table + " VALUES (" + project.getID() +
                                ", " + id + ")";
                    else query = "INSERT INTO " + table + "_projects VALUES (" + id +
                            ", " + project.getID() + ")";
                    System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                    System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                    choice = reader.nextLine();
                } else {
                    System.out.println("You wrote non-exists " + tableObjectName + ".. ");
                    System.out.println("Would you like to try again? (Y/any key)");
                    choice = reader.nextLine();
                }
            }
        } while (choice.equals("y") || choice.equals("Y"));
    }


    void createProject(Projects project, Connection connection, Statement statement)
            throws SQLException {
        Date startDate = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(startDate);
        instance.add(Calendar.YEAR, 1);
        Date endDate = instance.getTime();
        String ID, name, description, start, deadline = "";
        int cost, id = 0;

        if (project == null) {

            do {
                System.out.print("Enter ID (optional, click enter for skip): ");
                ID = reader.nextLine();
                if (ID.equals(""))
                    ID = "0";
                if (CommonMethods.isNumber(ID))
                    id = Integer.parseInt(ID);
                else System.out.println("You didn't write a number! Try again.");
            } while (!CommonMethods.isNumber(ID));

            System.out.print("Enter the name: ");
            name = reader.nextLine();

            System.out.println("Would you like to add a description? (Y/any key)");
            choice = reader.nextLine();
            if (choice.equals("y") || choice.equals("Y")) {
                System.out.print("Enter a description: ");
                description = reader.nextLine();
            } else description = "";

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                System.out.println("Do you want to set the default deadline for the project " +
                        "execution (one year)? (Y/any key)");
                choice = reader.nextLine();
                if (choice.equals("y") || choice.equals("Y")) {
                    start = format.format(startDate);
                    deadline = format.format(endDate);
                } else {
                    boolean date = true;
                    System.out.print("Enter the start of the project (YYYY-MM-DD): ");
                    start = reader.nextLine();
                    start = CommonMethods.checkDate(start);
                    startDate = format.parse(start);

                    while (date) {
                        System.out.print("Enter the end of the project (YYYY-MM-DD): ");
                        deadline = reader.nextLine();
                        deadline = CommonMethods.checkDate(deadline);
                        endDate = format.parse(deadline);

                        if (startDate.before(endDate))
                            date = false;
                        else System.out.println("End of the project can't be early than start! Try again.");
                    }
                }

            } catch (ParseException e) {
                System.out.println("There is a problem with parsing date. The deadline will set default.");
                start = "";
                deadline = "";
            }

            if (start.equals("")) {
                start = format.format(startDate);
            }
            if (deadline.equals(""))
                deadline = format.format(endDate);

            System.out.print("Enter the project cost: ");
            cost = reader.nextInt();
            reader.nextLine();

            project = new Projects(id, name, description, start, deadline, cost);

        } else id = project.getID();

        // Check name
        query = "SELECT COUNT(name) FROM projects WHERE name = '" + project.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            count = result.getInt(1);
        if (count > 0) {
            System.out.println("The table already exists the project" +
                    " which has name '" + project.getName() + "'.");
            return;
        }

        // Check ID
        if (id != 0) {
            count = 0;
            query = "SELECT COUNT(id) FROM projects WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                while (count != 0) {
                    query = "SELECT COUNT(id) FROM projects WHERE id = " + ++id;
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                }
                System.out.println("This ID is busied." +
                        " The new project will have ID = " + id);
                project.setID(id);
            }

            query = "INSERT INTO projects VALUES (" + id + ", '" + project.getName() +
                    "', '" + project.getDescription() + "', '" + project.getStart() +
                    "', '" + project.getDeadline() + "', " + project.getCost() + ")";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            query = "INSERT INTO projects VALUES (NULL, '" + project.getName() +
                    "', '" + project.getDescription() + "', '" + project.getStart() +
                    "', '" + project.getDeadline() + "', " + project.getCost() + ")";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
        }

        query = "SELECT * FROM projects WHERE name = '" + project.getName() + "'";
        result = statement.executeQuery(query);
        while (result.next())
            this.project = new Projects(result.getInt("id"),
                    result.getString("name"), result.getString("description"),
                    result.getString("start"), result.getString("deadline"),
                    result.getInt("cost"));

        addProjectLink(this.project, "skills", connection, statement);

        addProjectLink(this.project, "companies", connection, statement);

        addProjectLink(this.project, "customers", connection, statement);

        System.out.println("Would you like to add another project? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y"))
            createProject(null, connection, statement);
    }


    void updateProject(Connection connection, Statement statement)
            throws SQLException {
        if (project == null) {
            ProjectsView view = new ProjectsView();
            project = view.findProject(connection, reader);
        }

        StringBuilder builder = new StringBuilder();
        boolean flag = false;
        String column, value;

        CommonMethods.printTableColumns("projects", connection);
        while (!flag) {
            System.out.println("Choose a column to change (except ID)..");
            column = reader.nextLine();
            builder.append(column);
            System.out.println("Enter a new value. If you chose a string or a date column, you should write a new value " +
                    "in single quotes.\nDate must be written in format 'YYYY-MM-DD'.");
            value = reader.nextLine();
            if (column.equals("start") || column.equals("deadline")) {
                value = CommonMethods.checkDate(value);
            }
            builder.append(" = ").append(value);
            System.out.println("Would you like to change another column? (Y/any key)");
            choice = reader.nextLine();
            if (!choice.equals("y") && !choice.equals("Y"))
                flag = true;
            else builder.append(", ");
        }

        if (project != null) {
            query = "UPDATE projects SET " + builder.toString() + " WHERE id = " + project.getID();
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            System.out.println("The project wasn't found. Would you like " +
                    "to add a new project? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }

    }


    // return the developer which was deleted
    void deleteProject(Projects project, Connection connection, Statement statement)
            throws SQLException {

        if (project == null)
            if (this.project != null)
                project = this.project;
            else
                project = null;

        if (project != null) {
            query = "SELECT * FROM projects WHERE id = " + project.getID() +
                    " OR name = '" + project.getName() + "' OR description = '" +
                    project.getDescription() + "' OR start = '" +
                    project.getStart() + "' OR deadline = '" + project.getDeadline() +
                    "' OR cost = " + project.getCost() + " LIMIT 1";
            result = statement.executeQuery(query);
            while (result.next())
                this.project = new Projects(result.getInt("id"),
                    result.getString("name"), result.getString("description"),
                    result.getString("start"), result.getString("deadline"),
                    result.getInt("cost"));

            query = "DELETE FROM developers_projects WHERE id_project = " + this.project.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developers_" +
                    "projects..");
            query = "DELETE FROM projects_skills WHERE id_project = " + this.project.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from projects_" +
                    "skills..");
            query = "DELETE FROM companies_projects WHERE id_project = " + this.project.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from companies_" +
                    "projects..");
            query = "DELETE FROM customers_projects WHERE id_project = " + this.project.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from customers_" +
                    "projects..");

            query = "DELETE FROM projects WHERE id = " + this.project.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from the projects..");
        } else {
            System.out.println("The project wasn't found. Would you like " +
                    "to add a new project? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }
    }
}
