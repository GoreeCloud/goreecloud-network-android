package io.netbird.client.tool;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final String keyTraceLog = "tracelog";

    private final String keyForceRelayConnection = "isConnectionForceRelayed";

    private final String keyObfuscationMode = "isObfuscationModeEnabled";

    private final SharedPreferences sharedPref;

    public Preferences(Context context) {
       sharedPref = context.getSharedPreferences("netbird", Context.MODE_PRIVATE);
    }

    public boolean isTraceLogEnabled() {
       return sharedPref.getBoolean(keyTraceLog, false);
    }
    public void enableTraceLog() {
        sharedPref.edit().putBoolean(keyTraceLog, true).apply();
    }

    public void disableTraceLog() {
        sharedPref.edit().putBoolean(keyTraceLog, false).apply();
    }

    public boolean isConnectionForceRelayed() {
        return sharedPref.getBoolean(keyForceRelayConnection, true);
    }

    public void enableForcedRelayConnection() {
        sharedPref.edit().putBoolean(keyForceRelayConnection, true).apply();
    }

    public void disableForcedRelayConnection() {
        sharedPref.edit().putBoolean(keyForceRelayConnection, false).apply();
    }

    public boolean isObfuscationModeEnabled() {
        return sharedPref.getBoolean(keyObfuscationMode, false);
    }

    public void enableObfuscationMode() {
        sharedPref.edit().putBoolean(keyObfuscationMode, true).apply();
    }

    public void disableObfuscationMode() {
        sharedPref.edit().putBoolean(keyObfuscationMode, false).apply();
    }

    public static String defaultServer() {
        return "https://netbird.goreecloud.com";
    }
}
