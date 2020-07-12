package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import static com.example.myapplication.config.Config.getFullUrl;

public class NFCCardInfo extends Activity {
    private Context context;
    private final String mGetNfcCardByTokenUrl = getFullUrl("/NFC/getNFCCard");

    //是否第一次加载
    private boolean isFirstLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_card_info);
        LinearLayout bankCardInfo = findViewById(R.id.bank_card_info);
        context = getApplicationContext();
        CustomTitleBar titleBar = findViewById(R.id.nfc_card);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NFCCardInfo.this.finish();
            }
        });

        String token = SharedPreferencesUtil.getString(context,"token",null);
        if(token == null){
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NFCCardInfo.this, LoginActivity.class));
        }
        Map<String,String> postParams = new HashMap<>();
        postParams.put("token",token);
        String result = HttpRequestUtil.sendPost(mGetNfcCardByTokenUrl,postParams,token);
        /*重要，需要判断token的位置*/
        if(result==null|| result.equals("")){
            //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NFCCardInfo.this, LoginActivity.class));
        }
        try {
            JSONArray message = JsonUtil.stringToJsonObject(result).getJSONArray("nfcCards");
            if (message != null){
                for(int i = 0 ; i <  message.length() ; i++){
                    JSONObject jsonObject = (JSONObject) message.get(i);
                    Attributes attributes = new Attributes();
                    int source_id;
                    if(i==1){
                        source_id = R.drawable.bus2;
                    }else {
                        source_id = R.drawable.subway;
                    }
                    attributes.putValue("drawableId",String.valueOf(source_id));
                    CardInfo cardInfo = new CardInfo(context,attributes);
                    bankCardInfo.addView(cardInfo);

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.nfc_card).findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NFCCardInfo.this, BindNfcCard.class);
                startActivity(i);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            this.recreate();
        }

        isFirstLoading = false;
    }
}