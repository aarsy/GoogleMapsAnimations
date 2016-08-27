package com.arsy.googlemapsrippleeffect;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class App extends MultiDexApplication
{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
