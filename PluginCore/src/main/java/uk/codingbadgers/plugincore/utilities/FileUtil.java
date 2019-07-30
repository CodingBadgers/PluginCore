package uk.codingbadgers.plugincore.utilities;

import java.io.File;

public class FileUtil {
    public static String getNameWithoutExtension(File file) {
        String name = file.getName();
        return name.replaceFirst("[.][^.]+$", "");
    }
}
