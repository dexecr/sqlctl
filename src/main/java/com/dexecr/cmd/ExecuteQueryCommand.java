package com.dexecr.cmd;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Properties;

@Command(name = "execute")
public class ExecuteQueryCommand implements Runnable {

    @ParentCommand
    private SqlctlCommand base;

    @Option(names = {"-q", "--query"})
    private String query;

    @Parameters
    private List<String> queryTokens;

    @Override
    public void run()  {

        var driverVendor = base.vendor;
        var properties = new Properties();
        properties.put("user", base.username);
        properties.put("password", new String(base.password));
        try (var connection = driverVendor.getDriverInstance().connect(base.url, properties); var statement = connection.createStatement()){
            var isRs = statement.execute(query);
            if (isRs) {
                ResultSet rs = statement.getResultSet();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println();
                }
            } else {
                System.out.println("Row updated: " + statement.getUpdateCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
