package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import java.time.Instant;

public class BankCardInfo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_info);

        Button button = findViewById(R.id.titlebar_bank).findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BankCardInfo.this, AddBankCard.class);
                startActivity(i);
            }
        });
    }
}
