package com.example.myapplication.ui.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;

public class UserInfoActivity extends Activity {
    private Context context;
    private final String mGetUserIdByTokenUrl = getFullUrl("/user/Id");
    private final String mGetUserByTokenUrl = getFullUrl("/user");
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
        if (result == null){
            TextView textView = item2.findViewById(R.id.content_edt);
            textView.setText("未绑定");
            textView.setTextColor(0xff0000);
            item2.findViewById(R.id.jt_right_iv).setVisibility(View.VISIBLE);
        }else{
            try {
                String userInfo = HttpRequestUtil.sendGet(mGetUserByTokenUrl,getParams,token);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserInfoActivity.this, BandMessageCard.class);
                startActivity(i);
            }
        });

        CustomTitleBar titleBar = findViewById(R.id.titlebar_info);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
    }
}
