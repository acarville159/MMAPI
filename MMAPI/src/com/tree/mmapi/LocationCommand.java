package com.tree.mmapi;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocationCommand implements CommandExecutor {

private Main main;
	
	public LocationCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission("mmapi.admin")) {
				//print the location of the player to console
				Location loc = player.getLocation();
				String outString = "new Location(world,"+loc.getBlockX()+","+loc.getBlockY()+","+loc.getBlockZ()+","+loc.getYaw()+","+loc.getPitch()+")";
				System.out.println(outString);
				player.sendMessage("Printed Location to Server Console");
		}

		return false;
	}

}
