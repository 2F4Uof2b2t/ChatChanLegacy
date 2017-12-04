package com.sashachatchan.events;

import com.sashachatchan.ChatPlayer;
import com.sashachatchan.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;

import static com.sashachatchan.Main.p;

public class KickList extends PlayerListener {

    public KickList() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_KICK, this, Event.Priority.Highest, p);
    }

    @Override
    public void onPlayerKick(final PlayerKickEvent e) {
        ChatPlayer joinedP = new ChatPlayer(e.getPlayer());
        Main.chatPlayers.remove(joinedP);
    }

}
