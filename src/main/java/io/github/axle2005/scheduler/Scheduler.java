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
	Config config;
	SimpleDateFormat ft =  new SimpleDateFormat ("hh:mm");
	
	List<Long> list_counter =new ArrayList<Long>();
	
    @Override
    public void onEnable() {
    	config = new Config(this, "config.yml");
    	final String servertype = config.getStringValue("ServerType");
    	final long timeNow = System.currentTimeMillis();
    	
    	
    	
    	List<String> list_config = config.getStringList("ScheduledTasks");
    	
    	//Checks for "Replace Text" and removes it from the list, leaving only configured commands. 
    	if(list_config.contains("Replace Text"))
    	{
    		for(int i =0; i<=list_config.size()-1;)
    		{
    			if(list_config.get(i).equalsIgnoreCase("Replace Text"))
    			{
    				list_config.remove(i);
    			}
    			else
    			{
    				i++;
    			}
    			
    		}
    	}
    	
    	final String[][] tasks = new String[list_config.size()][4];
    	for(int i =0; i<=list_config.size()-1;i++)
		{
    		List<String> list_temp = Arrays.asList(list_config.get(i).split(","));
    		for(int j = 0; j <= list_temp.size()-1;j++)
    		{
    			tasks[i][j] = list_temp.get(j);
    			log.info("Tasks["+i+"]["+j+"]"+tasks[i][j]);
    		}
    		if(tasks[i][1] != null)
    		{
    			list_counter.add(addMinutes(timeNow,Integer.valueOf(tasks[i][1])));
    		}
    		else
    		{
    			tasks[i][1] = "10";
    			log.warning("[Scheduler] Error in config - "+tasks[i][0]+" has no interval, setting to 10");
    			list_counter.add(addMinutes(timeNow,10));
    		}
    			
    
    		
			
		}
    	
    	
    	
    	
    	
    	
    	
		new BukkitRunnable() {
			@Override
			public void run() {
				
				
				
				log.info("Current Date: " + ft.format(getSystemTime()));
				//log.info(convertToMinutes(getSystemTime())+"");
				//log.info("Current Date: " + ft.format(addMinutes(timeNow,10)));
				
				for(int i = 0; i<=list_counter.size()-1;i++)
				{
					if(convertToMinutes(getSystemTime()) == convertToMinutes(list_counter.get(i)))
					{
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), tasks[i][0]);
						list_counter.set(i, addMinutes(list_counter.get(i),Integer.valueOf(tasks[i][1])));
					}
					//log.info(convertToMinutes(list_counter.get(i))+"");
				}
				if(servertype.equalsIgnoreCase("spigot")){
					
				}
				
				
				
			}
		}.runTaskTimerAsynchronously(this, 0, (20 * 60 * 1)); // 20 Ticks ->
																// 60
																// Seconds
																// -> 1
																// Minute:
																// Time in
																// between
																// checks
	
    	
    }
    
    @Override
    public void onDisable() {
       
    }
    
    
    
    public long getSystemTime()
    {
    	long timeNow = System.currentTimeMillis();
    	return timeNow;
    }
    public long convertToMinutes(long time)
    {
    	long newTime = (time/1000)/60;
    	return newTime;
    }
    
    public long addMinutes(Long milli, int minutesadded)
    {
    	final long timeMinutes = ((milli/1000)/60) + minutesadded;
    	long minutestomilli = (timeMinutes*60)*1000;
    	return minutestomilli;
    }
	public void debug(String message) {
		if (config.getBooleanValue("Debug") == true) {
			log.info("Scheduler Debug: " + message);
		}
	}
}