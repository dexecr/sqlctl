package com.dexecr.cmd;

import com.dexecr.sql.DatabaseInfo;
import lombok.Getter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(subcommands = {
        ExecuteQueryCommand.class,
        ConnectCommand.class
})
public class SqlctlCommand implements Runnable, DatabaseInfo {

    @Getter
    @Option(names = {"-u", "--username"})
    private String username;

    @Getter
    @Option(names = {"-p", "--password"}, interactive = true)
    private char[] password;

    @Getter
    @Option(names = {"--url"})
    private String url;

    @Override
    public void run() {
        new ConnectCommand(this).run();
    }
}
