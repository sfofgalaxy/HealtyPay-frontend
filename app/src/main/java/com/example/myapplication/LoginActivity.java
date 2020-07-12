package com.example.myapplication;


import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.example.myapplication.utils.HttpRequestUtil;

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
    private EditText mPhoneView;
    private EditText mCaptchaView;
    private Button mLoginButton;
    private Button mSendCaptchaButton;
    private TimeCount time;
    //用于发送完验证码倒计时的类
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            mSendCaptchaButton.setBackgroundColor(Color.parseColor("#FFD700"));
            mSendCaptchaButton.setClickable(false);
            mSendCaptchaButton.setText("("+millisUntilFinished / 1000 +") 秒");
        }
        @Override
        public void onFinish() {
            mSendCaptchaButton.setText("重新获取");
            mSendCaptchaButton.setClickable(true);
            mSendCaptchaButton.setBackgroundColor(Color.parseColor("#D7D7D7"));
        }
    }

    private View focusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                        attemptLogin(mPhoneView.getText().toString(),mCaptchaView.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
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
                if(attemptSendCaptcha(mPhoneView.getText().toString())){
                    time.start();
                }
            }
        });

//        //尝试使用 cookie中的token 自动登录
//        SharedPreferences sp = getSharedPreferences(getString(R.string.cookie_preference_file), MODE_PRIVATE);
//        String localCookieStr = sp.getString("token", "");
//        if(! localCookieStr.isEmpty()) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }
    }

    //尝试发送验证码
    private boolean attemptSendCaptcha(String phone) {
        Map<String,String> postParams = new HashMap<>();
        postParams.put("phone",phone);
        return HttpRequestUtil.sendPost(mSendCaptchaUrl, postParams) != null;
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

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String phone,String captcha) throws JSONException {
        boolean cancel = checkForm();
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
           Map<String,String> postParams = new HashMap<>();
            //发起登录或注册请求
            postParams.put("phone",phone);
            postParams.put("captcha",captcha);
            String result =  HttpRequestUtil.sendPost(mLoginUrl, postParams);
             if(result==null){
                 System.out.println("登录后输出了null");
             }else {
                 //{"state":true,"message":"c27a8b436fde08f5471cb7b3085de7f8"}
                 System.out.println(result);
             }

        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.length()==11;
    }

    private boolean isCaptchaValid(String captcha) {
        return captcha.length() == 6;
    }


}

