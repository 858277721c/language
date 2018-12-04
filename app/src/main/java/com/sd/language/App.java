package com.sd.language;

import android.app.Application;

import com.sd.lib.language.LanguageManager;

public class App extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        LanguageManager.getInstance().init(this);
    }
}
