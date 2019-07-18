package com.fan.news.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileUtil {
    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random rand = new Random();

    // 缩略图
    /*public static String generateThumbnail(InputStream file, String originalFileName, String targetAddr) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(originalFileName);
        makeDir(targetAddr);

        String newFileAddr = targetAddr + fileName + extension;
        //logger.debug("Current File Path : " + newFileAddr);
        File destFile = new File(PathUtil.getImgBasePath() + newFileAddr);
        //logger.debug("Current Absolute File Path : " + PathUtil.getImgBasePath() + newFileAddr);

        try {
            Thumbnails.of(file).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "images/watermark.png")), 0.5f)
                    .outputQuality(0.8f).toFile(destFile);
        } catch (IOException io) {
            throw new RuntimeException("图片压缩失败！" + io.getMessage());
        }
        return newFileAddr;
    }*/

    public static boolean checkFileSize(Long len, long size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }

        /**
         * 删除工具
         * 如果path是目录，则删除目录下所有文件
         * 如果是文件，直接删除
         * @param path
         */
    public static void deleteFileOrPath(String path) {
        File f = new File(PathUtil.getBasePath() + path);
        if (f.exists()) {
            if (f.isDirectory()) {
                // 目录
                File[] files = f.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            f.delete();
        }
    }

    /**
     * 创建目标路径所涉及的目录
     * @param targetAddr
     */
    public static void makeDir(String targetAddr) {
        String realFilePath = PathUtil.getBasePath() + targetAddr;
        File dirPath = new File(realFilePath);
        if (!dirPath.exists()) {
            dirPath.mkdir();
        }
    }

    /**
     * 获取扩展名
     * @param originalFileName
     * @return
     */
    private static String getFileExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名  当前时间+随机5位数
     * @return
     */
    private static String getRandomFileName() {
        int num = rand.nextInt(99999) + 10000;  // 10000-99999
        return dateFormat.format(new Date()) + num;
    }

    /*public static void main(String[] args) throws IOException {
        System.out.println(basePath);
        Thumbnails.of(new File("G:\\ANIME\\good\\timg.jpg"))
                .size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"images/watermark.png")), 0.5f)
                .outputQuality(0.8f).toFile(new File(basePath + "images/newtimg.jpg"));
    }*/
}
