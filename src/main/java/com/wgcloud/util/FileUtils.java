/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static void getFileList(String filePath, List<String> filePathList) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; ++i) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    FileUtils.getFileList(files[i].getAbsolutePath(), filePathList);
                    continue;
                }
                String strFileName = files[i].getAbsolutePath();
                filePathList.add(files[i].getAbsolutePath());
            }
        }
    }

    public static void existsFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public static boolean existsFile(String filePath) {
        File newFile = new File(filePath);
        return newFile.exists();
    }

    public static List<String> getFileList(String filePath) {
        ArrayList<String> resultList = new ArrayList<String>();
        File dir = new File(filePath);
        if (!dir.exists()) {
            return resultList;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; ++i) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) continue;
                resultList.add(fileName);
            }
        }
        return resultList;
    }
}

