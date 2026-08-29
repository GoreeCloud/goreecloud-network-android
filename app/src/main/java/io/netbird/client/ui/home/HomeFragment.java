package io.netbird.client.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netbird.client.PlatformUtils;
import io.netbird.client.R;
import io.netbird.client.ServiceAccessor;
import io.netbird.client.StateListener;
import io.netbird.client.StateListenerRegistry;
import io.netbird.client.databinding.FragmentHomeBinding;
import io.netbird.gomobile.android.PeerInfo;
import io.netbird.gomobile.android.PeerInfoArray;

public class HomeFragment extends Fragment implements StateListener {

    private FragmentHomeBinding binding;
    private ServiceAccessor serviceAccessor;
    private StateListenerRegistry stateListenerRegistry;

    private TextView textHostname;
    private TextView textNetworkAddress;

    private Button buttonConnect;
    private ButtonAnimation buttonAnimation;
    private boolean isConnected;

    // Serializes peer-list refreshes off the UI thread. The JNI call into the
    // inherited Go networking core can take seconds during engine lifecycle work.
    private ExecutorService refreshExecutor;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ServiceAccessor) {
            serviceAccessor = (ServiceAccessor) context;
        } else {
            throw new RuntimeException(context + " must implement ServiceAccessor");
        }
        if (context instanceof StateListenerRegistry) {
            stateListenerRegistry = (StateListenerRegistry) context;
        } else {
            throw new RuntimeException(context + " must implement StateListenerRegistry");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textHostname = binding.textHostname;
        textNetworkAddress = binding.textNetworkAddress;
        TextView textConnStatus = binding.textConnectionStatus;

        updatePeerCount(0, 0);

        buttonConnect = binding.btnConnect;
        if (buttonAnimation == null) {
            buttonAnimation = new ButtonAnimation();
        }
        buttonAnimation.refresh(buttonConnect, textConnStatus);

        buttonConnect.setOnClickListener(v -> {
            if (serviceAccessor == null) return;

            if (isConnected) {
                buttonAnimation.disconnecting();
                serviceAccessor.switchConnection(false);
            } else {
                buttonAnimation.connecting();
                serviceAccessor.switchConnection(true);
            }
        });

        FrameLayout openPanelCardView = binding.peersBtn;
        openPanelCardView.setOnClickListener(v -> {
            v.clearFocus();
            BottomDialogFragment fragment = new BottomDialogFragment();
            fragment.show(getParentFragmentManager(), fragment.getTag());
        });

        if (PlatformUtils.isAndroidTV(requireContext())) {
            root.postDelayed(() -> {
                if (buttonConnect != null && buttonConnect.isEnabled()) {
                    buttonConnect.requestFocus();
                }
            }, 200);
        }

        refreshExecutor = Executors.newSingleThreadExecutor();
        stateListenerRegistry.registerServiceStateListener(this);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (buttonAnimation != null) buttonAnimation.destroy();
        stateListenerRegistry.unregisterServiceStateListener(this);
        if (refreshExecutor != null) {
            refreshExecutor.shutdown();
            refreshExecutor = null;
        }
        if (binding != null) {
            binding.peersBtn.setOnClickListener(null);
        }
        buttonConnect = null;
        binding = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        serviceAccessor = null;
    }

    @Override
    public void onEngineStarted() {
    }

    @Override
    public void onEngineStopped() {
        isConnected = false;
        if (buttonConnect != null) {
            buttonConnect.post(() -> {
                if (buttonAnimation != null) buttonAnimation.disconnected();
            });
        }
    }

    @Override
    public void onAddressChanged(String netAddr, String hostname) {
        if (textNetworkAddress == null || textHostname == null) return;
        textNetworkAddress.post(() -> textNetworkAddress.setText(netAddr));
        textHostname.post(() -> textHostname.setText(hostname));
    }

    @Override
    public void onConnected() {
        isConnected = true;
        if (buttonConnect != null) {
            buttonConnect.post(() -> {
                if (buttonAnimation != null) buttonAnimation.connected();
            });
        }
    }

    @Override
    public void onConnecting() {
        if (buttonConnect != null) {
            buttonConnect.post(() -> {
                if (buttonAnimation != null) buttonAnimation.connecting();
            });
        }
    }

    @Override
    public void onDisconnected() {
        isConnected = false;
        if (buttonConnect != null) {
            buttonConnect.post(() -> {
                if (buttonAnimation != null) buttonAnimation.disconnected();
            });
        }
        updatePeerCount(0, 0);
    }

    @Override
    public void onDisconnecting() {
        if (buttonConnect != null) {
            buttonConnect.post(() -> {
                if (buttonAnimation != null) buttonAnimation.disconnecting();
            });
        }
    }

    @Override
    public void onPeersListChanged(long numberOfPeers) {
        ExecutorService executor = refreshExecutor;
        if (executor == null || serviceAccessor == null) return;

        executor.execute(() -> {
            PeerInfoArray peersList = serviceAccessor.getPeersList();
            int connected = 0;
            for (int i = 0; i < peersList.size(); i++) {
                PeerInfo peer = peersList.get(i);
                if (Status.fromLong(peer.getConnStatus()) == Status.CONNECTED) {
                    connected++;
                }
            }
            updatePeerCount(connected, peersList.size());
        });
    }

    private void updatePeerCount(int connectedPeers, long totalPeers) {
        if (binding == null) return;
        TextView textPeersCount = binding.textOpenPanel;
        String text = getString(R.string.peers_connected, connectedPeers, totalPeers);
        textPeersCount.post(() ->
                textPeersCount.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
        );
    }
}
