package uk.codingbadgers.plugincore.player;

import java.io.File;
import java.io.IOException;

public interface CorePlayerData {
    /**
     *
     * @param dataFile
     *
     * @throws IOException
     */
    boolean save(File dataFile) throws IOException;

    /**
     *
     * @param dataFile
     *
     * @throws IOException
     */
    boolean load(File dataFile) throws IOException;
}
