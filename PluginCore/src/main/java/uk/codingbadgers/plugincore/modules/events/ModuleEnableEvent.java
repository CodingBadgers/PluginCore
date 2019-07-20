package uk.codingbadgers.plugincore.modules.events;

import org.bukkit.event.HandlerList;
import uk.codingbadgers.plugincore.modules.Module;

public class ModuleEnableEvent extends ModuleEvent {

    private static final HandlerList handlers = new HandlerList();

    public ModuleEnableEvent(Module module) {
        super(module);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
