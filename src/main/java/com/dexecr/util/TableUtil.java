package com.dexecr.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TableUtil {

    public static void printTable(List<String> headers, Iterator<List<String>> rows) {
        if (headers == null || rows == null) {
            return;
        }
        int[] maxColumnsLength = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            maxColumnsLength[i] = Math.max(maxColumnsLength[i], headers.get(i).length());
        }
        List<List<String>> cachedRows = new ArrayList<>();
        rows.forEachRemaining(row -> {
            for (int i = 0; i < row.size(); i++) {
                maxColumnsLength[i] = Math.max(maxColumnsLength[i], row.get(i) == null ? 0 : row.get(i).length());
            }
            cachedRows.add(row);
        });
        var sbRow = new StringBuilder();
        var sbDelimiter = new StringBuilder();
        for (int columnLength : maxColumnsLength) {
            sbRow.append(" %").append(columnLength).append("s |");
            var delimiter = new char[columnLength + 2];
            Arrays.fill(delimiter, '-');
            sbDelimiter.append(new String(delimiter)).append('+');
        }
        if (!sbRow.isEmpty()) {
            sbRow.setLength(sbRow.length() - 1);
            sbRow.append("%n");
            sbDelimiter.setLength(sbDelimiter.length() - 1);
        }
        String rowFormat = sbRow.toString();
        String rowDelimiter = sbDelimiter.toString();
        System.out.printf(rowFormat, headers.toArray());
        System.out.println(rowDelimiter);
        cachedRows.forEach(row -> System.out.printf(rowFormat, row.toArray()));
    }
}
