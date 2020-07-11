package com.example.myapplication;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.utils.HttpRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.config.Config.getFullUrl;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private final String mLoginUrl = getFullUrl("/user/login");
    private final String mSendCaptchaUrl = getFullUrl("/user/sendCaptcha");
    EditText mPhoneView = findViewById(R.id.edit_phone_number);
    EditText mCaptchaView = findViewById(R.id.edit_captcha);
    private View mProgressView;
    private View mLoginFormView;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private View focusView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        Button mLoginButton = findViewById(R.id.login_button);
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

        //点击发送验证码按钮
        Button mSendCaptchaButton = findViewById(R.id.send_captcha);
        mSendCaptchaButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptSendCaptcha(mPhoneView.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        //尝试使用 cookie中的token 自动登录
//        SharedPreferences sp = getSharedPreferences(getString(R.string.cookie_preference_file), MODE_PRIVATE);
//        String localCookieStr = sp.getString("token", "");
//        if(! localCookieStr.isEmpty()) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }
    }

    //尝试发送验证码
    private void attemptSendCaptcha(String phone) throws JSONException {
        // Show a progress spinner, and kick off a background task to
        // perform the user sign up attempt.
        showProgress(true);

        Map<String,String> postParams = new HashMap<>();
        postParams.put("phone",phone);
        System.out.println(HttpRequestUtil.sendPost(mSendCaptchaUrl,postParams));
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
        if (!TextUtils.isEmpty(phone) && !isCaptchaValid(captcha)) {
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
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
           Map<String,String> postParams = new HashMap<>();

            //发起登录或注册请求
            postParams.put("phone",phone);
            postParams.put("captcha",captcha);

             String result =  HttpRequestUtil.sendPost(mLoginUrl, postParams);
             if(result==null){
                 System.out.println("登录后输出了null");
             }else {
                 System.out.println(result);
             }

        }
    }

    private boolean isPhoneValid(String phone) {
        return phone.length()==11;
    }

    private boolean isCaptchaValid(String captcha) {
        return captcha.length() == 4;
    }


}

