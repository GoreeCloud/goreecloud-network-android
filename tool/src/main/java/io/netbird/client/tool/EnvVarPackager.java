package io.netbird.client.tool;

import io.netbird.gomobile.android.Android;
import io.netbird.gomobile.android.EnvList;

public class EnvVarPackager {
    public static EnvList getEnvironmentVariables(Preferences preferences) {
        var envList = new EnvList();

        // Obfuscation Mode uses the first-party Conduit padded-WSS relay
        // transport. Force Relay remains a separate path-selection control, but
        // required Obfuscation Mode must actually traverse a relay so direct
        // connectivity cannot bypass the requested transport. Derive the env
        // value without mutating the user's independent Force Relay preference.
        boolean requireRelay = preferences.isObfuscationModeEnabled()
                || preferences.isConnectionForceRelayed();
        envList.put(Android.getEnvKeyNBForceRelay(), String.valueOf(requireRelay));

        if (preferences.isObfuscationModeEnabled()) {
            envList.put(
                    Android.getEnvKeyNBRelayTransport(),
                    Android.getConduitPaddedWSSTransportID()
            );
        }

        return envList;
    }
}
