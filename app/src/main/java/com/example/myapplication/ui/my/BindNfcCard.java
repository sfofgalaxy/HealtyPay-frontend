package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.NFCUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;

public class BindNfcCard extends Activity {
    private Context context;
    private final String mBindNFCUrl = getFullUrl("/NFC/saveTag");
    public final NFCUtil nfcUtil = NFCUtil.getInstance();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_card);
        context = getApplicationContext();
        String content = nfcUtil.read();
        if (content != null)
            Toast.makeText(BindNfcCard.this, "NFC中包含的内容:" + content, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(BindNfcCard.this, "未检测到NFC芯片", Toast.LENGTH_SHORT).show();

        CustomTitleBar titleBar = findViewById(R.id.bind_nfc_card);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindNfcCard.this.finish();
            }
        });

        Button button_band = findViewById(R.id.bank_bank_button);
        button_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = "tag_test";
                String token = SharedPreferencesUtil.getString(context,"token",null);

                /*重要，需要判断token的位置*/
                if(token==null){
                    //整明本地缓存中没有token，需要跳转到登录页面
                    Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BindNfcCard.this, LoginActivity.class));
                }

                Map<String,String> postParams = new HashMap<>();
                postParams.put("tag",tag);
                String result = HttpRequestUtil.sendPost(mBindNFCUrl,postParams,token);
                /*重要，需要判断token的位置*/
                if(result==null|| result.equals("")){
                    //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
                    Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BindNfcCard.this, LoginActivity.class));
                }
                try {
                    boolean status = JsonUtil.stringToJsonObject(result).getBoolean("state");
                    if (status){
                        Toast.makeText(BindNfcCard.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(BindNfcCard.this, "绑定失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BindNfcCard.this.finish();
            }
        });
    }
}
