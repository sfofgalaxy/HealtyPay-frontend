package com.example.myapplication.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;


public class MyFragment extends Fragment {

    private MyViewModel myViewModel;
    private Context context;
    private final String mGetUserIdByTokenUrl = getFullUrl("/user/ID");
    private final String mGetUserByTokenUrl = getFullUrl("/user");
    private final String mLogoutByTokenUrl = getFullUrl("/user/logout");
    //是否第一次加载
    private boolean isFirstLoading = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        myViewModel =
                ViewModelProviders.of(this).get(MyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my, container, false);
        final TextView textView = root.findViewById(R.id.check_message);
        String token = SharedPreferencesUtil.getString(context,"token",null);
        /*重要，需要判断token的位置*/
        if(token==null){
            //整明本地缓存中没有token，需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        Map<String,String> getParams = new HashMap<>();
        getParams.put("token",token);
        String result = HttpRequestUtil.sendGet(mGetUserIdByTokenUrl,getParams,token);

        /*重要，需要判断token的位置*/
        if(result==null|| result.equals("")){
            //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        try {
            String message = JsonUtil.stringToJsonObject(result).getString("message");
            if (message == "null"){
                textView.setText("请先验证身份信息");
            }else{
                String userInfo = HttpRequestUtil.sendGet(mGetUserByTokenUrl,getParams,token);
                String name = JsonUtil.stringToJsonObject(userInfo).getJSONObject("user").getString("name");
                textView.setText(name);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        ItemGroup item1 = root.findViewById(R.id.ig_messagecard);
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(i);
            }
        });

        ItemGroup item2 = root.findViewById(R.id.ig_moneycard);
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BankCardInfo.class);
                startActivity(i);
            }
        });

        ItemGroup item3 = root.findViewById(R.id.ig_buscard);
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NFCCardInfo.class);
                startActivity(i);
            }
        });

        ItemGroup item4 = root.findViewById(R.id.ig_bill);
        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), BillActivity.class);
                startActivity(i);
            }
        });

        ItemGroup item5 = root.findViewById(R.id.ig_feedback);
        item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(i);
            }
        });

        Button button = root.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = SharedPreferencesUtil.getString(context,"token",null);
                if(token == null){
                    Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> postParams = new HashMap<>();
                postParams.put("token",token);
                String result = HttpRequestUtil.sendPost(mLogoutByTokenUrl,postParams,token);
                /*重要，需要判断token的位置*/
                if(result==null|| result.equals("")){
                    //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
                    Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Boolean state = JsonUtil.stringToJsonObject(result).getBoolean("state");
                    if(state){
                        Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }else{
                        Toast.makeText(context, "退出失败", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            getActivity().recreate();
            this.onHiddenChanged(false);
        }

        isFirstLoading = false;
    }
}