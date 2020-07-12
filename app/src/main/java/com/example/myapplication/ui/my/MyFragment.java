package com.example.myapplication.ui.my;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class MyFragment extends Fragment {

    private MyViewModel myViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        final TextView textView = root.findViewById(R.id.check_message);
        textView.setText("17326000000");

        ItemGroup item = root.findViewById(R.id.ig_messagecard);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(i);
            }
        });

        return root;
    }
}