package com.tree.mmapi;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gmCommand implements CommandExecutor {
	
	private Main main;
	
	public gmCommand(Main main) {
		this.main = main;
	}
	
	public GameMode getGameMode(int i) {
		switch (i) {
		case 0:
			return GameMode.SURVIVAL;
		case 1:
			return GameMode.CREATIVE;
		case 2:
			return GameMode.ADVENTURE;
		case 3:
			return GameMode.SPECTATOR;
		default:
			return GameMode.SURVIVAL;
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission("mmapi.admin") != true) {
			player.sendMessage(ChatColor.RED + "No Permission");
		}
		System.out.println("Args length =" + args.length);
		if(args.length == 0) {
			//no args so just show current score
			player.sendMessage(ChatColor.RED + "Provide a Gamemode");
			return false;
		}
		if(args.length == 1) {
			//set players own gamemode
			try {
				int gm = Integer.parseInt(args[0]);
				player.setGameMode(getGameMode(gm));
				player.sendMessage(ChatColor.AQUA + "Gamemode set to: "+ChatColor.YELLOW+gm);
			}catch(Exception e) {
				player.sendMessage(ChatColor.RED + "Invaid Gamemode");
			}
		}
		if(args.length == 2) {
			//set another players gamemode
			String name = args[0];
			UUID target = main.GetUUIDFromNickname(name);
			if(target != null) {
				Player targetPlayer = Bukkit.getPlayer(target);
				if(targetPlayer != null) {
					try {
						int gm = Integer.parseInt(args[0]);
						targetPlayer.setGameMode(getGameMode(gm));
						targetPlayer.sendMessage(ChatColor.AQUA + "Gamemode set to:"+ChatColor.YELLOW+gm);
						player.sendMessage(targetPlayer.getName()+ChatColor.AQUA + " gamemode set to: "+ChatColor.YELLOW+gm);

					}catch(Exception e) {
						player.sendMessage(ChatColor.RED + "Invaid Gamemode");
					}
				}
			}

		}
		
		return false;
	}

}
