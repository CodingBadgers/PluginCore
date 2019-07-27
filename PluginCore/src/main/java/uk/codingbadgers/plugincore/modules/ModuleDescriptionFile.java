package uk.codingbadgers.plugincore.modules;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.Reader;
import java.util.Collection;
import java.util.List;

public class ModuleDescriptionFile {

    private final String m_name;
    private final String m_version;
    private final String m_description;
    private final List<String> m_authors;
    private final String m_mainClass;
    private final Collection<String> m_dependencies;
    private final Material m_icon;

    public ModuleDescriptionFile(Reader data) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(data);

        m_name = config.getString("name", "Unknown");
        m_version = config.getString("version", "0.1-SNAPSHOT");
        m_description = config.getString("description", "");
        m_mainClass = config.getString("main", "");
        m_authors = config.getStringList("authors");
        m_dependencies = config.getStringList("dependencies");

        Material icon = Material.ENDER_EYE;
        try {
            icon = Material.valueOf(config.getString("icon"));
        }catch (Exception ex) {
            // TODO: log invalid icon name
        }
        m_icon = icon;
    }

    public String getName() {
        return m_name;
    }

    public String getVersion() {
        return m_version;
    }

    public String getDescription() {
        return m_description;
    }

    public String getMainClass() {
        return m_mainClass;
    }

    public List<String> getAuthors() {
        return m_authors;
    }

    public Collection<String> getDependencies() {
        return m_dependencies;
    }

    public Material getIcon() { return m_icon; }
}
