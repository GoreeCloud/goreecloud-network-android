package io.netbird.client.ui.home;

import android.widget.Button;
import android.widget.TextView;

/**
 * Lightweight native connection-state presenter for GoreeCloud Network.
 *
 * The upstream Android client used a Lottie animation as both the primary
 * connection control and its state machine. GoreeCloud keeps the networking
 * callbacks but presents them with ordinary Android widgets so the core
 * connect/disconnect experience does not depend on a third-party animation
 * runtime or bundled animation assets.
 */
class ButtonAnimation {
    private Button button;
    private TextView textConnStatus;

    private enum AnimationState {
        DISCONNECTED("Disconnected", "Connect", true, 1.0f),
        CONNECTING("Connecting…", "Connecting…", false, 0.72f),
        CONNECTED("Connected", "Disconnect", true, 1.0f),
        DISCONNECTING("Disconnecting…", "Disconnecting…", false, 0.72f);

        private final String statusText;
        private final String buttonText;
        private final boolean enabled;
        private final float alpha;

        AnimationState(String statusText, String buttonText, boolean enabled, float alpha) {
            this.statusText = statusText;
            this.buttonText = buttonText;
            this.enabled = enabled;
            this.alpha = alpha;
        }
    }

    private AnimationState currentState = AnimationState.DISCONNECTED;

    public void refresh(Button buttonConnect, TextView textConnStatus) {
        button = buttonConnect;
        this.textConnStatus = textConnStatus;
        render(currentState);
    }

    public void destroy() {
        button = null;
        textConnStatus = null;
    }

    public void connecting() {
        if (currentState == AnimationState.DISCONNECTING) return;
        currentState = AnimationState.CONNECTING;
        render(currentState);
    }

    public void connected() {
        currentState = AnimationState.CONNECTED;
        render(currentState);
    }

    public void disconnecting() {
        currentState = AnimationState.DISCONNECTING;
        render(currentState);
    }

    public void disconnected() {
        currentState = AnimationState.DISCONNECTED;
        render(currentState);
    }

    private void render(AnimationState state) {
        if (button != null) {
            button.post(() -> {
                if (button == null) return;
                button.setText(state.buttonText);
                button.setEnabled(state.enabled);
                button.setAlpha(state.alpha);
            });
        }
        if (textConnStatus != null) {
            textConnStatus.post(() -> {
                if (textConnStatus != null) {
                    textConnStatus.setText(state.statusText);
                }
            });
        }
    }
}
