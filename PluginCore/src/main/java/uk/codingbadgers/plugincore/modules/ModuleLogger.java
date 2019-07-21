package uk.codingbadgers.plugincore.modules;

import uk.codingbadgers.plugincore.PluginCore;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ModuleLogger extends Logger {

    private static final int c_debugValue = Level.ALL.intValue();
    private static final int c_normalValue = Level.INFO.intValue();

    private Module m_module;
    private String m_prefix;

    protected ModuleLogger(PluginCore plugin, Module module) {
        super(module.getDescription().getName(), null);

        m_module = module;
        m_prefix = "[" + plugin.getDescription().getName() + "] [" + module.getDescription().getName() + "] ";

        setParent(plugin.getLogger());
    }

    @Override
    public boolean isLoggable(Level level) {
        return level.intValue() >= (m_module.isDebug() ? c_debugValue : c_normalValue);
    }

    @Override
    public void log(LogRecord record) {
        if (!this.isLoggable(record.getLevel()))
            return;

        record.setMessage(m_prefix + record.getMessage());
        super.log(record);
    }
}
