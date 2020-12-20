package com.tree.mmapi;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
	
	private Main main;
	
	public LobbyCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		main.sendToServer(player, "lobby");
		return false;
	}

}
