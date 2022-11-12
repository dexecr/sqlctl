package com.dexecr.cmd;

import com.dexecr.sql.QueryExecutor;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Command(name = "execute")
class ExecuteQueryCommand implements Runnable {

    @ParentCommand
    private SqlctlCommand base;

    @Option(names = {"-q", "--query"})
    private String query;

    @Override
    public void run()  {
        try (var queryExecutor = new QueryExecutor(base)) {
            queryExecutor.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
