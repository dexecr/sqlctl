package com.dexecr.cmd;


import com.dexecr.sql.QueryExecutor;
import lombok.NoArgsConstructor;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

@Command(name = "connect")
@NoArgsConstructor
class ConnectCommand implements Runnable {

    @ParentCommand
    private SqlctlCommand base;

    ConnectCommand(SqlctlCommand base) {
        this.base = base;
    }

    @Override
    public void run() {
        try(var executor = new QueryExecutor(base)) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
