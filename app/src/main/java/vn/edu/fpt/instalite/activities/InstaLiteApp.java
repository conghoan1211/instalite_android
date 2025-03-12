package vn.edu.fpt.instalite.activities;

import android.app.Application;

import vn.edu.fpt.instalite.sessions.DataLocalManager;

public class InstaLiteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(this);
    }
}

