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
}
