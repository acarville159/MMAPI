package com.tree.mmapi;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkinCommand implements CommandExecutor{
	
	private Main main;
	
	public SkinCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission("mmapi.admin")) {
				if(args.length == 1) {
					String skinName = args[0];
					if(skinName.equals("reload")){
						main.LoadAllCustomSkins();
					}else {
						main.SetPlayerSkin(player, skinName);
					}
					
				}else {
					player.sendMessage("Only 1 arg is allowed for this command you cunt");
				}
		}

		return false;
	}
}
