package com.dexecr.cmd;

import com.dexecr.sql.DatabaseInfo;
import lombok.Getter;
import lombok.experimental.Delegate;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(subcommands = {
        ExecuteQueryCommand.class,
        ConnectCommand.class
})
public class SqlctlCommand implements Runnable, DatabaseInfo {

    @ArgGroup(exclusive = false)
    @Delegate
    private DatabaseInfoGroup databaseInfoGroup;

    @Override
    public void run() {
        new ConnectCommand(this).run();
    }

    @Getter
    static class DatabaseInfoGroup {

        @Option(names = {"-u", "--username"})
        private String username;

        @Option(names = {"-p", "--password"}, interactive = true)
        private char[] password;

        @Option(names = "--url")
        private String url;
    }
}
