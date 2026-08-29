package io.netbird.client.tool;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.netbird.gomobile.android.Android;

@RunWith(AndroidJUnit4.class)
public class EnvVarPackagerInstrumentedTest {
    private Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @After
    public void tearDown() {
        getContext().getSharedPreferences("netbird", Context.MODE_PRIVATE).edit().clear().apply();
    }

    @Test
    public void shouldReturnEnvironmentVariables() {
        var preferences = new Preferences(getContext());
        var environmentVariables = EnvVarPackager.getEnvironmentVariables(preferences);

        Assert.assertNotNull(environmentVariables);
        var forceRelay = environmentVariables.get(Android.getEnvKeyNBForceRelay());
        var variableNotPresentInList = environmentVariables.get("UNKNOWN_VAR");
        var emptyString = "";

        Assert.assertNotEquals(emptyString, forceRelay);
        Assert.assertEquals(emptyString, variableNotPresentInList);
    }

    @Test
    public void obfuscationModeRequestsExactConduitTransportAndRelayPath() {
        var preferences = new Preferences(getContext());
        preferences.disableForcedRelayConnection();
        preferences.enableObfuscationMode();

        var environmentVariables = EnvVarPackager.getEnvironmentVariables(preferences);

        Assert.assertEquals(
                Android.getConduitPaddedWSSTransportID(),
                environmentVariables.get(Android.getEnvKeyNBRelayTransport())
        );
        Assert.assertEquals("true", environmentVariables.get(Android.getEnvKeyNBForceRelay()));
        Assert.assertFalse(
                "Obfuscation Mode must not overwrite the independent Force Relay preference",
                preferences.isConnectionForceRelayed()
        );
    }

    @Test
    public void disabledObfuscationModeDoesNotSelectPaddedTransport() {
        var preferences = new Preferences(getContext());
        preferences.disableObfuscationMode();
        preferences.disableForcedRelayConnection();

        var environmentVariables = EnvVarPackager.getEnvironmentVariables(preferences);

        Assert.assertEquals("", environmentVariables.get(Android.getEnvKeyNBRelayTransport()));
        Assert.assertEquals("false", environmentVariables.get(Android.getEnvKeyNBForceRelay()));
    }
}
