package com.ice.util;

import java.io.File;

public class FileUtil {
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {// 判断目录是否存在
            dir.mkdirs();//多层目录需要调用mkdirs
        }
    }
}
