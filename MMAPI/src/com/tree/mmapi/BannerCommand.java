package com.tree.mmapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import io.netty.handler.codec.AsciiHeadersEncoder.NewlineType;

public class BannerCommand implements CommandExecutor {

private Main main;
	
	public BannerCommand(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		try {
			Player player = (Player)sender;
			System.out.println("Args length =" + args.length);
			if(args.length == 0) {
				//no args so just show current score
			}else {
				if(args.length == 1) {
					System.out.println("arg1");
					if(args[0].equalsIgnoreCase("save")) {
						System.out.println("argS");

						//try save the current banner
						ItemStack is = player.getInventory().getItemInMainHand();
						Material bantype = is.getType();
						if(bantype == Material.WHITE_BANNER) {
							BannerMeta meta = (BannerMeta)is.getItemMeta();
							List<Pattern> patterns = meta.getPatterns();
							String data = "";
							data += bantype.name();
							for(Pattern p : patterns) {
								PatternType type = p.getPattern();
								DyeColor color = p.getColor();
								data += ";"+type.toString() + "-" + color.toString(); 
							}
							main.SetPlayerBannerData(player,data);
						}else {
							player.sendMessage(ChatColor.RED + "You must be holding a white banner to do this command!");
						}
					}else {
						if(args[0].equalsIgnoreCase("load")) {
							if(player.hasPermission("mmapi.admin") != true) {
								player.sendMessage(ChatColor.RED + "No Permission");
							}
							System.out.println("argL");
							ItemStack is = new ItemStack(Material.WHITE_BANNER);
							BannerMeta meta = (BannerMeta)is.getItemMeta();
							meta.setPatterns(main.GetBannerPatternForPlayer(player));
							is.setItemMeta(meta);
							player.getInventory().addItem(is);
						}else {
							System.out.println("Unknown arg");
						}
					}
				}
			}
		}catch (Exception e) {
			
		}
		
		return false;
	}
	

}
