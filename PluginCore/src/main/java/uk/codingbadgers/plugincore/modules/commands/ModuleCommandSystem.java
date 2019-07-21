package uk.codingbadgers.plugincore.modules.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import uk.codingbadgers.plugincore.PluginCore;
import uk.codingbadgers.plugincore.modules.Module;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class ModuleCommandSystem {

    private static final String c_commandMapFieldName = "commandMap";
    private static final String c_commandFieldName = "knownCommands";

    private final PluginCore m_plugin;
    private Map<Module, List<ModuleCommand>> m_commands = new HashMap<>();
    private CommandMap m_commandMap;
    private Field m_knownCommandsField;

    public ModuleCommandSystem(PluginCore plugin) {
        m_plugin = plugin;

        setupCommandMap();
    }

    private void setupCommandMap() {
        try {
            PluginManager pluginManager = Bukkit.getServer().getPluginManager();
            Class<? extends PluginManager> clazz = pluginManager.getClass();
            Field field = clazz.getDeclaredField(c_commandMapFieldName);
            field.setAccessible(true);
            m_commandMap = (CommandMap) field.get(pluginManager);
        } catch(Exception e) {
            m_plugin.getLogger().log(Level.SEVERE, "Error setting up command handler", e);
            m_commandMap = null;
        }

        if (m_commandMap instanceof SimpleCommandMap) {
            try {
                m_knownCommandsField = SimpleCommandMap.class.getDeclaredField(c_commandFieldName);
                m_knownCommandsField.setAccessible(true);
            } catch(Exception e) {
                m_plugin.getLogger().log(Level.SEVERE, "Error setting up command handler", e);
            }
        } else {
            m_plugin.getLogger().log(Level.SEVERE, "Unknown command map type, cannot deregister commands");
        }
    }

    public void registerCommand(Module module, ModuleCommand command) {
        m_commandMap.register(module.getDescription().getName(), command);

        if (m_commands.containsKey(module)) {
            m_commands.get(module).add(command);
        } else {
            m_commands.put(module, new ArrayList<>(Collections.singletonList(command)));
        }
    }

    @SuppressWarnings("unchecked")
    public void deregisterCommands(Module module) {
        if (!m_commands.containsKey(module)) {
            return;
        }

        List<ModuleCommand> commands = getCommands(module);

        for (ModuleCommand c : commands) {
            try {
                Map<String, Command> knownCommands = (Map<String, Command>) m_knownCommandsField.get(m_commandMap);
                Command command = m_commandMap.getCommand(c.getLabel());

                if (command == null) {
                    m_plugin.getLogger().log(Level.SEVERE, "Could not find command '" + c.getLabel() + "' in command map");
                    return;
                }

                command.unregister(m_commandMap);
                knownCommands.remove(command.getLabel().toLowerCase());
            } catch (Exception e) {
                m_plugin.getLogger().log(Level.SEVERE, "Error deregistering commands for module '" + module.getDescription().getName() + "'", e);
            }
        }

        m_commands.remove(module);
    }

    public List<ModuleCommand> getCommands(Module module) {
        if (!m_commands.containsKey(module)) {
            return ImmutableList.of();
        }

        return ImmutableList.copyOf(m_commands.get(module));
    }
}
