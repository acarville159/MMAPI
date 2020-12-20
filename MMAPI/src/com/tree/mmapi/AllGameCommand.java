package com.tree.mmapi;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AllGameCommand implements CommandExecutor {
	
	Main main;
	
	public AllGameCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission("mmapi.admin")) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p != null) {
					main.sendToServer(p, "game");
				}
			}
				
		}

		return false;
	}

}
