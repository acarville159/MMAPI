package com.tree.mmapi;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.ByteArrayOutputStream;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.server.v1_16_R3.EntityPlayer;

public class Main extends JavaPlugin {

	private static Connection connection;
	private String host, database, username, password;
	private int port;

	private ArrayList<CustomSkin> customSkins = new ArrayList<CustomSkin>();

	@Override
	public void onEnable() {

		// customSkins.add(new CustomSkin("red_santa",
		// "ewogICJ0aW1lc3RhbXAiIDogMTYwNjgzOTg5Nzg1MywKICAicHJvZmlsZUlkIiA6ICI5ZDIyZGRhOTVmZGI0MjFmOGZhNjAzNTI1YThkZmE4ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTYWZlRHJpZnQ0OCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNjYyY2UxNDhiZDMyM2RjMDVhZjk2ODVlYTg3MjVkYTM2YmYwOGZmNWYxNGZmNmRjZmQ2M2EyNTg3MzI5ZTcxIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
		// "V8x30QRRgfMeK/0ULwLVI5TzahVz1nPXLS0s8niwqSs3q5O3jix5URddgJdvYThzY/CtVZ5Npilq2Gli9JxfnPhv+DN3CbwaWOfTVHTiqkfJHi7gKPOnJwXjfOc7ICDA7naPF2ABbAsfW97bWW7s8UQZGCFfPYtRMn/F2XegfP6vSwaa2/gd1zuw+6VFiZuR22zo3hvUIu+7/nNBKfcd5SexfUg6awrlyk1+v7YF4VT4JTKdcfQ/UMTScTrcJAgMIrmslADDnj/97SS6CPHYo+cedspDfUb1QdwWUmNpa8D6u5PDVKjmKDBEiow+E+GbRzup5mjgQNnnC/LepuBkWJU7MTzdIU6lvJBYGDyI3/s/LVvPUCE/zMQ+zJ6285Db7rAmrfyxb8xgYusv6EDbCbCxy69KdQFsYAE0IIGEVKUlr/iaTarAC25y7bD78LtjDxbH0No7zx12LhamHH3SwtO7idlwraTywXpHi/D5mwZehyJVWpKaNTlUqhsaFDrRX5sfMUrdxJffMVR/HOqeM56qo3bHhxaNgfTYwh4bhD9l7ex8WwzWoYbMI6MENBR9yCkIr2qn3mPBlVrwsA16mwHS7u/GZiCLTfs/h26HEBt4z2gGxh4c6Qzf8swK8u6U041jBZI/Ae2FB4cmTXN0Y/gGkRZzH8avwzlLPKyzA1w="));

		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		host = "localhost";
		port = 3306;
		database = "minecraftmondays";
		username = "root";
		password = "";

		try {
			openConnection();
			System.out.println("[MMAPI] Connected to Database!");
		} catch (SQLException x) {
			x.printStackTrace();
		}

		getCommand("mm").setExecutor(new MinecraftMondaysCommand(this));
		getCommand("banner").setExecutor(new BannerCommand(this));
		getCommand("tp").setExecutor(new TPCommand(this));
		getCommand("bring").setExecutor(new BringCommand(this));
		getCommand("get").setExecutor(new GetCommand(this));
		getCommand("gm").setExecutor(new gmCommand(this));
		getCommand("ping").setExecutor(new PingCommand(this));
		getCommand("pm").setExecutor(new PMCommand(this));
		getCommand("game").setExecutor(new GameCommand(this));
		getCommand("allgame").setExecutor(new AllGameCommand(this));
		getCommand("lobby").setExecutor(new LobbyCommand(this));
		getCommand("skin").setExecutor(new SkinCommand(this));
		getCommand("location").setExecutor(new LocationCommand(this));

		LoadAllCustomSkins();

	}

