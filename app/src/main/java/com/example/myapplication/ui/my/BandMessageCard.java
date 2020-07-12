package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;

public class BandMessageCard extends Activity {
    private Context context;
    private final String mPutUserIdByTokenUrl = getFullUrl("/user/ID");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.band_message_card);


        CustomTitleBar titleBar = findViewById(R.id.titlebar_band);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BandMessageCard.this.finish();
            }
        });

        Button button_band = findViewById(R.id.band_button);
        button_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_name = findViewById(R.id.edit_band_name);
                EditText edit_card = findViewById(R.id.edit_band_card);
                String name = edit_name.getText().toString();
                String card = edit_card.getText().toString();
                if(card.length() != 18){
                    Toast.makeText(BandMessageCard.this, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
                }else{
                    String token = SharedPreferencesUtil.getString(context,"token",null);
                    Map<String,String> putParams = new HashMap<>();
                    putParams.put("token",token);
                    putParams.put("name",name);
                    putParams.put("ID",card);
                    String result = HttpRequestUtil.sendPut(mPutUserIdByTokenUrl,putParams,token);
                    try {
                        Boolean status = JsonUtil.stringToJsonObject(result).getBoolean("state");
                        if (status){
                            Toast.makeText(BandMessageCard.this, "绑定成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(BandMessageCard.this, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                BandMessageCard.this.finish();
            }
        });
    }
}