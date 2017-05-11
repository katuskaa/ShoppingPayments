package katka.shoppingpayments.helpers.shared_preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesHelper {

    public static void saveUserUid(Context context, String userUid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstants.USER_UID, userUid);
        editor.apply();
    }

    public static String getUserUid(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstants.USER_UID, "");
    }

    public static void saveUserNickname(Context context, String userUid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstants.USER_NICKNAME, userUid);
        editor.apply();
    }

    public static String getUserNickname(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstants.USER_NICKNAME, "");
    }

    public static void saveUserEmail(Context context, String userUid) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferencesConstants.USER_EMAIL, userUid);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(SharedPreferencesConstants.USER_EMAIL, "");
    }

}

