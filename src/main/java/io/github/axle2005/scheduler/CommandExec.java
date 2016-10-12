package io.github.axle2005.scheduler;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExec implements CommandExecutor {
	private Scheduler plugin;
	Config config;
	Logger log = Logger.getLogger("Minecraft");

	CommandExec(Scheduler plugin) {
		this.plugin = plugin;
	}

	CommandExec(Scheduler plugin, Config config) {
		this.plugin = plugin;
		this.config = config;
	}

	// For some stupid reason it wont @Override - Come back to this.
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub

		if (cmd.getName().equalsIgnoreCase("scheduler")) {
			if (args[0] != null) {
				if (args[0].equalsIgnoreCase("save")) {
					config.set("Counter", plugin.list_counter);
					config.save();
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) {
					config.reload();
					return true;
				}
				else
				{
					return false;
				}
					
			}
		}

			return false;
	
			

	}

}
