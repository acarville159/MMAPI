package com.tree.mmapi;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PMCommand implements CommandExecutor {

	private Main main;
	
	public PMCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;

		if(args.length == 1) {
			player.sendMessage(ChatColor.RED + "Incorrect usage of command");
		}
		if(args.length > 1) {
			// 0 = player 1 = message
			String name = args[0];
			UUID target = main.GetUUIDFromNickname(name);
			String msg = "";
			for(String arg : args) {
				msg += (" " + arg);
			}
			if(target != null) {
				Player targetPlayer = Bukkit.getPlayer(target);
				if(targetPlayer != null) {
					targetPlayer.sendMessage(player.getName() + ChatColor.GRAY + "whispers "+ChatColor.WHITE + msg);
				}else {
					player.sendMessage(ChatColor.RED + "No Player With This Name");
					return false;
				}
			}else {
				player.sendMessage(ChatColor.RED + "No ID");
				return false;
			}
		}
		
		return false;
	}

}
