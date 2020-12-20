package com.tree.mmapi;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MinecraftMondaysCommand implements CommandExecutor {
	
	private Main main;
	
	public MinecraftMondaysCommand(Main main) {
		// TODO Auto-generated constructor stub
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,String label,String[] args) {
		Player player = (Player)sender;
		System.out.println("Args length =" + args.length);
		if(args.length == 0) {
			//no args so just show current score
			player.sendMessage(ChatColor.GREEN + "Your current score is: " + main.GetPlayerScore(player));
			List<UUID> allPlayers = main.GetPlayersInOrder();
			int i = 1;
			for(UUID id : allPlayers) {
				if(player != null) {
					try {
						player.sendMessage(i + "." + main.GetNickname(id) + ChatColor.GREEN +" " + ChatColor.YELLOW + main.GetPlayerScore(id));
						i++;	
					}catch(Exception e){
						
					}

				}	
			}
		}else {
			//we have an actual command
			if(player.hasPermission("mmapi.admin")) {
				
				if(args[0].equalsIgnoreCase("score")){
					try {
						if(args[1].equalsIgnoreCase("set")) {
							//player is trying to set the score of another player
							Player target = null;
							int amt = 0;
							try {
								target = Bukkit.getPlayer(args[2]);
							}catch(Exception e){
								player.sendMessage("Invalid Player");
								return false;
							}
							try {
								amt = Integer.parseInt(args[3]);
							}catch(Exception e) {
								player.sendMessage("Invalid score amount");
								return false;
							}
							
							main.SetPlayerScore(target, amt);
							
							return false;
						}
						if(args[1].equalsIgnoreCase("add")) {
							//player is trying to add score to another player
							Player target = null;
							int amt = 0;
							try {
								target = Bukkit.getPlayer(args[2]);
							}catch(Exception e){
								player.sendMessage("Invalid Player");
								return false;
							}
							try {
								amt = Integer.parseInt(args[3]);
							}catch(Exception e) {
								player.sendMessage("Invalid score amount");
								return false;
							}
							
							main.AddScoreToPlayer(target,"", amt);
							
							return false;
						}
						if(args[1].equalsIgnoreCase("minus")) {
							//player is trying to remove score from another player
							Player target = null;
							int amt = 0;
							try {
								target = Bukkit.getPlayer(args[2]);
							}catch(Exception e){
								player.sendMessage("Invalid Player");
								return false;
							}
							try {
								amt = Integer.parseInt(args[3]);
							}catch(Exception e) {
								player.sendMessage("Invalid score amount");
								return false;
							}
							
							main.MinusScoreToPlayer(target, amt);
							
							return false;
							
						}
					
					}catch(Exception e){
						player.sendMessage("Error With Score");
					}

				}
				if(args[0].equalsIgnoreCase("start")) {
					//start the current tourn
					//reset all scores to 0
					main.MMStart();
					
				}
				if(args[0].equalsIgnoreCase("stop")) {
					//stop the current tourn
					main.MMStop();
				}
				if(args[0].equalsIgnoreCase("pause")) {
					//stop the current tourn
					main.MMPause();
				}
				if(args[0].equalsIgnoreCase("resume")) {
					//stop the current tourn
					main.MMResume();
				}
				
				//this player is allowed to do commands
				
			}

		}
		return false;
	}

}
