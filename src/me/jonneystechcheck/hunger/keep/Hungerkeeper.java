package me.jonneystechcheck.hunger.keep;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Hungerkeeper extends JavaPlugin implements Listener{
	
	Logger log = Logger.getLogger("Minecraft.Hungerkeeper");
	
	public void onEnable() {
		// register events in this class
		getServer().getPluginManager().registerEvents(this, this);
		// set the config
		this.getConfig().options().copyDefaults(true);
		this.getConfig().addDefault("Per-World-Hunger", true);
		int worldnumber = Bukkit.getWorlds().size();
	     for(int x = 0; x < worldnumber; x = x+1) {
	    	 this.getConfig().addDefault("Per-World-Hunger-in."+Bukkit.getWorlds().get(x).getName(), false);
	      }
		// save the config
		this.saveConfig();
		// print to the console that the plugin in enabled
		log.info("[HungerKeeper] has been Enabled!");
		Bukkit.getWorld("world").setAnimalSpawnLimit(2);
	}
	
	@EventHandler
	public void playerdie(PlayerDeathEvent ev){
		String name = ev.getEntity().getName();
		int food = ev.getEntity().getFoodLevel();
		this.getConfig().set(name+'.'+"master", food);
		this.getConfig().set(name+ '.'+ev.getEntity().getWorld().getName(),food);
		this.saveConfig();
	}
	
	@EventHandler
	public void playerrespawn(PlayerRespawnEvent ev){
		final Player p = ev.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
		{
		     public void run()
		          {
		    	 hungar(p);
		          }
		}, 5);
	}
	
	public void hungar(Player p){
		if(this.getConfig().getBoolean("Per-World-Hunger-in."+p.getWorld().getName()) == true && this.getConfig().getBoolean("Per-World-Hunger") == true){
			 int foodlevelmulti = this.getConfig().getInt(p.getName()+'.'+p.getWorld().getName());
			 		p.setFoodLevel(foodlevelmulti);
					Bukkit.broadcastMessage("normal");
		}else{
			//perworld disabled
			int foodmaster = this.getConfig().getInt(p.getName()+'.'+"master");
			p.setFoodLevel(foodmaster);
		}
	}
	
}
