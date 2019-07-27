package uk.codingbadgers.plugincore.modules.events;

import org.bukkit.event.HandlerList;
import uk.codingbadgers.plugincore.modules.Module;

public class ModuleDisableEvent extends ModuleEvent {

    private static final HandlerList handlers = new HandlerList();

    public ModuleDisableEvent(Module module) {
        super(module);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
