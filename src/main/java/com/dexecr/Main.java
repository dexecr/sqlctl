package com.dexecr;

import com.dexecr.cmd.SqlctlCommand;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        int resultCode = new CommandLine(new SqlctlCommand()).execute(args);
        System.exit(resultCode);
    }
}
