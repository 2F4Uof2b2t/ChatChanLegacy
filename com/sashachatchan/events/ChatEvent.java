package com.sashachatchan.events;

import com.sashachatchan.ChatPlayer;
import com.sashachatchan.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

import static com.sashachatchan.Main.p;

public class ChatEvent extends PlayerListener {

    public ChatEvent() {
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_CHAT, this, Event.Priority.Highest, p);
    }

    @Override
    public void onPlayerChat(final PlayerChatEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getMessage().startsWith(">")) {
            e.setMessage("\247a" + e.getMessage());
        }
        Player[] r = e.getRecipients().toArray(new Player[0]);
        int i = 0;
        while (i < r.length)
        {
            ChatPlayer cp = Main.getChatPlayerByName(r[i].getName());
            if ((cp.isIgnoring(e.getPlayer().getName())) || (cp.chatOff)) {
                e.getRecipients().remove(r[i]);
            }
            i++;
        }
    }

}
