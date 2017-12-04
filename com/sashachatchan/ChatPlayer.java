package com.sashachatchan;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatPlayer {

    public Player player;
    public String playerName;
    //private String UUID;
    public boolean chatOff;
    protected ArrayList<String> ignorelist;
    private ChatPlayer lastMessager;
    private ChatPlayer lastSentTo;
    protected boolean didErrorOccur = false;

    public ChatPlayer(Player player)
    {
        this.player = player;
        this.playerName = player.getName();
        //this.UUID = player.getUniqueId().toString();
        this.chatOff = false;
        this.lastMessager = null;
        this.lastSentTo = null;
        this.ignorelist = new ArrayList();
        try {
            getIgnoreList();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getIgnoreList()
            throws IOException
    {
        File file = new File("ignores/" + this.playerName + ".txt");
        file.getParentFile().mkdirs();
        if (!file.exists())
        {
            file.createNewFile();
            return;
        }
        IgnorelistThreadTask ignorelistgetter = new IgnorelistThreadTask(file, this.playerName, this);
        ignorelistgetter.start();
    }

    public void writeIgnoreList()
            throws IOException
    {
        File file = new File("ignores/" + this.playerName + ".txt");
        if (file.exists()) {
            file.getParentFile().mkdirs();
        }
        IgnorelistWriteThreadTask writertask = new IgnorelistWriteThreadTask(file, this.playerName, this);
        writertask.start();
    }

    public void setLastMessager(ChatPlayer sender)
    {
        this.lastMessager = sender;
    }

    public void setLastSentTo(ChatPlayer reciever)
    {
        this.lastSentTo = reciever;
    }

    public boolean isIgnoring(String playerName)
    {
        if (this.ignorelist == null) {
            return false;
        }
        return this.ignorelist.contains(playerName);
    }

    public void addToIgnoreList(String playerName)
    {
        if (this.ignorelist.contains(playerName)) {
            return;
        }
        this.ignorelist.add(playerName);
        try
        {
            writeIgnoreList();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void removeFromIgnorelist(String playerName)
    {
        if (!this.ignorelist.contains(playerName)) {
            return;
        }
        this.ignorelist.remove(playerName);
        try
        {
            writeIgnoreList();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ChatPlayer getLastMessager()
    {
        return this.lastMessager;
    }

    public ChatPlayer getLastSentTo()
    {
        return this.lastSentTo;
    }

    public void sendWhisper(ChatPlayer sender, String message)
    {
        this.player.sendMessage("\247d" + sender.player.getName() + " whispers: " + message);
    }
}