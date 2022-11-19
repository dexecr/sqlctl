package com.dexecr.sql;

import com.dexecr.util.TableUtil;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryExecutor implements AutoCloseable {

    private final Connection connection;

    @SneakyThrows
    public QueryExecutor(DatabaseInfo databaseInfo) {
        var driver = databaseInfo.getDriver();
        var url = driver.buildUrl(databaseInfo.getHost(), databaseInfo.getPort(), databaseInfo.getDatabase());
        connection = driver.getDriverInstance().connect(url, driver.info.build(databaseInfo));
    }

    @SneakyThrows
    public void execute(String query) {
        try (var statement = connection.createStatement()){
            var isRs = statement.execute(query);
            if (isRs) {
                try(var rs = statement.getResultSet()) {
                    var headers = headers(rs.getMetaData());
                    TableUtil.printTable(headers, new Iterator<>() {

                        @Override
                        @SneakyThrows
                        public boolean hasNext() {
                            return rs.next();
                        }

                        @Override
                        @SneakyThrows
                        public String[] next() {
                            String[] result = new String[headers.size()];
                            for (int i = 1; i <= headers.size(); i++) {
                                result[i - 1] = rs.getString(i);
                            }
                            return result;
                        }
                    });
                }
            } else {
                System.out.println("Row updated: " + statement.getUpdateCount());
            }
        }
    }

    @SneakyThrows
    private static List<String> headers(ResultSetMetaData metaData) {
        List<String> headers = new ArrayList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            headers.add(metaData.getColumnLabel(i));
        }
        return headers;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
