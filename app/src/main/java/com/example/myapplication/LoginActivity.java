package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;



/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
    private final String mLoginUrl = getFullUrl("/user/login");
    private final String mSendCaptchaUrl = getFullUrl("/user/sendCaptcha");
    private final String mGetUserByTokenUrl = getFullUrl("/user");
    private EditText mPhoneView;
    private EditText mCaptchaView;
    private Button mLoginButton;
    private Button mSendCaptchaButton;
    private TimeCount time;
    private Context context;
    private View focusView;
    //用于发送完验证码倒计时的类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            mSendCaptchaButton.setTextColor(Color.parseColor("#FFFFFF"));
            mSendCaptchaButton.setClickable(false);
            mSendCaptchaButton.setText("("+millisUntilFinished / 1000 +") s");
        }
        @Override
        public void onFinish() {
            mSendCaptchaButton.setText("重新获取");
            mSendCaptchaButton.setClickable(true);
            mSendCaptchaButton.setBackgroundColor(Color.parseColor("#D7D7D7"));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //每个activity需获取
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);
        mPhoneView = findViewById(R.id.edit_phone_number);
        mCaptchaView = findViewById(R.id.edit_captcha);
        //输入完成验证码后点击回车键触发
        mCaptchaView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login_button || id == EditorInfo.IME_NULL) {
                try {
                    attemptLogin(mPhoneView.getText().toString(), mCaptchaView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
            }
        });
        //点击登录按钮后触发
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin(mPhoneView.getText().toString(),mCaptchaView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        time = new TimeCount(60000, 1000);
        //点击发送验证码按钮
        mSendCaptchaButton = findViewById(R.id.send_captcha);
        mSendCaptchaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptSendCaptcha(mPhoneView.getText().toString());
                    Toast.makeText(context, "获取验证码成功", Toast.LENGTH_SHORT).show();
                    time.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //尝试使用存储的token自动登录
        String token = SharedPreferencesUtil.getString(context,"token",null);
        if(token!=null&&!token.isEmpty()) {
            Map<String,String> param = new HashMap<>();
            param.put("key","value");
            try {
                if(JsonUtil.stringToJsonObject(HttpRequestUtil.sendGet(mGetUserByTokenUrl,param,token)).getBoolean("state")){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    SharedPreferencesUtil.remove(context,"token");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //尝试发送验证码
    private void attemptSendCaptcha(String phone) throws JSONException {
        Map<String,String> postParams = new HashMap<>();
        postParams.put("phone",phone);
        String result = HttpRequestUtil.sendPost(mSendCaptchaUrl,postParams);
        //获取state判断发送是否成功
        JsonUtil.stringToJsonObject(result).getBoolean("state");
    }

    boolean checkForm() {
        //Reset errors
        mPhoneView.setError(null);
        mCaptchaView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String captcha = mCaptchaView.getText().toString();

        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(captcha) && !isCaptchaValid(captcha)) {
            mCaptchaView.setError(getString(R.string.error_invalid_captcha));
            focusView = mCaptchaView;
            cancel = true;
        }

        // 检查phone的长度
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }
        return cancel;
    }

    private void attemptLogin(String phone, String captcha) throws JSONException {
        boolean cancel = checkForm();
        if (cancel) {
            focusView.requestFocus();
        } else {
           Map<String,String> postParams = new HashMap<>();
            //发起登录或注册请求
            postParams.put("phone",phone);
            postParams.put("captcha",captcha);
            String result =  HttpRequestUtil.sendPost(mLoginUrl, postParams);
             if(result==null||result.equals("")){
                 Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
             }else if(!JsonUtil.stringToJsonObject(result).getBoolean("state")){
                 Toast.makeText(this, JsonUtil.stringToJsonObject(result).getString("message"), Toast.LENGTH_SHORT).show();
             }else {
                 Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                 SharedPreferencesUtil.putString(context,"token",JsonUtil.stringToJsonObject(result).getString("message"));
                 System.out.println("从Json:"+JsonUtil.stringToJsonObject(result).getString("message"));
                 startActivity(new Intent(LoginActivity.this, MainActivity.class));
             }

        }
    }

    private boolean isPhoneValid(String phone) {
        boolean valid = true;
        if(phone.length()!=11){
            valid=false;
        }else{
            char[] chars = phone.toCharArray();
            for(char a:chars){
                if (a < '0' || a > '9') {
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    private boolean isCaptchaValid(String captcha) {
        boolean valid = true;
        if(captcha.length()!=6){
            valid=false;
        }else{
            char[] chars = captcha.toCharArray();
            for(char a:chars){
                if(a<'0'||a>'9'){
                    valid=false;
                }
            }
        }
        return valid;
    }
}

