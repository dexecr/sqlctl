package com.dexecr.cmd;

import com.dexecr.sql.DatabaseInfo;
import com.dexecr.sql.DriverType;
import lombok.Getter;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(subcommands = {
        ExecuteQueryCommand.class,
        ConnectCommand.class,
})
public class SqlctlCommand implements Runnable {

    @ArgGroup(exclusive = false)
    DatabaseInfoGroup databaseInfoGroup;

    @Override
    public void run() {
        new ConnectCommand(this).run();
    }

    @Getter
    static class DatabaseInfoGroup implements DatabaseInfo {

        @Option(names = {"-u", "--username"}, defaultValue = "${sys:user.name}")
        private String username;

        @Option(names = {"-p", "--password"}, interactive = true)
        private char[] password;

        @Option(names = { "--host", "-h" }, defaultValue = "localhost")
        private String host;

        @Option(names = { "--port", "-P" }, defaultValue = "5432")
        private int port;

        @Option(names = { "--database", "-D" })
        private String database;

        @Option(names = { "--driver", "-d" }, defaultValue = "postgresql")
        private DriverType driver;
    }
}
