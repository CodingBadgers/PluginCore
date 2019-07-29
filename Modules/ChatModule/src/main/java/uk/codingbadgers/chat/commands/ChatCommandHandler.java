package uk.codingbadgers.chat.commands;

import com.google.common.collect.ImmutableList;
import uk.codingbadgers.chat.ChatModule;
import uk.codingbadgers.chat.commands.chat.ChannelCommandHandler;
import uk.codingbadgers.chat.commands.chat.JoinCommandHandler;
import uk.codingbadgers.chat.commands.chat.LeaveCommandHandler;
import uk.codingbadgers.chat.commands.chat.UserCommandHandler;
import uk.codingbadgers.plugincore.modules.commands.ModuleCommand;

public class ChatCommandHandler extends ModuleCommand {
    public ChatCommandHandler(ChatModule module) {
        super(module, "chat", "Chat module core command", "/ch help", ImmutableList.of("ch"));

        this.registerChildCommand(new ChannelCommandHandler(module));
        this.registerChildCommand(new UserCommandHandler(module));
        this.registerChildCommand(new JoinCommandHandler(module));
        this.registerChildCommand(new LeaveCommandHandler(module));
    }
}
