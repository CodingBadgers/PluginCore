package uk.codingbadgers.testmodule;

import uk.codingbadgers.plugincore.modules.Module;

import java.util.logging.Level;

public class TestModule extends Module {
    @Override
    public void onEnable() {
        this.registerCommand(new TestCommandHandler(this));

        getLogger().log(Level.INFO, "Enabled " + getDescription().getName());
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled " + getDescription().getName());
    }
}
