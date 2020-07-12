package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.w3c.dom.Attr;
import org.xmlpull.v1.XmlPullParser;

import java.time.Instant;
import java.util.jar.Attributes;

public class BankCardInfo extends Activity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_info);
        LinearLayout bankCardInfo = findViewById(R.id.bank_card_info);

        context = getApplicationContext();

        String token = SharedPreferencesUtil.getString(context,"token",null);
        if(token == null){

        }

        Attributes attributes = new Attributes();
        attributes.putValue("bank_title","银行卡储蓄");
        int source_id = R.drawable.bill;
        attributes.putValue("drawableId",String.valueOf(source_id));
        CardInfo cardInfo = new CardInfo(this,attributes);
        bankCardInfo.addView(cardInfo);

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
