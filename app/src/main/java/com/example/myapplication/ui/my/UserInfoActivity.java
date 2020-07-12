package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.LoginActivity;
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

        /*重要，需要判断token的位置*/
        if(token==null){
            //整明本地缓存中没有token，需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
        }

        Map<String,String> getParams = new HashMap<>();
        getParams.put("token",token);
        String result = HttpRequestUtil.sendGet(mGetUserIdByTokenUrl,getParams,token);

        /*重要，需要判断token的位置*/
        if(result==null|| result.equals("")){
            //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
        }

        try {
            String message = JsonUtil.stringToJsonObject(result).getString("message");
            final String userInfo = HttpRequestUtil.sendGet(mGetUserByTokenUrl, getParams, token);
            if (message.equals("null")){
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
