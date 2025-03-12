package vn.edu.fpt.instalite.sessions;

import android.content.Context;

import com.google.gson.Gson;

import vn.edu.fpt.instalite.models.User;

public class DataLocalManager {

    private static final String USER_SHARED_PREFERENCES = "data_user";
    private static DataLocalManager instance;
    private MySharedPreferences mySharedPreferences;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    public static DataLocalManager gI() {
        if (instance == null) instance = new DataLocalManager();
        return instance;
    }

    public static void setUser(User user) {
        Gson gson = new Gson();
        String strJsonUser = gson.toJson(user);
        DataLocalManager.gI().mySharedPreferences.putStringValue(USER_SHARED_PREFERENCES, strJsonUser);
    }

    public static User getUser() {
        String strJsonUser = DataLocalManager.gI().mySharedPreferences.getStringValue(USER_SHARED_PREFERENCES);
        Gson gson = new Gson();
        return gson.fromJson(strJsonUser, User.class);
    }
    public static void clearUser() {
        DataLocalManager.gI().mySharedPreferences.putStringValue(USER_SHARED_PREFERENCES, null);
    }

}
