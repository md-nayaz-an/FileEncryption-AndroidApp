package com.cheesyguy.FileEncryptApp.HomeFragmentPack;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheesyguy.FileEncryptApp.EncryptionHelper.Encryption;
import com.cheesyguy.FileEncryptApp.R;
import com.molihuan.pathselector.PathSelector;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.BasePathSelectFragment;
import com.molihuan.pathselector.fragment.impl.PathSelectFragment;
import com.molihuan.pathselector.listener.FileItemListener;
import com.molihuan.pathselector.utils.MConstants;
import com.molihuan.pathselector.utils.Mtools;

import java.io.File;

public class EncryptFragment extends Fragment {


    RelativeLayout rel;
    PathSelectFragment selectFragment;
    Button filePickButton;
    String filePath;

    public static final int FILE_PICKER_REQUEST_CODE = 989;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_encrypt, container, false);

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
                NavHostFragment.findNavController(EncryptFragment.this).popBackStack();
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

                selectFragment = PathSelector.build(EncryptFragment.this, MConstants.BUILD_FRAGMENT)
                        .setMaxCount(1)
                        .setFrameLayoutId(R.id.file_pick_frame)
                        .setFileItemListener(
                                new FileItemListener() {
                                    @Override
                                    public boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
                                        Mtools.toast("you clicked path:\n" + file.getPath());
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


        Button EncryptButton = root.findViewById(R.id.encryptButton);

        TextView passwordText = (TextView) root.findViewById(R.id.password);
        TextView passwordTextAgain = (TextView) root.findViewById(R.id.passwordAgain);

        String[] password = new String[2];

        Encryption enc = new Encryption();

        EncryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                password[0] = passwordText.getText().toString();
                password[1] = passwordTextAgain.getText().toString();

                if(!password[0].equals(password[1])) {
                    passwordText.setText("");
                    passwordTextAgain.setText("");
                    Toast.makeText(getActivity(), "Passwords don't match. Please try again", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(enc.encrypt(filePath, password[0]))
                        Toast.makeText(getActivity(), "File Encrypted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Encryption Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }

    private void setFilePath(String path) {
        this.filePath = path;
    }


}