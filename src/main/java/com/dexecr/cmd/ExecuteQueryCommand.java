package com.dexecr.cmd;

import com.dexecr.sql.QueryExecutor;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(name = "execute")
class ExecuteQueryCommand implements Runnable {

    @ParentCommand
    private SqlctlCommand base;

    @Parameters
    private String query;

    @Override
    public void run()  {
        try (var queryExecutor = new QueryExecutor(base.databaseInfoGroup)) {
            queryExecutor.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
