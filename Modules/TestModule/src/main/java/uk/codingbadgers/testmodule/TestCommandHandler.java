package uk.codingbadgers.testmodule;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.plugincore.modules.Module;
import uk.codingbadgers.plugincore.modules.commands.ModuleChildCommand;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

public class TestCommandHandler extends ModuleCommand {
    protected TestCommandHandler(Module module) {
        super(module, "test");

        registerChildCommand(new ModuleChildCommand(module, "child") {
            @Override
            public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
                sendMessage(sender,"Test Child Command");
                return true;
            }
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.sendMessage(sender, "Test command");
        return true;
    }
}
