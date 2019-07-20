package uk.codingbadgers.plugincore.modules.commands;

import uk.codingbadgers.plugincore.modules.Module;

import java.util.List;

public abstract class ModuleChildCommand extends ModuleCommand {
    protected ModuleChildCommand(Module module, String name) {
        super(module, name);
    }

    public ModuleChildCommand(Module module, String name, String description, String usageMessage, List<String> aliases) {
        super(module, name, description, usageMessage, aliases);
    }
}
