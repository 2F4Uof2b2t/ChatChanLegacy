package com.sashachatchan.events;

import com.sashachatchan.ChatPlayer;
import com.sashachatchan.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.sashachatchan.Main.p;

public class QuitList extends PlayerListener {

    public QuitList() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, this, Event.Priority.Highest, p);
    }

    @Override
    public void onPlayerQuit(final PlayerQuitEvent e) {
        ChatPlayer joinedP = new ChatPlayer(e.getPlayer());
        Main.chatPlayers.remove(joinedP);
    }

}
