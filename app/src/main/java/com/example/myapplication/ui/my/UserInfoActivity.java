package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;

public class UserInfoActivity extends Activity {
    private Context context;
    private final String mGetUserIdByTokenUrl = getFullUrl("/user/ID");
    private final String mGetUserByTokenUrl = getFullUrl("/user");

    //是否第一次加载
    private boolean isFirstLoading = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.fragment_userinfo);

        ItemGroup item1 = findViewById(R.id.ig_name);
        item1.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);

        ItemGroup item2 = findViewById(R.id.ig_card);

        ItemGroup item3 = findViewById(R.id.ig_number);
        item3.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);

        String token = SharedPreferencesUtil.getString(context,"token",null);
        Map<String,String> getParams = new HashMap<>();
        getParams.put("token",token);
        String result = HttpRequestUtil.sendGet(mGetUserIdByTokenUrl,getParams,token);
        try {
            String message = JsonUtil.stringToJsonObject(result).getString("message");
            final String userInfo = HttpRequestUtil.sendGet(mGetUserByTokenUrl, getParams, token);
            if (message == "null"){
                TextView textView = item2.findViewById(R.id.content_edt);
                textView.setText("未绑定");
                textView.setTextColor(Color.rgb(255,0,0));
                String number = JsonUtil.stringToJsonObject(userInfo).getJSONObject("user").getString("phone");
                TextView textView3 = item3.findViewById(R.id.content_edt);
                textView3.setText(number);
                item2.findViewById(R.id.jt_right_iv).setVisibility(View.VISIBLE);

                item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(UserInfoActivity.this, BandMessageCard.class);
                        startActivity(i);
                    }
                });
            }else {

                String name = JsonUtil.stringToJsonObject(userInfo).getJSONObject("user").getString("name");
                String card = JsonUtil.stringToJsonObject(userInfo).getJSONObject("user").getString("id");
                String number = JsonUtil.stringToJsonObject(userInfo).getJSONObject("user").getString("phone");
                TextView textView1 = item1.findViewById(R.id.content_edt);
                TextView textView2 = item2.findViewById(R.id.content_edt);
                TextView textView3 = item3.findViewById(R.id.content_edt);
                textView1.setText(name);
                textView2.setText(card);
                textView3.setText(number);
                item2.findViewById(R.id.jt_right_iv).setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        CustomTitleBar titleBar = findViewById(R.id.titlebar_info);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
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
