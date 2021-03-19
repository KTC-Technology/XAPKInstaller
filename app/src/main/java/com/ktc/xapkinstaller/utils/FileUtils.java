package com.ktc.xapkinstaller.utils;

import java.io.File;

/**
 * @author: ArvinYoung
 * @date: 2021/3/19
 */
public class FileUtils {

    public static boolean createOrExistsDir(String dirPath){
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(File file) {
        if(file != null && (file.exists()) && file.isDirectory()){
                return true;
        } else {
            return file.mkdirs();
        }
    }

    public static File getFileByPath(String filePath){
        if (isSpace(filePath)){
            return null;
        } else {
            return new File(filePath);
        }
    }

    public static String getFileName(File file){
        if (file == null) {
            return "";
        } else {
            return getFileName(file.getAbsolutePath());
        }
    }

    public static String getFileName(String filePath){
        if (isSpace(filePath)) return "";
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return filePath;
        } else {
            return filePath.substring(lastSep + 1);
        }
    }

    public static String getFileNameNoExtension(File file){
        if (file == null) {
            return "";
        } else {
            return getFileNameNoExtension(file.getPath());
        }
    }

    public static String  getFileNameNoExtension(String filePath){
        if (isSpace(filePath)) return "";
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            if (lastPoi == -1){
                return filePath;
            } else {
                return filePath.substring(0, lastPoi);
            }
        }

        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        } else {
            return filePath.substring(lastSep + 1, lastPoi);
        }
    }

    private static boolean isSpace(String s) {
        if (s == null) return true;
        int i = 0;
        int len = s.length();
        while (i < len) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
            ++i;
        }
        return true;
    }
}
