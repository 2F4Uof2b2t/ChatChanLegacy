package com.sashachatchan;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IgnorelistWriteThreadTask implements Runnable
{
    private File theIgnoreListFile;
    private Thread t;
    private String threadName;
    private ChatPlayer cPlayer;

    public IgnorelistWriteThreadTask(File file, String threadName, ChatPlayer thePlayer)
    {
        this.theIgnoreListFile = file;
        this.threadName = threadName;
        this.cPlayer = thePlayer;
    }

    public void run()
    {
        try
        {
            this.theIgnoreListFile.setWritable(true);
            this.theIgnoreListFile.createNewFile();
            FileWriter writer = new FileWriter(this.theIgnoreListFile, false);
            BufferedWriter writer1 = new BufferedWriter(writer);
            for (String player : this.cPlayer.ignorelist) {
                writer1.write(player + "\r\n");
            }
            writer1.close();
        }
        catch (IOException e)
        {
            System.out.println("Error whilst writing ignorelist from thread \"" + this.threadName + "\"");
            e.printStackTrace();
            this.cPlayer.didErrorOccur = true;
        }
    }

    public void start()
    {
        if (this.t == null)
        {
            this.t = new Thread(this, this.threadName);
            this.t.start();
        }
    }
}
