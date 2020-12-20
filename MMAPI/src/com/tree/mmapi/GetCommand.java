package com.tree.mmapi;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetCommand implements CommandExecutor {

	private Main main;
	
	public GetCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		System.out.println("Args length =" + args.length);
		if(args.length == 0) {
			player.sendMessage(ChatColor.RED + "Must provide a nickname");
		}
		if(args.length == 1) {
			String name = args[0];
			try {
				UUID id = main.GetUUIDFromNickname(name);
				player.sendMessage(ChatColor.AQUA + "Their name is: "+main.GetMCName(Bukkit.getPlayer(id)));
			}catch(Exception e) {
				player.sendMessage(ChatColor.RED +"Error getting name");
			}
			
		}
		
		return false;
	}

}
