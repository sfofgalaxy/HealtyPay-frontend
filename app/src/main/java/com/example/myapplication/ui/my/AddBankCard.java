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
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;

public class AddBankCard extends Activity {
    private Context context;
    private final String mPutUserIdByTokenUrl = getFullUrl("/bankCard/bindBankCard");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_card);

        context = getApplicationContext();

        CustomTitleBar titleBar = findViewById(R.id.titlebar_add_bank);
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBankCard.this.finish();
            }
        });

        Button button_band = findViewById(R.id.bank_bank_button);
        button_band.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText bank_number = findViewById(R.id.edit_bank_number);
                EditText bank_password = findViewById(R.id.edit_bank_password);
                EditText bank_phone = findViewById(R.id.edit_bank_phone);
                String bankNumber = bank_number.getText().toString();
                String password = bank_password.getText().toString();
                String phone = bank_phone.getText().toString();
                if(isCardNumberValid(bankNumber) && isPhoneValid(phone) && isPhoneValid(password)){
                    Toast.makeText(AddBankCard.this, "请输入正确的信息", Toast.LENGTH_SHORT).show();
                }else{
                    String token = SharedPreferencesUtil.getString(context,"token",null);
                    /*重要，需要判断token的位置*/
                    if(token==null){
                        //整明本地缓存中没有token，需要跳转到登录页面
                        Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBankCard.this, LoginActivity.class));
                    }
                    Map<String,String> postParams = new HashMap<>();
                    postParams.put("token",token);
                    postParams.put("cardNumber",bankNumber);
                    postParams.put("password",password);
                    postParams.put("phone",phone);
                    String result = HttpRequestUtil.sendPost(mPutUserIdByTokenUrl,postParams,token);
                    if(result==null|| result.equals("")){
                        //证明此时token虽然在手机缓存有但已经过期，同样需要跳转到登录页面
                        Toast.makeText(context, "登录已过期", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddBankCard.this, LoginActivity.class));
                    }
                    try {
                        Boolean status = JsonUtil.stringToJsonObject(result).getBoolean("state");
                        if (status){
                            Toast.makeText(AddBankCard.this, "绑定成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddBankCard.this, "绑定失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                AddBankCard.this.finish();
            }
        });
    }

    private boolean isCardNumberValid(String number) {
        boolean valid = true;
        if(number.length() >= 18 && number.length() <=21){
            valid=false;
        }else{
            char[] chars = number.toCharArray();
            for(char a:chars){
                if(a<'0'||a>'9'){
                    valid=false;
                }
            }
        }
        return valid;
    }

    private boolean isPhoneValid(String phone) {
        boolean valid = true;
        if(phone.length()!=11){
            valid=false;
        }else{
            char[] chars = phone.toCharArray();
            for(char a:chars){
                if(a<'0'||a>'9'){
                    valid=false;
                }
            }
        }
        return valid;
    }
    private boolean isPasswordValid(String password) {
        boolean valid = true;
        if(password.length()!=6){
            valid=false;
        }else{
            char[] chars = password.toCharArray();
            for(char a:chars){
                if(a<'0'||a>'9'){
                    valid=false;
                }
            }
        }
        return valid;
    }
}
