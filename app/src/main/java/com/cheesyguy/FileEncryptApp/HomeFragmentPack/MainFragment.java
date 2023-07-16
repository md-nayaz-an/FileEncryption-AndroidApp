package com.cheesyguy.FileEncryptApp.HomeFragmentPack;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheesyguy.FileEncryptApp.R;

public class MainFragment extends Fragment {
    CardView encryptButton, decryptButton, sandboxButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transition));

        encryptButton = root.findViewById(R.id.encButton);
        decryptButton = root.findViewById(R.id.decButton);
        sandboxButton = root.findViewById(R.id.sandbox);

        TextView encryptTitle = root.findViewById(R.id.encryptTitle);
        ImageView encryptIcon = root.findViewById(R.id.encryptIcon);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(encryptButton, encryptButton.getTransitionName())
                        .addSharedElement(encryptTitle, encryptTitle.getTransitionName())
                        .addSharedElement(encryptIcon, encryptIcon.getTransitionName())
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

        TextView decryptTitle = root.findViewById(R.id.decryptTitle);
        ImageView decryptIcon = root.findViewById(R.id.decryptIcon);

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                        .addSharedElement(decryptButton, decryptButton.getTransitionName())
                        .addSharedElement(decryptTitle, decryptTitle.getTransitionName())
                        .addSharedElement(decryptIcon, decryptIcon.getTransitionName())
                        .build();
                Navigation.findNavController(root)
                        .navigate(
                                R.id.action_mainFragment_to_decryptFragment,
                                null,
                                null,
                                extras
                        );
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