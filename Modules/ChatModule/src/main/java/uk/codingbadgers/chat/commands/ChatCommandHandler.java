package uk.codingbadgers.chat.commands;

import com.google.common.collect.ImmutableList;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

public class ChatCommandHandler extends ModuleCommand {
    public ChatCommandHandler(ChatModule module) {
        super(module, "chat", "Chat module core command", "/ch help", ImmutableList.of("ch"));

        this.registerChildCommand(new ChannelCommandHandler(module));
    }
}
