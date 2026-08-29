package io.netbird.client.ui.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import io.netbird.client.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        try {
            String packageName = requireContext().getPackageName();
            String versionName = requireContext()
                    .getPackageManager()
                    .getPackageInfo(packageName, 0).versionName;

            binding.txtVersionString.setText(versionName);
        } catch (Exception e) {
            binding.txtVersionString.setText("unknown");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
