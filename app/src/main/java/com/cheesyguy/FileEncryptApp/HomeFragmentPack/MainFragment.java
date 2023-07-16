package com.cheesyguy.FileEncryptApp.HomeFragmentPack;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheesyguy.FileEncryptApp.R;

public class MainFragment extends Fragment {
    CardView encryptButton, decryptButton, sandboxButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        encryptButton = root.findViewById(R.id.encButton);
        decryptButton = root.findViewById(R.id.decButton);
        sandboxButton = root.findViewById(R.id.sandbox);


        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(encryptButton, encryptButton.getTransitionName())
                        .build();
                Navigation.findNavController(root)
                        .navigate(
                        R.id.action_mainFragment_to_encryptFragment,
                        null,
                        null,
                        extras
                );
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        sandboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return root;
    }
}