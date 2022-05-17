package ru.kpfu.itis.java2.srvergasov.kr;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final int DELTA_TIME = 10000;
    private static String url = "";
    private static String domain = "facebook";

    public static void init() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("data/app.properties")) {
            properties.load(in);
            url = properties.getProperty("url");
        } catch (IOException ex) {
            System.out.println("Fail to get properties");
        }
    }

    public static void main(String[] args) {
        init();
        String json = getJson();
        if (json != null) {
            Pattern pattern = Pattern.compile("\"domain\": \"([a-zA-Z-.0-9_]+)\"");
            Matcher matcher = pattern.matcher(json);

            Pattern pattern1 = Pattern.compile("\"create_date\": \"([0-9-:A-Z.a-z]+)\"");
            Matcher matcher1 = pattern1.matcher(json);

            Pattern pattern2 = Pattern.compile("\"update_date\": \"([0-9-:A-Z.a-z]+)\"");
            Matcher matcher2 = pattern2.matcher(json);

            Pattern pattern3 = Pattern.compile("\"country\": (?:\"|)([0-9-:A-Z.a-z]+)(?:\"|)");
            Matcher matcher3 = pattern3.matcher(json);

            while (matcher.find() && matcher1.find() && matcher2.find() && matcher3.find()) {
                int date = checkDate(matcher1.group(1), matcher2.group(1));
                String site = matcher.group(1);
                if (date < DELTA_TIME) {
                    writeCsv(site, date, getUrlSize(site));
                }
            }
        }
    }

    public static int checkDate(String d1, String d2) {
        LocalDateTime date1 = LocalDateTime.parse(d1);
        LocalDateTime date2 = LocalDateTime.parse(d2);
        //TODO
        return 100;
    }

    public static int getUrlSize(String url) {
        //TODO
        return 1000;
    }

    public static String getJson() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("data/response.json")
                    ));
            return in.readLine();
        } catch (IOException ex) {
            System.out.println("failed to get json");
        }
        return null;
    }

    public static void writeCsv(String s1, int s2, int s3) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("data/result.csv")
                    ));
            out.write(s1 + ",");
            out.write(s2 + ",");
            out.write(s3 + ",");
            out.flush();
            out.close();
        } catch (IOException ex) {
            System.out.println("failed to write in CSV file");
        }
    }
}

/*
3 вариант
пользователь вводит домен
программа запрос из ответа в ксв записывает те у кого срок заканчивается например через 2 месяца
помимо имени срок обновления, страна, размер странички по этому доменному имени

 */