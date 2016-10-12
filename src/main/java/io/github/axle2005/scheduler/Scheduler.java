package io.github.axle2005.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Scheduler extends JavaPlugin {
	Logger log = Logger.getLogger("Minecraft");
	Config config = new Config(this, "config.yml");
	SimpleDateFormat ft = new SimpleDateFormat("hh:mm");

	int z = 0;

	List<Long> list_counter = new ArrayList<Long>();
	List<String> list_tmpcounter = new ArrayList<String>();

	@Override
	public void onEnable() {
		CommandExec exec = new CommandExec(this, config);
		this.getCommand("scheduler").setExecutor(new CommandExec(this, config));

		final String servertype = config.getStringValue("ServerType");

		List<String> list_config = config.getStringList("ScheduledTasks");
		list_tmpcounter = config.getStringList("Counter");
		for (int i = 0; i <= list_tmpcounter.size() - 1; i++) {
			list_counter.add(Long.valueOf(list_tmpcounter.get(i)));
		}

		// Checks for "Replace Text" and removes it from the list, leaving only
		// configured commands.
		if (list_config.contains("Replace Text")) {
			for (int i = 0; i <= list_config.size() - 1;) {
				if (list_config.get(i).equalsIgnoreCase("Replace Text")) {
					list_config.remove(i);
				} else {
					i++;
				}

			}
		}

		final String[][] tasks = new String[list_config.size()][4];
		for (int i = 0; i <= list_config.size() - 1; i++) {

			List<String> list_temp = Arrays.asList(list_config.get(i).split(","));
			for (int j = 0; j <= list_temp.size() - 1; j++) {
				tasks[i][j] = list_temp.get(j);
			}
			if (list_counter.size() < 1) {

				if (tasks[i][1] != null) {
					list_counter.add(addMinutes(getSystemTime(), Integer.valueOf(tasks[i][1])));
				} else {
					tasks[i][1] = "10";
					log.warning("[Scheduler] Error in config - " + tasks[i][0] + " has no interval, setting to 10");
					list_counter.add(addMinutes(getSystemTime(), 10));
				}
			}

		}

		if (servertype.equalsIgnoreCase("spigot")) {

			new BukkitRunnable() {
				@Override
				public void run() {

					for (int i = 0; i <= list_counter.size() - 1; i++) {
						if (convertToMinutes(getSystemTime()) >= convertToMinutes(list_counter.get(i))) {
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), tasks[i][0]);
							list_counter.set(i, addMinutes(getSystemTime(), Integer.valueOf(tasks[i][1])));
							log.info("[Scheduler] Ran Command " + tasks[i][0]);
						}
						// log.info(convertToMinutes(list_counter.get(i))+"");
					}

					if (z == 10) {
						List<String> counter = new ArrayList<String>();
						for (int i = 0; i <= list_counter.size() - 1; i++) {
							counter.add(list_counter.get(i) + "");
						}
						config.getConfig().set("Counter", counter);
						config.save();
						z = 0;
					}
					z++;

				}
			}.runTaskTimerAsynchronously(this, 0, (20 * 60 * 1)); // 20 Ticks ->
																	// 60
																	// Seconds
																	// -> 1
																	// Minute:
																	// Time in
																	// between
																	// checks

		} else if (servertype.equalsIgnoreCase("sponge")) {

		}
	}

	@Override
	public void onDisable() {
		List<String> counter = new ArrayList<String>();
		for (int i = 0; i <= list_counter.size() - 1; i++) {
			counter.add(list_counter.get(i) + "");
		}
		config.getConfig().set("Counter", counter);
		config.save();

	}

	public long getSystemTime() {
		long timeNow = System.currentTimeMillis();
		return timeNow;
	}

	public long convertToMinutes(long time) {
		long newTime = (time / 1000) / 60;
		return newTime;
	}

	public long convertToMinutes(String time) {
		int t = Integer.valueOf(time);
		long newTime = (t / 1000) / 60;
		return newTime;
	}

	public long addMinutes(Long milli, int minutesadded) {
		final long timeMinutes = ((milli / 1000) / 60) + minutesadded;
		long minutestomilli = (timeMinutes * 60) * 1000;
		return minutestomilli;
	}

	public void debug(String message) {
		if (config.getBooleanValue("Debug") == true) {
			log.info("Scheduler Debug: " + message);

		}
	}
}
