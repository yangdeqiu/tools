package com.ydq.tools.binlog;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 手动读取bingLog文件内容，并输出到文件
 * 逻辑： 从开始关键字开始行读，读到结束关键字为结束行
 * 注意： 1.需要检查每个文件结尾和开头是否会存在某个sql的一部分，即文件分隔导致了sql不完整问题
 */
public class AnalysisBinlogAutoDemo {

    // 需要读取binlog文件的目录
    private static final String logDir = "F:/BIN_LOG/binLogs";
    // 结果输出路径
    private static final String retFilePath = "F:/BIN_LOG/binLogResult.sql";

    // 开始关键字
    private static final List<String> startFlagList = Arrays.asList("### INSERT", "### UPDATE");
    // 结束关键字
    private static final List<String> EndFlagList = Arrays.asList("### INSERT", "### UPDATE", "# at");

    // 累计总数
    private static int totalCnt = 0;

    // true=输出控制台， false=输出文件
    private static boolean isConsole = false;


    public static void main(String[] args) {

        File dir = new File(logDir);
        if (dir.isFile()) {
            System.err.println("【error】需要配置带读取文件夹");
            return;
        }
        File[] files = dir.listFiles();
        if (Objects.isNull(files)) {
            System.err.println("【warn】指定文件夹为空");
            return;
        }
        // 以文件名自然序排序，保证事务的先后顺序
        TreeMap<String, File> fileMap = new TreeMap<>(Comparator.naturalOrder());
        for (File file : files) {
            fileMap.put(file.getName(), file);
        }
        fileMap.forEach((nameKey, fileVal) -> {
            // 读取每个文件，依次解析并输出到结果文件
            handleLogFile(fileVal);
        });
    }

    private static void handleLogFile(File fileVal) {
        System.out.println("【info】 ==> 开始读取文件：" + fileVal.getName());
        // 避免结果文件输出流中断，每次读取建立一次输出流
        long start = System.currentTimeMillis();
        int cnt = 0;
        try (
                FileOutputStream fos = new FileOutputStream(retFilePath, true);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(fileVal))
                )
        ) {

            // 从开始关键字开始行读，读到结束关键字为结束行, 非开始情况直接跳过
            String line = null;
            // true : 上次结束行就是开始行
            boolean isNextStart = false;
            StringBuilder sql = new StringBuilder();
            String tableName = null;
            while ((line = br.readLine()) != null) {
                // 从【开始行】读开始，循环读到【结束行】做一次提交，再从结束行就开始判断开始下次OR结束当前循环
                if (!isNextStart && !isStartLine(line)) {
                    continue;
                }

                // 通过带表名的开始行获取表名
                if (!isNextStart) {
                    tableName = getTableNameByLine(line);
                }
                do {

                    // 解析对应表指定下标字段名，并替换到sql语句行
                    line = line.substring(3);
                    line = handleTableFieldName(tableName, line);
                    sql.append(line).append("\r\n");
                    line = br.readLine();
                    if (Objects.isNull(line)) {
                        break;
                    }
                } while (!isEndLine(line));
                // 执行sql输出
                sql.append(";").append("\r\n\r\n");
                if (isConsole) {
                    System.out.println(sql.toString() + "\r\n");
                } else {
                    fos.write(sql.toString().getBytes(StandardCharsets.UTF_8));
                }
                sql.setLength(0);
                cnt++;

                if (isNextStart = isStartLine(line)) {
                    sql.append(line.substring(3)).append("\r\n");
                    tableName = getTableNameByLine(line);
                }
            }

            if (isNextStart && !StringUtils.isBlank(sql)) {
                System.err.println("【ERROR】 异常结束行： " + sql);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        totalCnt += cnt;
        System.out.println("【info】 <== 结束读取文件：" + fileVal.getName()
                + ", 共读取关键字：" + cnt
                + "项， 耗时：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("【info】 ******************  累计项：" + totalCnt + "  ********************");
    }

    // 替换tableName@i 为指定字段名
    private static String handleTableFieldName(String tableName, String line) {
        // 是否还在检查@并执行替换过程中
        boolean onCheck = true;
        // 结果字符
        StringBuilder lineSb = new StringBuilder();
        for (int i = 0; i < line.length(); i ++) {
            char c = line.charAt(i);
            if (c == ' ') {
                lineSb.append(c);
                continue;
            }
            if (c == '@') { // 是以@开头，满足需要替换条件
                onCheck = false;
                // 待替换字符
                StringBuilder replaceSb = new StringBuilder();
                replaceSb.append(c);
                while (++ i < line.length() && (c = line.charAt(i)) != '=') {
                    replaceSb.append(c);
                }

                // 找到正确的字段名，并执行替换
                String fieldName = TableTools.getFieldName(tableName, replaceSb.toString());
                lineSb.append(fieldName);

                if (i < line.length()) {
                    lineSb.append(c);
                }
            } else {
                if (onCheck) {
                    // 不是以@开始的
                    return line;
                }
                // 替换过程已经结束，原样输出
                lineSb.append(c);
            }
        }
        return lineSb.toString();
    }

    private static String getTableNameByLine(String line) {
        // 从最后一个 ` 开始读， 读到下一个 ` 结束，结果即时表名
        StringBuilder sb = new StringBuilder();
        int len = line.length() - 2;
        for (int i = len; i > 0; i--) {
            char c = line.charAt(i);
            if (c == '`') break;
            sb.insert(0, c);
        }
        return sb.toString();
    }

    private static boolean isEndLine(String line) {
        for (String str : EndFlagList) {
            if (line.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isStartLine(String line) {
        for (String str : startFlagList) {
            if (line.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取表字段名工具类（需要配置jdbc链接信息）
     */
    static class TableTools {

        // jdbc连接信息
        private static String jdbcUrl = "jdbc:mysql://localhost:3306/test_lock?useServerPrepStmts=false&useCompression=true&useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useLocalSessionState=true";
        private static String user = "root";
        private static String pwd = "root";


        // 表字段顺序缓存 <tableName@i, fieldName>
        private static Map<String, String> fieldsCache = new ConcurrentHashMap<>();


        // 通过 tableName@i 获取字段名缓存，没有缓存则查询数据库获取 （获取不到就原样返回）
        private static @NotNull String getFieldName(String tableName, String seq) {
            String fieldKey = tableName + seq;
            if (fieldsCache.containsKey(fieldKey)) {
                return fieldsCache.get(fieldKey);
            }
            fieldsCache.putAll(getFieldsMapSeqByJdbc(tableName));
            return fieldsCache.getOrDefault(fieldKey, seq);
        }

        private static Map<String, String> getFieldsMapSeqByJdbc(String tableName) {
            Map<String, String> map = new HashMap<>();

            Connection conn = null;
            PreparedStatement ps = null;
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(jdbcUrl, user, pwd);
                ps = conn.prepareStatement("Desc " + tableName + ";");
                ResultSet rs = ps.executeQuery();
                int seq = 0;
                while (rs.next()) {
                    seq++;
                    String key = tableName + "@" + seq;
                    String val = rs.getString(1);
                    if (StringUtils.isBlank(val)) {
                        break;
                    }
                    map.put(key, val);
                }
            } catch (Exception e) {
                System.err.println("【error】" + e.toString());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // 没使用连接池，sleep避免频繁建立jdbc连接
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return map;
        }
    }


}
