package com.mygdx.mongojocs.iapplicationemu;

import com.mygdx.mongojocs.midletemu.MIDlet;

public class IApplication implements Runnable{

    public static boolean appClosed;
    public static String appFilesFolder = "";
    public static String assetsFolder;

    public static void setAppFilesFolder(String appFilesFolder) {
        IApplication.appFilesFolder = appFilesFolder;
    }

    public static IApplication getCurrentApp() {
        return null;
    }

    public static void setAssetsFolder(String f) {
        assetsFolder = f;
    }

    public void terminate()
    {
        if(AudioPresenter.currentSound != null)
            AudioPresenter.currentSound.music.stop();
        appClosed = true;
    }

    public String getSourceURL() {
        return null;
    }

    public void startApp() {
    }

    public void run() {

    }
}
