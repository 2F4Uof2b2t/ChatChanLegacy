package com.sashachatchan;

import java.io.*;

public class IgnorelistThreadTask implements Runnable
{
    private File theIgnoreListFile;
    private Thread t;
    private String threadName;
    private ChatPlayer cPlayer;

    public IgnorelistThreadTask(File file, String threadName, ChatPlayer thePlayer)
    {
        this.theIgnoreListFile = file;
        this.threadName = threadName;
        this.cPlayer = thePlayer;
    }

    public void run()
    {
        try
        {
            FileInputStream input = new FileInputStream(this.theIgnoreListFile);
            InputStreamReader reader = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                this.cPlayer.ignorelist.add(line);
            }
            br.close();
        }
        catch (IOException e)
        {
            System.out.println("(&s)Error whilst reading ignorelist from thread \"" + this.threadName + "\"".replace("&s", this.theIgnoreListFile.getAbsolutePath()));
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
