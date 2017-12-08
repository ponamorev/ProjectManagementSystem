package com.andersen.crud;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

class CommonView {
    static boolean flag = false;
    private static StringBuilder builderTables = new StringBuilder(),
            builderColumns = new StringBuilder(), builder = new StringBuilder();
    private static Scanner reader = new Scanner(System.in);
    private static LinkedList<String> listTablesLink = new LinkedList<>();


    private static String[] getListColumns(String table) {
        switch (table) {
            case "developers":
                return new String[]{"id", "name", "salary"};
            case "skills":
                return new String[]{"id", "specialty"};
            case "projects":
                return new String[]{"id", "name", "description", "start",
                        "deadline", "cost"};
            case "companies":
                return new String[]{"id", "name", "address"};
            case "customers":
                return new String[]{"id", "name"};
            case "developers_skills":
                return new String[]{"id_developer", "id_skill"};
            case "developers_projects":
                return new String[]{"id_developer", "id_projects"};
            case "developers_companies":
                return new String[]{"id_developer", "id_company"};
            case "projects_skills":
                return new String[]{"id_project", "id_skill"};
            case "companies_projects":
                return new String[]{"id_company", "id_project"};
            case "companies_customers":
                return new String[]{"id_company", "id_customer"};
            case "customers_projects":
                return new String[]{"id_customer", "id_project"};
            default:
                System.out.println("You wrote nonexistent table.");
                return new String[0];
        }
    }

    static void viewData(Connection connection) throws SQLException {
        listTablesLink.add("developers_skills");
        listTablesLink.add("developers_projects");
        listTablesLink.add("developers_companies");
        listTablesLink.add("projects_skills");
        listTablesLink.add("companies_projects");
        listTablesLink.add("companies_customers");
        listTablesLink.add("customers_projects");

        System.out.println("Select the tables to search data (separated by a comma)..");
        builderTables.append(reader.nextLine());
        String[] tables = builderTables.toString().split(",");
        String select;
        String[] columns;
        for (String tab : tables) {

            tab = tab.trim();

            builder.delete(0, builder.length());

            System.out.println("Select the columns for output from the table '" + tab + "' " +
                    "(separated by a comma). For select all columns enter 'all'..");

            CommonMethods.printTableColumns(tab, connection);
            select = reader.nextLine();
            if (!select.equals("all")) {
                builder.append(select);
                columns = builder.toString().split(",");
                for (String c : columns)
                    c = c.trim();
            } else
                columns = getListColumns(tab);

            for (String column : columns)
                builderColumns.append(tab).append(".").append(column).append(", ");
        }

        builder.delete(0, builder.length());
        System.out.println("Enter one and more values or range of values.\n" +
                "If you enter string or date value you will write this value " +
                "in follow format: string_column = 'some_value'.\nFor example, " +
                "'table.column = value' or 'table1.column1 = value1, table2.column2 " +
                "> value2' or 'table.column BETWEEN min_value AND max_value'..");
        select = reader.nextLine();
        builder.append(select);

        while (!flag) {

            System.out.println("Would you like to add another value to search? (Y/any key)");
            String choice = reader.nextLine();

            if (choice.equals("y") || choice.equals("Y")) {

                select = reader.nextLine();

                System.out.println("It's required condition? (Y/any key)");
                choice = reader.nextLine();

                if (choice.equals("y") || choice.equals("Y"))
                    builder.append(" AND ").append(select);
                else builder.append(" OR ").append(select);
            } else flag = true;
        }

        String query;
        CommonMethods.checkQueryAndOut(query, builderColumns.toString().split(", "), connection);
    }

    private static String oneTable(String table, String columns, String condition) {
        return "SELECT " + columns + " FROM " + table + " WHERE " + condition;
    }

    private static String twoTable(String tab1, String tab2, String columns, String condition) {
        String newTable = tab1 + "_" + tab2;
        if (listTablesLink.contains(newTable)) {
            String[] columnsNewTable = getListColumns(newTable);
            return "SELECT " + columns + " FROM " + tab1 + " INNER JOIN " + tab2 + " JOIN " +
                    newTable + " ON " + tab1 + ".id = " + newTable + "." + columnsNewTable[0] +
                    " AND " + tab2 + ".id = " + newTable + "." + columnsNewTable[1] + " AND " +
                    condition;
        } else {
            newTable = tab2 + "_" + tab1;
            if (listTablesLink.contains(newTable)) {
                String[] columnsNewTable = getListColumns(newTable);
                return "SELECT " + columns + " FROM " + tab1 + " INNER JOIN " + tab2 + " JOIN " +
                        newTable + " ON " + tab1 + ".id = " + newTable + "." + columnsNewTable[0] +
                        " AND " + tab2 + ".id = " + newTable + "." + columnsNewTable[1] + " AND " +
                        condition;
            }
            else {
                String newTables[] = new String[2];
                if (listTablesLink.contains(tab1 + "_projects"))
                    newTables[0] = tab1 + "_projects";
                else newTables[0] = "projects_" + tab1;
                if (listTablesLink.contains(tab2 + "_projects"))
                    newTables[1] = tab2 + "_projects";
                else newTables[1] = "projects_" + tab2;
                String[] columnsNewTable1 = getListColumns(newTables[0]);
                String[] columnsNewTable2 = getListColumns(newTables[1]);

                return "SELECT " + columns + " FROM " + tab1 + " INNER JOIN " + tab2 + " JOIN projects JOIN " +
                        newTables[0] + " JOIN " + newTables[1] + " ON " + tab1 + ".id = " + newTables[0] +
                        "." + columnsNewTable1[0] +
            }
        }
    }
}