	public void sendToServer(Player player, String name) {
		ByteArrayOutputStream stream;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bao);
		try {
			out.writeUTF("Connect"); // Connect = connect player to server
			out.writeUTF(name); // Target Server
		} catch (IOException e) {
		}
		stream = bao;
		player.sendPluginMessage(this, "BungeeCord", stream.toByteArray());
	}

	public void sendAllPlayersToServer(String name) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p != null) {
				sendToServer(p, name);
			}
		}
	}

	private void openConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			return;
		}
		connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
				this.username, this.password);
	}

	public void CreateEntryIfDoesntExist(Player player) {
		try {
			ResultSet rs = Main.prepareStatement(
					"SELECT COUNT(UUID) FROM player_scores WHERE UUID = '" + player.getUniqueId().toString() + "';")
					.executeQuery();
			rs.next();

			if (rs.getInt(1) == 0) {
				// is not in the system
				System.out.println(player.getName() + " is not in the system. Adding them now.");
				Main.prepareStatement(
						"INSERT INTO player_scores(UUID, SCORE) VALUES ('" + player.getUniqueId() + "'," + "DEFAULT);")
						.executeUpdate();
			} else { // is already in the system;
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public DyeColor GetDyeColorFromString(String s) {
		if (s.equalsIgnoreCase("BLACK")) {
			return DyeColor.BLACK;
		}
		if (s.equalsIgnoreCase("BLUE")) {
			return DyeColor.BLUE;
		}
		if (s.equalsIgnoreCase("BROWN")) {
			return DyeColor.BROWN;
		}
		if (s.equalsIgnoreCase("CYAN")) {
			return DyeColor.CYAN;
		}
		if (s.equalsIgnoreCase("GRAY")) {
			return DyeColor.GRAY;
		}
		if (s.equalsIgnoreCase("GREEN")) {
			return DyeColor.GREEN;
		}
		if (s.equalsIgnoreCase("LIGHT_BLUE")) {
			return DyeColor.LIGHT_BLUE;
		}
		if (s.equalsIgnoreCase("LIGHT_GRAY")) {
			return DyeColor.LIGHT_GRAY;
		}
		if (s.equalsIgnoreCase("LIME")) {
			return DyeColor.LIME;
		}
		if (s.equalsIgnoreCase("MAGENTA")) {
			return DyeColor.MAGENTA;
		}
		if (s.equalsIgnoreCase("ORANGE")) {
			return DyeColor.ORANGE;
		}
		if (s.equalsIgnoreCase("PINK")) {
			return DyeColor.PINK;
		}
		if (s.equalsIgnoreCase("PURPLE")) {
			return DyeColor.PURPLE;
		}
		if (s.equalsIgnoreCase("RED")) {
			return DyeColor.RED;
		}
		if (s.equalsIgnoreCase("WHITE")) {
			return DyeColor.WHITE;
		}
		if (s.equalsIgnoreCase("YELLOW")) {
			return DyeColor.YELLOW;
		}

		return DyeColor.WHITE;
	}

	public PatternType GetPatternTypeFromString(String s) {
		if (s.equalsIgnoreCase("BORDER")) {
			return PatternType.BORDER;
		}
		if (s.equalsIgnoreCase("CURLY_BORDER")) {
			return PatternType.CURLY_BORDER;
		}
		if (s.equalsIgnoreCase("STRAIGHT_CROSS")) {
			return PatternType.STRAIGHT_CROSS;
		}
		if (s.equalsIgnoreCase("CIRCLE_MIDDLE")) {
			return PatternType.CIRCLE_MIDDLE;
		}
		if (s.equalsIgnoreCase("BASE")) {
			return PatternType.BASE;
		}
		if (s.equalsIgnoreCase("BRICKS")) {
			return PatternType.BRICKS;
		}
		if (s.equalsIgnoreCase("CIRCLE_MIDDLE")) {
			return PatternType.CIRCLE_MIDDLE;
		}
		if (s.equalsIgnoreCase("CREEPER")) {
			return PatternType.CREEPER;
		}
		if (s.equalsIgnoreCase("CROSS")) {
			return PatternType.CROSS;
		}
		if (s.equalsIgnoreCase("DIAGONAL_LEFT")) {
			return PatternType.DIAGONAL_LEFT;
		}
		if (s.equalsIgnoreCase("DIAGONAL_LEFT_MIRROR")) {
			return PatternType.DIAGONAL_LEFT_MIRROR;
		}
		if (s.equalsIgnoreCase("DIAGONAL_RIGHT_MIRROR")) {
			return PatternType.DIAGONAL_RIGHT_MIRROR;
		}
		if (s.equalsIgnoreCase("DIAGONAL_RIGHT")) {
			return PatternType.DIAGONAL_RIGHT;
		}
		if (s.equalsIgnoreCase("FLOWER")) {
			return PatternType.FLOWER;
		}
		if (s.equalsIgnoreCase("GLOBE")) {
			return PatternType.GLOBE;
		}
		if (s.equalsIgnoreCase("GRADIENT")) {
			return PatternType.GRADIENT;
		}
		if (s.equalsIgnoreCase("GRADIENT_UP")) {
			return PatternType.GRADIENT_UP;
		}
		if (s.equalsIgnoreCase("HALF_HORIZONTAL")) {
			return PatternType.HALF_HORIZONTAL;
		}
		if (s.equalsIgnoreCase("HALF_HORIZONTAL_MIRROR")) {
			return PatternType.HALF_HORIZONTAL_MIRROR;
		}
		if (s.equalsIgnoreCase("HALF_VERTICAL")) {
			return PatternType.HALF_VERTICAL;
		}
		if (s.equalsIgnoreCase("HALF_VERTICAL_MIRROR")) {
			return PatternType.HALF_VERTICAL_MIRROR;
		}
		if (s.equalsIgnoreCase("MOJANG")) {
			return PatternType.MOJANG;
		}
		if (s.equalsIgnoreCase("RHOMBUS_MIDDLE")) {
			return PatternType.RHOMBUS_MIDDLE;
		}
		if (s.equalsIgnoreCase("SKULL")) {
			return PatternType.SKULL;
		}
		if (s.equalsIgnoreCase("SQUARE_BOTTOM_LEFT")) {
			return PatternType.SQUARE_BOTTOM_LEFT;
		}
		if (s.equalsIgnoreCase("SQUARE_BOTTOM_RIGHT")) {
			return PatternType.SQUARE_BOTTOM_RIGHT;
		}
		if (s.equalsIgnoreCase("SQUARE_TOP_LEFT")) {
			return PatternType.SQUARE_TOP_LEFT;
		}
		if (s.equalsIgnoreCase("SQUARE_TOP_RIGHT")) {
			return PatternType.SQUARE_TOP_RIGHT;
		}
		if (s.equalsIgnoreCase("STRAIGHT_CROSS")) {
			return PatternType.STRAIGHT_CROSS;
		}
		if (s.equalsIgnoreCase("STRIPE_BOTTOM")) {
			return PatternType.STRIPE_BOTTOM;
		}
		if (s.equalsIgnoreCase("STRIPE_CENTER")) {
			return PatternType.STRIPE_CENTER;
		}
		if (s.equalsIgnoreCase("STRIPE_DOWNLEFT")) {
			return PatternType.STRIPE_DOWNLEFT;
		}
		if (s.equalsIgnoreCase("STRIPE_DOWNRIGHT")) {
			return PatternType.STRIPE_DOWNRIGHT;
		}
		if (s.equalsIgnoreCase("STRIPE_LEFT")) {
			return PatternType.STRIPE_LEFT;
		}
		if (s.equalsIgnoreCase("STRIPE_MIDDLE")) {
			return PatternType.STRIPE_MIDDLE;
		}
		if (s.equalsIgnoreCase("STRIPE_RIGHT")) {
			return PatternType.STRIPE_RIGHT;
		}
		if (s.equalsIgnoreCase("STRIPE_SMALL")) {
			return PatternType.STRIPE_SMALL;
		}
		if (s.equalsIgnoreCase("STRIPE_TOP")) {
			return PatternType.STRIPE_TOP;
		}
		if (s.equalsIgnoreCase("TRIANGLE_BOTTOM")) {
			return PatternType.TRIANGLE_BOTTOM;
		}
		if (s.equalsIgnoreCase("TRIANGLE_TOP")) {
			return PatternType.TRIANGLE_TOP;
		}
		if (s.equalsIgnoreCase("TRIANGLES_BOTTOM")) {
			return PatternType.TRIANGLES_BOTTOM;
		}
		if (s.equalsIgnoreCase("TRIANGLES_TOP")) {
			return PatternType.TRIANGLES_TOP;
		}

		return PatternType.BASE;
	}

	public Pattern MakePatternFromPart(String part) {
		try {
			String[] parts = part.split("-");
			// part 0 = pattern --- part 1 = color
			if (parts.length != 2) {
				System.out.println("Error parsing part: " + part);
				return null;
			}
			PatternType type = GetPatternTypeFromString(parts[0]);
			DyeColor col = GetDyeColorFromString(parts[1]);
			Pattern pat = new Pattern(col, type);
			return pat;

		} catch (Exception e) {
			return null;
		}

	}

	public List<Pattern> CreatePatternsFromData(String data) {
		List<Pattern> patterns = new ArrayList<Pattern>();
		ItemStack is = null;
		try {
			String[] parts = data.split(";");
			if (parts.length == 1) {
				System.out.println("Length cannot be 1 [CREATEPATTERN]");
				return null;
			} else {
				String bannerType = parts[0];
				if (bannerType.equalsIgnoreCase("WHITE_BANNER")) {
					is = new ItemStack(Material.WHITE_BANNER);
				}
				int i = 1;
				while (i <= parts.length - 1) {
					patterns.add(MakePatternFromPart(parts[i]));
					i++;
				}
			}
		} catch (Exception e) {
		}

		return patterns;
	}

	public List<Pattern> GetBannerPatternForPlayer(Player player) {
		String data = LoadPlayerBannerData(player);
		List<Pattern> patterns = CreatePatternsFromData(data);
		return patterns;
	}

	public void CreateBannerEntryIfDoesntExist(Player player) {
		try {
			ResultSet rs = Main.prepareStatement(
					"SELECT COUNT(UUID) FROM player_banners WHERE UUID = '" + player.getUniqueId().toString() + "';")
					.executeQuery();
			rs.next();

			if (rs.getInt(1) == 0) {
				// is not in the system
				System.out.println(player.getName() + " is not in the system. Adding them now.");
				Main.prepareStatement(
						"INSERT INTO player_banners(UUID, DATA) VALUES ('" + player.getUniqueId() + "'," + "DEFAULT);")
						.executeUpdate();
			} else { // is already in the system;
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void SetPlayerBannerData(Player player, String data) {
		CreateBannerEntryIfDoesntExist(player);
		int length = data.length();
		if (length > 6000) {
			player.sendMessage(ChatColor.RED + "your banner is too complicated!");
			return;
		}
		try {
			Main.prepareStatement(
					"UPDATE player_banners SET DATA = '" + data + "' WHERE UUID = '" + player.getUniqueId() + "';")
					.executeUpdate();
			player.sendMessage(
					ChatColor.BLUE + "[Minecraft Mondays] " + ChatColor.GREEN + "Your banner has been updated");

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	public String LoadPlayerBannerData(Player player) {
		CreateBannerEntryIfDoesntExist(player);
		String return_data = "null";
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_banners WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			return_data = rs2.getString("DATA");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return return_data;
	}

	public void SetDefaultNicknameIfDoesntExist(Player player) {
		try {
			ResultSet rs = Main.prepareStatement(
					"SELECT COUNT(UUID) FROM player_names WHERE UUID = '" + player.getUniqueId().toString() + "';")
					.executeQuery();
			rs.next();

			if (rs.getInt(1) == 0) {
				// is not in the system
				System.out.println(player.getName() + " is not in the name system. Adding them now.");
				Main.prepareStatement(
						"INSERT INTO player_names(UUID, NAME) VALUES ('" + player.getUniqueId() + "'," + "DEFAULT);")
						.executeUpdate();
			} else { // is already in the system;
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public boolean isNicknamed(Player player) {
		try {
			String name = GetNickname(player);
			// System.out.println("The current nickname is: " + name);
			if (!name.equalsIgnoreCase("unnamed")) {
				// System.out.println("True");

				return true;
			} else {
				// System.out.println("False");
				return false;
			}
		} catch (Exception e) {
			System.out.print("Error getting name");
			return false;
		}

	}

	public void SetNickname(String nickname, Player player) {
		SetDefaultNicknameIfDoesntExist(player);
		int length = nickname.length();
		if (length > 10) {
			player.sendMessage(ChatColor.RED + "The Max Length of your name is 10!");
			return;
		}
		try {
			Main.prepareStatement(
					"UPDATE player_names SET NAME = '" + nickname + "' WHERE UUID = '" + player.getUniqueId() + "';")
					.executeUpdate();
			player.sendMessage(ChatColor.BLUE + "[Minecraft Mondays] " + ChatColor.GREEN + "Your name is now: "
					+ GetNickname(player));

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void SetMCName(String MCName, Player player) {
		try {
			Main.prepareStatement(
					"UPDATE player_names SET MCNAME = '" + MCName + "' WHERE UUID = '" + player.getUniqueId() + "';")
					.executeUpdate();

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void LoadAllCustomSkins() {
		customSkins.clear();
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM custom_skins;").executeQuery();
			while (rs2.next()) {
				String name = rs2.getString("SKIN_NAME");
				String data = rs2.getString("SKIN_DATA");
				String sig = rs2.getString("SKIN_SIG");
				CustomSkin skin = new CustomSkin(name, data, sig);
				customSkins.add(skin);
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}
		System.out.println("Loaded " + customSkins.size() + " skins");
	}

	public String GetPlayerSkinName(Player player) {
		String return_name = "null";
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_names WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			return_name = rs2.getString("SKINNAME");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return return_name;
	}

	public void SetPlayerSkin(Player player, String skinName) {
		// try get the skin
		CustomSkin skin = GetSkin(skinName);
		if (skin != null) {
			SetPlayerSkin(player, skin);
		} else {
			player.sendMessage("Failed to set your skin to be " + skinName);
		}
	}

	public CustomSkin GetSkin(String skinName) {
		for (CustomSkin s : customSkins) {
			if (s.name.equals(skinName)) {
				return s;
			}
		}
		return null;
	}

	public void SetPlayerSkinFromSaved(Player player) {
		String skinName = GetPlayerSkinName(player);
		CustomSkin skin = GetSkin(skinName);
		if (skin != null) {
			SetPlayerSkin(player, skin);
		}

	}

	public void SetPlayerSkin(Player player, CustomSkin skin) {
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		GameProfile gp = ep.getProfile();
		PropertyMap pm = gp.getProperties();
		Property property = pm.get("textures").iterator().next();
		pm.remove("textures", property);
		pm.put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
		// player.sendMessage("Set your skin to be " + skin.getName() + " length of
		// data: " + skin.getValue().length() + " legnth of sig: " +
		// skin.getSignature().length());

	}

	public UUID GetUUIDFromNickname(String nickname) {
		UUID return_id = null;
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_names WHERE NAME = '" + nickname + "';")
					.executeQuery();
			rs2.next();
			return_id = UUID.fromString(rs2.getString("UUID"));
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return return_id;
	}

	public String GetMCName(Player player) {
		String return_name = "null";
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_names WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			return_name = rs2.getString("MCNAME");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return return_name;
	}

	public String GetNickname(Player player) {
		SetDefaultNicknameIfDoesntExist(player);
		String return_name = "null";
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_names WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			return_name = rs2.getString("NAME");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		ChatColor playerColour = getChatColor(player);
		return playerColour + return_name;
	}

	public void SpawnPlayerFireWork(Player player, Location loc) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p != null) {
				Firework fw = (Firework) p.getWorld().spawnEntity(loc, EntityType.FIREWORK);
				setPlayerFireWork(player, fw);
				fw.detonate();
			}
		}
	}

	public void setPlayerFireWork(Player player, Firework fw) {
		ChatColor playerColor = getChatColor(player);
		FireworkMeta fwm = fw.getFireworkMeta();

		fwm.setPower(2);
		fwm.addEffect(FireworkEffect.builder().withColor(translateChatColorToColor(playerColor)).flicker(true).build());

		fw.setFireworkMeta(fwm);
	}

	public static Color translateChatColorToColor(ChatColor chatColor) {
		switch (chatColor) {
		case AQUA:
			return Color.AQUA;
		case BLACK:
			return Color.BLACK;
		case BLUE:
			return Color.BLUE;
		case DARK_AQUA:
			return Color.BLUE;
		case DARK_BLUE:
			return Color.BLUE;
		case DARK_GRAY:
			return Color.GRAY;
		case DARK_GREEN:
			return Color.GREEN;
		case DARK_PURPLE:
			return Color.PURPLE;
		case DARK_RED:
			return Color.RED;
		case GOLD:
			return Color.YELLOW;
		case GRAY:
			return Color.GRAY;
		case GREEN:
			return Color.GREEN;
		case LIGHT_PURPLE:
			return Color.PURPLE;
		case RED:
			return Color.RED;
		case WHITE:
			return Color.WHITE;
		case YELLOW:
			return Color.YELLOW;
		default:
			break;
		}

		return null;
	}

	public ChatColor getChatColor(Player player) {
		String colorName = "white";
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_names WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			colorName = rs2.getString("COLOR");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		switch (colorName) {
		case "white":
			return ChatColor.WHITE;
		case "aqua":
			return ChatColor.AQUA;
		case "black":
			return ChatColor.BLACK;
		case "blue":
			return ChatColor.BLUE;
		case "dark_aqua":
			return ChatColor.DARK_AQUA;
		case "dark_blue":
			return ChatColor.DARK_BLUE;
		case "dark_gray":
			return ChatColor.DARK_GRAY;
		case "dark_green":
			return ChatColor.DARK_GREEN;
		case "dark_purple":
			return ChatColor.DARK_PURPLE;
		case "dark_red":
			return ChatColor.DARK_RED;
		case "gold":
			return ChatColor.GOLD;
		case "gray":
			return ChatColor.GRAY;
		case "green":
			return ChatColor.GREEN;
		case "light_purple":
			return ChatColor.LIGHT_PURPLE;
		case "magic":
			return ChatColor.MAGIC;
		case "red":
			return ChatColor.RED;
		case "yellow":
			return ChatColor.YELLOW;
		case "purple":
			return ChatColor.DARK_PURPLE;
		case"orange":
			return ChatColor.GOLD;
		case "pink":
			return ChatColor.LIGHT_PURPLE;

		default:
			return ChatColor.WHITE;
		}
	}

	public String GetNickname(UUID player) {
		// SetDefaultNicknameIfDoesntExist(player);
		String return_name = "null";
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_names WHERE UUID = '" + player + "';")
					.executeQuery();
			rs2.next();
			return_name = rs2.getString("NAME");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return return_name;
	}

	/*
	 * Works from 1.0+.
	 *
	 * @param name new name of the player
	 * 
	 * @param player player to change the name of
	 */
	@SuppressWarnings("unchecked")
	public void changeName(String name, Player player) {
		try {
			int length = name.length();
			if (length > 16) {
				System.out.println("The length of the name is too high: " + length);
				String newName = "";
				newName = name.substring(0, 15);
				name = newName;
				name = newName;
			}
			Method getHandle = player.getClass().getMethod("getHandle");
			Object entityPlayer = getHandle.invoke(player);
			/*
			 * These methods are no longer needed, as we can just access the profile using
			 * handle.getProfile. Also, because we can just use the method, which will not
			 * change, we don't have to do any field-name look-ups.
			 */
			boolean gameProfileExists = false;
			// Some 1.7 versions had the GameProfile class in a different package
			try {
				Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
				gameProfileExists = true;
			} catch (ClassNotFoundException ignored) {

			}
			try {
				Class.forName("com.mojang.authlib.GameProfile");
				gameProfileExists = true;
			} catch (ClassNotFoundException ignored) {

			}
			if (!gameProfileExists) {
				/*
				 * Only 1.6 and lower servers will run this code.
				 *
				 * In these versions, the name wasn't stored in a GameProfile object, but as a
				 * String in the (final) name field of the EntityHuman class. Final (non-static)
				 * fields can actually be modified by using {@link
				 * java.lang.reflect.Field#setAccessible(boolean)}
				 */
				Field nameField = entityPlayer.getClass().getSuperclass().getDeclaredField("name");
				nameField.setAccessible(true);
				nameField.set(entityPlayer, name);
			} else {
				// Only 1.7+ servers will run this code
				Object profile = entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer);
				Field ff = profile.getClass().getDeclaredField("name");
				ff.setAccessible(true);
				ff.set(profile, name);
			}
			// In older versions, Bukkit.getOnlinePlayers() returned an Array instead of a
			// Collection.
			if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class) {
				Collection<? extends Player> players = (Collection<? extends Player>) Bukkit.class
						.getMethod("getOnlinePlayers").invoke(null);
				for (Player p : players) {
					p.hidePlayer(player);
					p.showPlayer(player);
				}
			} else {
				Player[] players = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers").invoke(null));
				for (Player p : players) {
					p.hidePlayer(player);
					p.showPlayer(player);
				}
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchFieldException e) {
			/*
			 * Merged all the exceptions. Less lines
			 */
			e.printStackTrace();
		}
		player.setDisplayName(name);
		player.setPlayerListName(name);
	}

	public void CreateCurrentGameStateIfDoesntExist() {
		try {
			ResultSet rs = Main
					.prepareStatement("SELECT COUNT(STATE_NAME) FROM game_states WHERE STATE_NAME = 'IS_LIVE';")
					.executeQuery();
			rs.next();

			if (rs.getInt(1) == 0) {
				// is not in the system
				Main.prepareStatement("INSERT INTO game_states(STATE_NAME, VALUE) VALUES ('IS_LIVE','NOT_LIVE');")
						.executeUpdate();
			} else { // is already in the system;
			}
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void AddScoreToPlayer(Player player, String reason, int amt) {
		CreateEntryIfDoesntExist(player);
		int currentScore;
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_scores WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			currentScore = rs2.getInt("SCORE");
			System.out.println(player.getName() + " currently has a score of: " + currentScore);
			int finalScore = currentScore + amt;
			Main.prepareStatement("UPDATE player_scores SET SCORE = '" + finalScore + "' WHERE UUID = '"
					+ player.getUniqueId() + "';").executeUpdate();
			System.out.println(player.getName() + " now has a score of: " + finalScore);
			player.sendMessage(ChatColor.BLUE + "[Minecraft Mondays] " + ChatColor.GREEN + "+" + amt + " score "
					+ reason + " [" + GetPlayerScore(player) + "]");

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	public List<UUID> GetPlayersInOrder() {
		List<UUID> playerList = new ArrayList<UUID>();
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_scores ORDER BY SCORE DESC;").executeQuery();
			while (rs2.next()) {
				UUID id = UUID.fromString(rs2.getString("UUID"));
				playerList.add(id);
			}

		} catch (SQLException x) {
			x.printStackTrace();
		}
		return playerList;
	}

	public int GetPlayerScoreCount() {
		List<Player> playerList = new ArrayList<Player>();
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_scores ORDER BY SCORE DESC;").executeQuery();
			while (rs2.next()) {
				UUID id = UUID.fromString(rs2.getString("UUID"));
				playerList.add(Bukkit.getPlayer(id));
			}

		} catch (SQLException x) {
			x.printStackTrace();
		}
		return playerList.size();
	}

	public void MinusScoreToPlayer(Player player, int amt) {
		CreateEntryIfDoesntExist(player);
		int currentScore;
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_scores WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			currentScore = rs2.getInt("SCORE");
			System.out.println(player.getName() + " currently has a score of: " + currentScore);
			int finalScore = currentScore - amt;
			if (finalScore < 0) {
				finalScore = 0;
			}
			Main.prepareStatement("UPDATE player_scores SET SCORE = '" + finalScore + "' WHERE UUID = '"
					+ player.getUniqueId() + "';").executeUpdate();
			System.out.println(player.getName() + " now has a score of: " + finalScore);
			player.sendMessage(ChatColor.BLUE + "[Minecraft Mondays] " + ChatColor.GREEN + "-" + amt + " points ["
					+ GetPlayerScore(player) + "]");

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	public void SetPlayerScore(Player player, int value) {
		CreateEntryIfDoesntExist(player);
		try {
			Main.prepareStatement(
					"UPDATE player_scores SET SCORE = '" + 0 + "' WHERE UUID = '" + player.getUniqueId() + "';")
					.executeUpdate();
			System.out.println(player.getName() + " now has a score of: " + 0);
			player.sendMessage(ChatColor.BLUE + "[Minecraft Mondays] " + ChatColor.GREEN + "Your score is now: "
					+ GetPlayerScore(player));

		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void ResetScores() {
		try {
			Main.prepareStatement("DELETE FROM player_scores").executeUpdate();
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public void sendMessageToAllPlayers(String msg) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player != null) {
				player.sendMessage(msg);
			}
		}
	}

	public void MMStart() {
		ResetScores();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player != null) {
				SetPlayerScore(player, 0);

			}
		}
		sendMessageToAllPlayers(
				ChatColor.GREEN + "Minecraft Mondays Has Started!" + ChatColor.YELLOW + " All Scores have been reset.");
		MMResume();
	}

	public void MMStop() {
		sendMessageToAllPlayers(ChatColor.GREEN + "Minecraft Mondays Has Ended!");
		List<UUID> playersInOrder = GetPlayersInOrder();
		int size = playersInOrder.size();
		System.out.println("Number of players: " + size);
		if (size >= 1) {
			try {
				sendMessageToAllPlayers(ChatColor.GOLD + "1st Place: " + GetNickname(playersInOrder.get(0)));
			} catch (Exception e) {

			}
		}
		if (size >= 2) {
			try {
				sendMessageToAllPlayers(ChatColor.GRAY + "2nd Place: " + GetNickname(playersInOrder.get(1)));
			} catch (Exception e) {

			}

		}
		if (size >= 3) {
			try {
				sendMessageToAllPlayers(ChatColor.WHITE + "3rd Place: " + GetNickname(playersInOrder.get(2)));
			} catch (Exception e) {

			}
		}

		List<UUID> allPlayers = GetPlayersInOrder();
		int i = 1;
		sendMessageToAllPlayers(ChatColor.WHITE + "Final Scores");
		for (UUID id : allPlayers) {
			String name = GetNickname(id);
			sendMessageToAllPlayers(i + "." + name + ChatColor.GREEN + " " + ChatColor.YELLOW + GetPlayerScore(id));
			i++;

		}

		ResetScores();
	}

	public void MMPause() {
		sendMessageToAllPlayers(ChatColor.GREEN + "Minecraft Mondays Has Been Paused!");
		sendMessageToAllPlayers(ChatColor.GREEN + "You Will No Longer Earn Points");
		setLive("NOT_LIVE");

	}

	public void MMResume() {
		sendMessageToAllPlayers(ChatColor.GREEN + "Minecraft Mondays Has Been Resumed");
		sendMessageToAllPlayers(ChatColor.GREEN + "You Will Now Earn Points");
		setLive("LIVE");
	}

	public int GetPlayerScore(Player player) {
		CreateEntryIfDoesntExist(player);
		int returnScore = 0;
		try {
			ResultSet rs2 = Main
					.prepareStatement("SELECT * FROM player_scores WHERE UUID = '" + player.getUniqueId() + "';")
					.executeQuery();
			rs2.next();
			returnScore = rs2.getInt("SCORE");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return returnScore;
	}

	public int GetPlayerScore(UUID player) {
		// CreateEntryIfDoesntExist(player);
		int returnScore = 0;
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM player_scores WHERE UUID = '" + player + "';")
					.executeQuery();
			rs2.next();
			returnScore = rs2.getInt("SCORE");
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return returnScore;
	}

	public boolean isLive() {
		CreateCurrentGameStateIfDoesntExist();
		boolean b = false;
		try {
			ResultSet rs2 = Main.prepareStatement("SELECT * FROM game_states WHERE STATE_NAME = 'IS_LIVE';")
					.executeQuery();
			rs2.next();
			b = (rs2.getString("VALUE").equalsIgnoreCase("LIVE"));
		} catch (SQLException x) {
			x.printStackTrace();
		}
		return b;
	}

	public void setLive(String string1) {
		CreateCurrentGameStateIfDoesntExist();
		try {
			Main.prepareStatement("UPDATE game_states SET VALUE = '" + string1 + "' WHERE STATE_NAME = 'IS_LIVE';")
					.executeUpdate();
		} catch (SQLException x) {
			x.printStackTrace();
		}
	}

	public static PreparedStatement prepareStatement(String query) {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ps;
	}
}
