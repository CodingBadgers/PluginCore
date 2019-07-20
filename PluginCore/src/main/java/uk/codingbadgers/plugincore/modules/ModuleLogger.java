package uk.codingbadgers.plugincore.modules;

import uk.codingbadgers.plugincore.PluginCore;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ModuleLogger extends Logger {

    private String m_prefix;

    protected ModuleLogger(PluginCore plugin, Module module) {
        super(module.getDescription().getName(), null);

        m_prefix = "[" + plugin.getDescription().getName() + "] [" + module.getDescription().getName() + "] ";
        setParent(plugin.getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage(m_prefix + record.getMessage());
        super.log(record);
    }
}
