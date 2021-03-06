package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.EventsHandler;
import net.minecraft.command.ICommandSender;

public class HFCommandNewDay extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "newDay";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        EventsHandler.newDay(true);
        return true;
    }
}
