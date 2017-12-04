package com.sashachatchan;

import com.sashachatchan.events.ChatEvent;
import com.sashachatchan.events.JoinList;
import com.sashachatchan.events.KickList;
import com.sashachatchan.events.QuitList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class Main extends JavaPlugin {


    public static File data;
    public static ArrayList<ChatPlayer> chatPlayers = new ArrayList<>();
    public static JavaPlugin p;


    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        p = this;
        /*
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, new ChatEvent(), Event.Priority.High, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new JoinList(), Event.Priority.High, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, new QuitList(), Event.Priority.High, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, new KickList(), Event.Priority.High, this);
        */
        //Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new JoinList(), Event.Priority.Highest, p);
        new QuitList();
        new KickList();
        new ChatEvent();
        new JoinList();
        data = this.getDataFolder();
    }

    public static ChatPlayer getChatPlayerByName(String name)
    {
        for (ChatPlayer chatPlayer : chatPlayers) {
            if (chatPlayer.playerName.equalsIgnoreCase(name)) {
                return chatPlayer;
            }
        }
        return null;
    }

    public boolean isPlayerOnline(String p)
    {
        for (Player lp : getServer().getOnlinePlayers()) {
            if (lp.getName().equalsIgnoreCase(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\2474you must be a player");
            return false;
        }
        Player player = (Player)sender;
        ChatPlayer cPlayer = Main.getChatPlayerByName(player.getName());
        if (commandLabel.toLowerCase().matches("w|whisper|msg|t")) {
            if (args == null || args.length == 0) {
                player.sendMessage("\u00a74Incorrect syntax.");
                player.sendMessage("\u00a77/%cmd% <player> <message...>".replace("%cmd%", commandLabel));
                return true;
            }
            if (!this.isPlayerOnline(args[0])) {
                player.sendMessage("\u00a74The specified player, \"" + args[0] + "\", is not online.");
                return true;
            }
            if (cPlayer.isIgnoring(args[0])) {
                player.sendMessage("\u00a74You are ignoring " + args[0]);
                return true;
            }
            ChatPlayer theOtherPlayer = Main.getChatPlayerByName(args[0]);
            if (theOtherPlayer.isIgnoring(cPlayer.playerName)) {
                player.sendMessage("\u00a74" + args[0] + " is ignoring you.");
                return true;
            }
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String s : args) {
                if (i == 0) {
                    ++i;
                    continue;
                }
                builder.append(s + " ");
                ++i;
            }
            if (i == 1) {
                player.sendMessage("\u00a74You didn't write out a message.");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            player.sendMessage("\u00a7dYou whisper to " + theOtherPlayer.playerName + ": " + builder.toString());
            target.sendMessage("\u00a7d" + player.getName() + " whispers: " + builder.toString());
            theOtherPlayer.setLastMessager(cPlayer);
            cPlayer.setLastSentTo(theOtherPlayer);
            return true;
        }
        if (commandLabel.equalsIgnoreCase("r")) {
            if (args == null || args.length == 0) {
                player.sendMessage("\u00a74Incorrect syntax.");
                player.sendMessage("\u00a77/r <message...>");
                return true;
            }
            if (cPlayer.getLastMessager() == null) {
                player.sendMessage("\u00a74You have no one that you can reply to.");
                return true;
            }
            if (!this.isPlayerOnline(cPlayer.getLastMessager().playerName)) {
                player.sendMessage("\u00a74The specified player, \"" + cPlayer.getLastMessager().playerName + "\", is not online.");
                return true;
            }
            if (cPlayer.isIgnoring(cPlayer.playerName)) {
                player.sendMessage("\u00a74You are ignoring " + cPlayer.getLastMessager().playerName);
                return true;
            }
            ChatPlayer theOtherPlayer = Main.getChatPlayerByName(cPlayer.getLastMessager().playerName);
            if (theOtherPlayer.isIgnoring(cPlayer.playerName)) {
                player.sendMessage("\u00a74" + cPlayer.getLastMessager().playerName + " is ignoring you.");
                return true;
            }
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String s : args) {
                builder.append(s + " ");
                ++i;
            }
            if (i == 0) {
                player.sendMessage("\u00a74You didn't write out a message.");
                return true;
            }
            Player target = Bukkit.getPlayer((String)cPlayer.getLastMessager().playerName);
            player.sendMessage("\u00a7dYou whisper to " + theOtherPlayer.playerName + ": " + builder.toString());
            target.sendMessage("\u00a7d" + player.getName() + " whispers: " + builder.toString());
            theOtherPlayer.setLastMessager(cPlayer);
            cPlayer.setLastSentTo(theOtherPlayer);
            return true;
        }
        if (commandLabel.equalsIgnoreCase("l")) {
            if (args == null || args.length == 0) {
                player.sendMessage("\u00a74Incorrect syntax.");
                player.sendMessage("\u00a77/l <message...>");
                return true;
            }
            if (cPlayer.getLastSentTo() == null) {
                player.sendMessage("\u00a74You have no one that you can resend to.");
                return true;
            }
            if (!this.isPlayerOnline(cPlayer.getLastSentTo().playerName)) {
                player.sendMessage("\u00a74The specified player, \"" + cPlayer.getLastSentTo().playerName + "\", is not online.");
                return true;
            }
            if (cPlayer.isIgnoring(cPlayer.playerName)) {
                player.sendMessage("\u00a74You are ignoring " + cPlayer.getLastSentTo().playerName);
                return true;
            }
            ChatPlayer theOtherPlayer = Main.getChatPlayerByName(cPlayer.getLastSentTo().playerName);
            if (theOtherPlayer.isIgnoring(cPlayer.playerName)) {
                player.sendMessage("\u00a74" + cPlayer.getLastSentTo().playerName + " is ignoring you.");
                return true;
            }
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String s : args) {
                builder.append(s + " ");
                i++;
            }
            if (i == 0) {
                player.sendMessage("\u00a74You didn't write out a message.");
                return true;
            }
            Player target = Bukkit.getPlayer((String)cPlayer.getLastSentTo().playerName);
            player.sendMessage("\u00a7dYou whisper to " + theOtherPlayer.playerName + ": " + builder.toString());
            target.sendMessage("\u00a7d" + player.getName() + " whispers: " + builder.toString());
            theOtherPlayer.setLastMessager(cPlayer);
            cPlayer.setLastSentTo(theOtherPlayer);
            return true;
        }
        if (commandLabel.equalsIgnoreCase("ignore")) {
            if (args == null || args.length == 0) {
                player.sendMessage("\u00a74Incorrect syntax.");
                player.sendMessage("\u00a77/ignore <player>");
                return true;
            }
            ChatPlayer arged = Main.getChatPlayerByName(args[0]);
            if (cPlayer.isIgnoring(args[0])) {
                cPlayer.removeFromIgnorelist(args[0]);
                player.sendMessage("\u00a7e" + args[0] + " removed from ignorelist.");
                return true;
            }
            cPlayer.addToIgnoreList(args[0]);
            player.sendMessage("\u00a7e" + args[0] + " added to ignorelist.");
        }
        if (commandLabel.equalsIgnoreCase("togglechat")) {
            if (cPlayer.chatOff) {
                cPlayer.chatOff = false;
                player.sendMessage("\u00a7eChat turned on. Run the command again to turn it back off.");
                return true;
            }
            cPlayer.chatOff = true;
            player.sendMessage("\u00a7eChat turned off. Run the command again to turn it back on.");
            return true;
        }
        if (commandLabel.equalsIgnoreCase("ignorelist")) {
            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (String s : cPlayer.ignorelist) {
                builder.append(s + ", ");
                ++i;
            }
            player.sendMessage(ChatColor.GRAY + builder.toString());
            player.sendMessage(ChatColor.DARK_AQUA + "" + i + " ignored players.");
        }
        return true;
    }
}
