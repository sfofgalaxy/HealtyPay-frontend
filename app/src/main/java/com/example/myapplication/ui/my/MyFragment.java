package com.example.myapplication.ui.my;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;


public class MyFragment extends Fragment {

    private MyViewModel myViewModel;
    private Context context;
    private final String mGetUserIdByTokenUrl = getFullUrl("/user/ID");
    private final String mGetUserByTokenUrl = getFullUrl("/user");
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
        Map<String,String> getParams = new HashMap<>();
        getParams.put("token",token);
        String result = HttpRequestUtil.sendGet(mGetUserIdByTokenUrl,getParams,token);
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