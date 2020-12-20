package com.tree.mmapi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener {
	
	private Main main;
	
	public PlayerListener(Main main) {
		this.main = main;
		
	}
	
	@EventHandler
	public void onCraftItem(CraftItemEvent  e) {
		ItemStack is = e.getRecipe().getResult();
		CraftingInventory inv = e.getInventory();
		Player player = (Player) e.getWhoClicked();
		if(is.getType() == Material.SHIELD) {
			//replace the shield with the players custom shield
			ItemStack is2 = new ItemStack(Material.SHIELD);
			ItemMeta meta = is.getItemMeta();
			BlockStateMeta bmeta = (BlockStateMeta) meta;
			Banner banner = (Banner) bmeta.getBlockState();
			List<Pattern> panters = main.GetBannerPatternForPlayer(player);
			banner.setPatterns(panters);
			banner.update();
			bmeta.setBlockState(banner);
			is.setItemMeta(bmeta);
			//e.setCancelled(true);
			//ItemStack is3 = new ItemStack(Material.WHITE_BANNER);
			//BannerMeta meta2 = (BannerMeta)is.getItemMeta();
			//meta.setPatterns(main.GetBannerPatternForPlayer(player));
			is2.setItemMeta(meta);
			inv.setResult(is2);
		

		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		//System.out.println("InventoryClickEvent " + e.getRawSlot());
		Player player = (Player)e.getWhoClicked();
		if(e.getCurrentItem() != null && e.getView().getTitle().contains("Teleport") && e.getRawSlot() < 54) {
			//player.sendMessage("you just clicked on: " + e.getCurrentItem().getItemMeta().getLocalizedName());
			
			try {
				UUID targetID =UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName());
				Player target = Bukkit.getPlayer(targetID);
				player.teleport(target.getLocation());
				player.sendMessage(ChatColor.AQUA + "You have been teleported to: " + target.getName());
			}catch(Exception ex) {
				player.sendMessage(ChatColor.RED + "Cannot find player");
			}
			
			e.setCancelled(true);
			player.closeInventory();
		}
		
		if(e.getCurrentItem() != null && e.getView().getTitle().contains("Bring") && e.getRawSlot() < 54) {
			//player.sendMessage("you just clicked on: " + e.getCurrentItem().getItemMeta().getLocalizedName());
			
			try {
				UUID targetID =UUID.fromString(e.getCurrentItem().getItemMeta().getLocalizedName());
				Player target = Bukkit.getPlayer(targetID);
				target.teleport(player.getLocation());
				target.sendMessage(ChatColor.AQUA + "You have been teleported to: " + player.getName());
			}catch(Exception ex) {
				player.sendMessage(ChatColor.RED + "Cannot find player");
			}
			
			e.setCancelled(true);
			player.closeInventory();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		player.setCustomName(player.getName());
		main.SetPlayerSkinFromSaved(player);
		System.out.println("A Player Joined The Server Their Real Name is: " + player.getName() + "-" + player.getDisplayName());
		PreparedStatement ps;
		//check to see if the player exists in the scores table already
		try {
			ResultSet rs = Main.prepareStatement("SELECT COUNT(UUID) FROM player_scores WHERE UUID = '"+player.getUniqueId().toString() +"';").executeQuery();
			rs.next();
			
			if(rs.getInt(1) == 0) {
				//is not in the system
				System.out.println(player.getName() + " is not in the system. Adding them now.");
				Main.prepareStatement("INSERT INTO player_scores(UUID, SCORE) VALUES ('"+player.getUniqueId() +"',"
						+ "DEFAULT);").executeUpdate();
			}else { //is already in the system
				ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_scores WHERE UUID = '"+player.getUniqueId()+"';").executeQuery();
				rs2.next();
				
				
				int score = rs2.getInt("SCORE");
				System.out.println(player.getName() + " has a score of: " + score);
			}
		}catch(SQLException x) {
			x.printStackTrace();
		}
		main.changeName(main.GetNickname(player), player);
		e.setJoinMessage( main.GetNickname(player) + " joined the server");
	}

}
