package com.example.myapplication.ui.nfc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NFCFragment extends Fragment {

    private NFCViewModel nfcViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        nfcViewModel =
                ViewModelProviders.of(this).get(NFCViewModel.class);
        View root = inflater.inflate(R.layout.fragment_nfc, container, false);

        //NavHostController navHostController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //navHostController.navigate(R.id.secondFragment);
        //navHostController.navigateUp();
        //final TextView textView = root.findViewById(R.id.text_nfc);
        /*nfcViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}