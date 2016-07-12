package devintensive.softdesign.com.devintensive.data.managers;

import android.content.Context;

import devintensive.softdesign.com.devintensive.data.network.RestService;
import devintensive.softdesign.com.devintensive.data.network.ServiceGenerator;
import devintensive.softdesign.com.devintensive.data.network.req.UserLoginReq;
import devintensive.softdesign.com.devintensive.data.network.res.UserModelRes;
import devintensive.softdesign.com.devintensive.utils.DevintensiveApplication;
import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE = null;
    private Context mContext;
    private PreferencesManager mPreferencesManager;

    private RestService mRestService;

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mContext = DevintensiveApplication.getContext();
        this.mRestService = ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    public Call<UserModelRes> loginUser(String lastModified, UserLoginReq userLoginReq) {
        return mRestService.loginUser(lastModified, userLoginReq);
    }


}
