package com.fan.news.utils;

public class PathUtil {
    private static String seperator = System.getProperty("file.seperator");
    public static String getBasePath() {
        String os = System.getProperty("os.name");
        String basePath ="";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/JavaProjects/news_dev";
        } else {
            basePath = "/home/project/dev";
        }
        if (seperator == null || seperator.equals("/")) {
            return basePath;
        }

        return basePath.replace("/", seperator);
    }
    public static String getUserPath(String userId, String dir) {
        String userPath = "/" + userId + "/" + dir;
        if (seperator == null || seperator.equals("/")) {
            return userPath;
        }
        return userPath.replace("/", seperator);
    }
    public static String getPublicPath(String dir) {
        String userPath = "/" + dir;
        if (seperator == null || seperator.equals("/")) {
            return userPath;
        }
        return userPath.replace("/", seperator);
    }
}
