package com.sashachatchan.events;

import com.sashachatchan.ChatPlayer;
import com.sashachatchan.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import static com.sashachatchan.Main.p;

public class JoinList extends PlayerListener {

    public JoinList() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, this, Event.Priority.Highest, p);
    }

    @Override
    public void onPlayerJoin(final PlayerJoinEvent e) {
        ChatPlayer joinedP = new ChatPlayer(e.getPlayer());
        Main.chatPlayers.add(joinedP);
    }

}
