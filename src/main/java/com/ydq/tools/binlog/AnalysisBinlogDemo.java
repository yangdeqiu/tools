package com.ydq.tools.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogFileReader;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;

import java.io.File;
import java.io.IOException;

public class AnalysisBinlogDemo {

    public static void main(String[] args) throws IOException {
        readSql();
    }

    private static void readSql() throws IOException {
        String filePath="D:\\mysql\\mysql-5.7.16-winx64\\logs\\mysql-bin.000001";
        File binlogFile = new File(filePath);
        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        BinaryLogFileReader reader = new BinaryLogFileReader(binlogFile, eventDeserializer);
        try {
            for (Event event; (event = reader.readEvent()) != null; ) {
                System.out.println(event.toString());
            }
        } finally {
            reader.close();
        }
    }

    private static void analysisSql() throws IOException {
        BinaryLogClient client = new BinaryLogClient("localhost", 3306, "root", "root");
        client.setBinlogFilename("F:/BIN_LOG/binLogs/mysql-bin.052374.sql");
        client.setBlocking(false);
        client.registerEventListener(System.out::println);
        client.connect();
    }

}
