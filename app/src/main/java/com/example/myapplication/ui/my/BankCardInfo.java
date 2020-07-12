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
import org.w3c.dom.Attr;
import org.xmlpull.v1.XmlPullParser;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

import static com.example.myapplication.config.Config.getFullUrl;

public class BankCardInfo extends Activity {
    private Context context;
    private final String mGetBankCardByTokenUrl = getFullUrl("/bankCard/getBankCard");

    //是否第一次加载
    private boolean isFirstLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_card_info);
        LinearLayout bankCardInfo = findViewById(R.id.bank_card_info);

        context = getApplicationContext();

        CustomTitleBar titleBar = findViewById(R.id.titlebar_bank);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankCardInfo.this.finish();
            }
        });

        String token = SharedPreferencesUtil.getString(context,"token",null);
        if(token == null){
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BankCardInfo.this, LoginActivity.class));
        }
        Map<String,String> postParams = new HashMap<>();
        postParams.put("token",token);
        String result = HttpRequestUtil.sendPost(mGetBankCardByTokenUrl,postParams,token);
        /*重要，需要判断token的位置*/
        if(result==null|| result.equals("")){
            //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
            Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(BankCardInfo.this, LoginActivity.class));
        }
        try {
            JSONArray message = JsonUtil.stringToJsonObject(result).getJSONArray("bankCards");
            if (message != null){
                for(int i = 0 ; i <  message.length() ; i++){
                    JSONObject jsonObject = (JSONObject) message.get(i);
                    String bankName = jsonObject.getString("bankName");
                    String cardNumber = jsonObject.getString("cardNumber");
                    Attributes attributes = new Attributes();
                    attributes.putValue("bank_title",bankName + "(" + cardNumber.substring(0,6)+"************" + ")");
                    attributes.putValue("cardNumber",cardNumber);
                    int source_id;
                    if(bankName.equals("农业银行")){
                        source_id = R.drawable.nongyebank;
                    }else {
                        source_id = R.drawable.zhaoshbank;
                    }
                    attributes.putValue("drawableId",String.valueOf(source_id));
                    CardInfo cardInfo = new CardInfo(context,attributes);
                    bankCardInfo.addView(cardInfo);

                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

        Button button = findViewById(R.id.titlebar_bank).findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BankCardInfo.this, AddBankCard.class);
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
