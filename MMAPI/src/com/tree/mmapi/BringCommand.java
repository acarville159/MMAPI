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

public class BringCommand implements CommandExecutor {

	private Main main;
	
	public BringCommand(Main main) {
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
					targetPlayer.teleport(player.getLocation());
					targetPlayer.sendMessage(ChatColor.AQUA + "You have been teleported to: " +player.getName());
				}else {
					player.sendMessage(ChatColor.RED + "No Player");
				}
			}else {
				player.sendMessage(ChatColor.RED + "No ID");
			}
		}
		
		
		return false;
	}
	
	
	private Inventory onlinePlayersSkull() {
        Inventory inv = Bukkit.createInventory(null, 54, "Bring");
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
