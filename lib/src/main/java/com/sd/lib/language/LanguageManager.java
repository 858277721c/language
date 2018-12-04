package com.sd.lib.language;

import android.content.Context;

public class LanguageManager
{
    private static LanguageManager sInstance;

    private Context mContext;

    private LanguageManager()
    {
    }

    public static LanguageManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (LanguageManager.class)
            {
                if (sInstance == null)
                    sInstance = new LanguageManager();
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context)
    {
        if (mContext == null)
            mContext = context.getApplicationContext();
    }

    public Context getContext()
    {
        if (mContext == null)
            throw new NullPointerException("you must call init method before this");
        return mContext;
    }
}
