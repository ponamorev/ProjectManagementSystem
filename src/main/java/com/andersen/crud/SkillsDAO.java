package com.andersen.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

class SkillsDAO {
    private Scanner reader = new Scanner(System.in);
    private String query, choice;
    private ResultSet result;
    private int count;
    private Skills skill;

    SkillsDAO(Skills skill) {
        this.skill = skill;
    }


    private Skills checkSkill(int id, String specialty, Statement statement)
            throws SQLException, NullPointerException {
        Skills chSkill = null;

        if (id != 0) {
            query = "SELECT * FROM skills WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                chSkill = new Skills(result.getInt("id"),
                        result.getString("specialty"));
            return chSkill;
        } else if (!specialty.equals("") ||
                !Objects.equals(specialty, null)) {
            query = "SELECT * FROM developers WHERE name = '" + specialty + "'";
            result = statement.executeQuery(query);
            while (result.next())
                chSkill = new Skills(result.getInt("id"),
                        result.getString("name"));
            return chSkill;
        }

        return null;
    }

    private void unsuccessUpOrDel(Connection connection, Statement statement)
            throws SQLException {
        Skills chSkill;
        if (choice.equals("y") || choice.equals("Y")) {
            System.out.print("Enter the name: ");
            chSkill = new Skills(0, reader.nextLine());
            createSkill(chSkill, connection, statement);
        }
    }

    void addSkillLink(Skills skill, String table,
                      Connection connection, Statement statement) throws SQLException {
        String tableObjectName = "", columns[] = new String[1];
        switch (table) {
            case "developers":
                tableObjectName = "developer";
                columns = new String[]{"id", "name", "salary"};
                break;
            case "projects":
                tableObjectName = "project";
                columns = new String[]{"id", "name", "description", "start", "deadline", "cost"};
                break;
        }

        query = "SELECT * FROM " + table;
        CommonMethods.checkQueryAndOut(query, columns, connection);
        do {
            System.out.println("Enter the " + tableObjectName + "..");
            choice = reader.nextLine();
            if (CommonMethods.isNumber(choice)) {
                query = "INSERT INTO " + table + "_skills VALUES (" + choice +
                        ", " + skill.getID() + ")";
                System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                choice = reader.nextLine();
            } else {
                query = "SELECT id FROM " + table + " WHERE name = '" + choice + "'";
                result = statement.executeQuery(query);
                int id = 0;
                while (result.next())
                    id = result.getInt(1);

                if (id != 0) {
                    query = "INSERT INTO " + table + "_skills VALUES (" + id +
                            ", " + skill.getID() + ")";
                    System.out.println("Add " + statement.executeUpdate(query) + " row(-s) in the table..");

                    System.out.println("Would you like to add another " + tableObjectName + "? (Y/any key)");
                    choice = reader.nextLine();
                } else {
                    System.out.println("You wrote non-exists " + tableObjectName + ".. ");
                    System.out.println("Would you like to try again? (Y/any key)");
                    choice = reader.nextLine();
                }
            }
        } while (Objects.equals(choice, "Y") || Objects.equals(choice, "y"));
    }


    void createSkill(Skills skill, Connection connection, Statement statement)
            throws SQLException, NullPointerException {
        String ID, specialty;
        int id;

        if (skill == null) {

            System.out.print("Enter ID (optional, click enter for skip): ");
            ID = reader.nextLine();
            if (ID.equals(""))
                ID = "0";
            id = Integer.parseInt(ID);

            System.out.print("Enter the specialty: ");
            specialty = reader.nextLine();

            skill = new Skills(id, specialty);

            // Check specialty
            query = "SELECT COUNT(specialty) FROM skills WHERE specialty = '" + skill.getSpecialty() + "'";
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                System.out.println("The table already exists this skill..");
                return;
            }
        } else id = skill.getID();

        // Check ID
        if (id != 0) {
            count = 0;
            query = "SELECT COUNT(id) FROM skills WHERE id = " + id;
            result = statement.executeQuery(query);
            while (result.next())
                count = result.getInt(1);
            if (count > 0) {
                while (count != 0) {
                    query = "SELECT COUNT(id) FROM skills WHERE id = " + ++id;
                    result = statement.executeQuery(query);
                    while (result.next())
                        count = result.getInt(1);
                }
                System.out.println("Chosen ID is busied." +
                        " The new skill will have ID = " + id);
                skill.setID(id);

                query = "INSERT INTO skills VALUES (" + id + ", '" + skill.getSpecialty() + "')";
                System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
            }
        } else {
            query = "INSERT INTO skills VALUES (NULL, '" + skill.getSpecialty() + "')";
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");
        }

        query = "SELECT * FROM skills WHERE specialty = '" + skill.getSpecialty() + "'";
        result = statement.executeQuery(query);
        while (result.next())
           this.skill = new Skills(result.getInt("id"), result.getString("specialty"));

        addSkillLink(this.skill, "developers", connection, statement);

        addSkillLink(this.skill, "projects", connection, statement);

        System.out.println("Would you like to add another skill? (Y/any key)");
        choice = reader.nextLine();
        if (choice.equals("y") || choice.equals("Y"))
            createSkill(null, connection, statement);
    }


    void updateSkill(Connection connection, Statement statement)
            throws SQLException {
        if (skill == null) {
            SkillsView view = new SkillsView();
            skill = view.findSkill(connection, reader);
        }

        StringBuilder builder = new StringBuilder();

        CommonMethods.printTableColumns("skills", connection);
        System.out.println("Enter a new value. You should write it in single quotes.");
        builder.append("specialty").append(" = ").append(reader.nextLine());

        if (skill != null) {
            query = "UPDATE skills SET " + builder.toString() + " WHERE id = " + skill.getID();
            System.out.println(statement.executeUpdate(query) + " row(-s) changed.\n");

        } else {
            System.out.println("The skill wasn't found. Would you like " +
                    "to add a new skill? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }

    }


    // return the skill which was deleted
    void deleteSkill(Skills skill, Connection connection, Statement statement)
            throws SQLException {

        if (skill == null)
            if (this.skill != null)
                skill = this.skill;
            else
                skill = null;

        if (skill != null) {
            query = "SELECT * FROM skills WHERE id = " + skill.getID() +
                    " OR specialty = '" + skill.getSpecialty() + "' LIMIT 1";
            result = statement.executeQuery(query);
            while (result.next())
                this.skill = new Skills(result.getInt("id"), result.getString("specialty"));

            query = "DELETE FROM developers_skills WHERE id_skill = " + this.skill.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from developer_" +
                    "skills..");
            query = "DELETE FROM projects_skills WHERE id_skill = " + this.skill.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from projects_skills..");

            query = "DELETE FROM skills WHERE id = " + this.skill.getID();
            System.out.println("Delete " + statement.executeUpdate(query) + " from the skills..");
        } else {
            System.out.println("The skill wasn't found. Would you like " +
                    "to add a new skill? (Y/any key)");
            choice = reader.nextLine();
            unsuccessUpOrDel(connection, statement);
        }
    }
}
