package com.dexecr.cmd;

import com.dexecr.driver.Vendor;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(subcommands = ExecuteQueryCommand.class)
public class SqlctlCommand {
    @Option(names = {"-u", "--username"}, scope = CommandLine.ScopeType.INHERIT)
    String username;

    @Option(names = {"-p", "--password"}, interactive = true, scope = CommandLine.ScopeType.INHERIT)
    char[] password;

    @Option(names = {"-h", "--host"}, defaultValue = "localhost", scope = CommandLine.ScopeType.INHERIT)
    String host;

    @Option(names = {"--port"}, scope = CommandLine.ScopeType.INHERIT)
    String port;

    @Option(names = {"-d", "--database"}, scope = CommandLine.ScopeType.INHERIT)
    String database;

    @Option(names = {"--url"}, scope = CommandLine.ScopeType.INHERIT)
    String url;

    @Option(names = {"--vendor"}, scope = CommandLine.ScopeType.INHERIT)
    Vendor vendor;
}
