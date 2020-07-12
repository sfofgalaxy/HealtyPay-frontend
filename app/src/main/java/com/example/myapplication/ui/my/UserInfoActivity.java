package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.time.Instant;

public class UserInfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_userinfo);
        ItemGroup item1 = findViewById(R.id.ig_name);
        item1.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);

        ItemGroup item2 = findViewById(R.id.ig_card);
        item2.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);

        ItemGroup item3 = findViewById(R.id.ig_number);
        item3.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfoActivity.this, BandMessageCard.class);
                startActivity(i);
            }
        });
    }
}
