package uk.codingbadgers.plugincore.utilities;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter {

    private final String m_extension;

    public FileExtensionFilter(String ext) {
        m_extension = "." + ext;
    }

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(m_extension);
    }
}
