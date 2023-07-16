package com.cheesyguy.FileEncryptApp.HomeFragmentPack;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheesyguy.FileEncryptApp.EncryptionHelper.Decryption;
import com.cheesyguy.FileEncryptApp.EncryptionHelper.Encryption;
import com.cheesyguy.FileEncryptApp.R;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

public class DecryptFragment extends Fragment {

    PathSelectFragment selectFragment;
    Button filePickButton;
    String filePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_decrypt, container, false);

        setSharedElementEnterTransition(new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform()));
        setSharedElementReturnTransition(new TransitionSet()
                .addTransition(new ChangeBounds())
                .addTransition(new ChangeTransform()));

        setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.change_transition));


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(DecryptFragment.this).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


        LinearLayout cardContainer = root.findViewById(R.id.cardContainer);

        filePickButton = root.findViewById(R.id.filePickButton);

        TextView[] FileInfo = new TextView[3];
        FileInfo[0] = (TextView) root.findViewById(R.id.fileNameText);
        FileInfo[1] = (TextView) root.findViewById(R.id.fileType);
        FileInfo[2] = (TextView) root.findViewById(R.id.fileSize);


        filePickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectFragment = PathSelector.build(DecryptFragment.this, MConstants.BUILD_FRAGMENT)
                        .setMaxCount(1)
                        .setFrameLayoutId(R.id.file_pick_frame)
                        .setShowFileTypes("aes")
                        .setSelectFileTypes("aes")
                        .setFileItemListener(
                                new FileItemListener() {
                                    @Override
                                    public boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                        setFilePath(file.getPath());

                                        // remove the file pick layout after selecting a file
                                        ((ViewGroup) root.findViewById(R.id.file_pick_frame)).removeAllViews();

                                        if(cardContainer.getVisibility() == View.GONE) {
                                            cardContainer.setVisibility(View.VISIBLE);
                                        }

                                        FileInfo[0].setText(file.getName());
                                        FileInfo[1].setText(file.getFileExtension());
                                        FileInfo[2].setText(String.format("%.2f",(double)file.getSize() / 1000000.00) + "MB");


                                        return false;
                                    }
                                }
                        )
                        .show();

            }
        });


        Button DecryptButton = root.findViewById(R.id.decryptButton);

        TextView passwordText = (TextView) root.findViewById(R.id.password);

        String[] password = new String[1];

        Decryption dec = new Decryption();

        DecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password[0] = passwordText.getText().toString();

                String path = dec.decrypt(filePath, password[0]);

                if(!path.equals("false")) {
                    Toast.makeText(getActivity(), "Decrypt Successfull", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Decryption Error", Toast.LENGTH_SHORT).show();
                }
            });

        return root;
    }

    private void setFilePath(String path) {
        this.filePath = path;
    }
}