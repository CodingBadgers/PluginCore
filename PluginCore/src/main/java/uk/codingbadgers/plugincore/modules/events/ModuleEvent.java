package uk.codingbadgers.plugincore.modules.events;

import org.bukkit.event.Event;
import uk.codingbadgers.plugincore.modules.Module;

public abstract class ModuleEvent extends Event {

    private Module m_module;

    public ModuleEvent(Module module) {
        m_module = module;
    }

    public Module getModule() {
        return m_module;
    }
}
