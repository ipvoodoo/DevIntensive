package devintensive.softdesign.com.devintensive.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class DevintensiveApplication extends Application {

    public static SharedPreferences sSharedPreferences;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
//        Получаем SharedPreferences
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
    public static Context getContext(){
        return sContext;
    }
}
