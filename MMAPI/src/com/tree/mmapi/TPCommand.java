package com.tree.mmapi;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class TPCommand implements CommandExecutor {
	
	private Main main;
	
	public TPCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		if(player.hasPermission("mmapi.admin") != true) {
			player.sendMessage(ChatColor.RED + "No Permission");
		}
		System.out.println("Args length =" + args.length);
		if(args.length == 0) {
			player.openInventory(onlinePlayersSkull());
		}
		if(args.length == 1) {
			//the sender wishes to tp to the player they have typed
			String name = args[0];
			UUID target = main.GetUUIDFromNickname(name);
			if(target != null) {
				Player targetPlayer = Bukkit.getPlayer(target);
				if(targetPlayer != null) {
					player.teleport(targetPlayer.getLocation());
					player.sendMessage(ChatColor.AQUA + " You have been teleported to : " + targetPlayer.getName());
				}else {
					player.sendMessage(ChatColor.RED + "No Player");
					return false;
				}
			}else {
				player.sendMessage(ChatColor.RED + "No ID");
				return false;
			}
		}
		if(args.length == 2) {
			//the sender wishes to tp to the player they have typed
			String name = args[0];
			String name2 = args[1];
			UUID target = main.GetUUIDFromNickname(name);
			UUID target2 = main.GetUUIDFromNickname(name2);
			Player targetPlayer = null;
			Player targetPlayer2 = null;
			
			if(target != null) {
				targetPlayer = Bukkit.getPlayer(target);
				if(targetPlayer != null) {
				}else {
					player.sendMessage(ChatColor.RED + "No Player");
					return false;
				}
			}else {
				player.sendMessage(ChatColor.RED + "No ID");
				return false;
			}
			
			if(target2 != null) {
				targetPlayer2 = Bukkit.getPlayer(target2);
				if(targetPlayer2 != null) {
					//
				}else {
					player.sendMessage(ChatColor.RED + "No Player");
					return false;
				}
			}else {
				player.sendMessage(ChatColor.RED + "No ID");
				return false;
			}
			
			if(targetPlayer != null && targetPlayer2 != null) {
				targetPlayer.teleport(targetPlayer2.getLocation());
				player.sendMessage(targetPlayer.getName() + ChatColor.AQUA + " has been teleported to: " + targetPlayer2.getName());
				targetPlayer.sendMessage(ChatColor.AQUA + "You have been teleported to: " + targetPlayer2.getName());
			}else {
				player.sendMessage(ChatColor.RED + "Error doing tp");
			}
		}
		
		
		return false;
	}
	
	
	private Inventory onlinePlayersSkull() {
        Inventory inv = Bukkit.createInventory(null, 54, "Teleport");
        int time = 0;
        for(Player p: Bukkit.getOnlinePlayers()) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            System.out.println("Getting Head for: "+p.getCustomName());
            meta.setOwner(main.GetMCName(p));
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + p.getName());
            meta.setLocalizedName(p.getUniqueId().toString());
            skull.setItemMeta(meta);
            inv.setItem(time, skull);
            time++;
        }
        return inv;
    }

}
