package com.example.myapplication.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;

/**
 * @Author : 张
 * @Email : manitozhang@foxmail.com
 * @Date : 2018/9/19
 *
 * 一个简单的自定义标题栏
 */

public class CustomTitleBar extends RelativeLayout {

    private ImageView ivBack;
    private TextView tvTitle;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context,attrs);
    }

    //初始化视图
    private void initView(final Context context, AttributeSet attributeSet) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        ivBack = inflate.findViewById(R.id.iv_back);
        tvTitle = inflate.findViewById(R.id.tv_title);

        init(context,attributeSet);
    }

    //初始化资源文件
    public void init(Context context, AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ItemGroup);
        String title = typedArray.getString(R.styleable.ItemGroup_bar_title);//标题
        int leftIcon = typedArray.getResourceId(R.styleable.ItemGroup_left_icon, R.drawable.arrow_left);//左边图片
        int titleBarType = typedArray.getInt(R.styleable.ItemGroup_titlebar_type, 10);//标题栏类型,默认为10

        //赋值进去我们的标题栏
        tvTitle.setText(title);
        ivBack.setImageResource(leftIcon);
    }

    //左边图片点击事件
    public void setLeftIconOnClickListener(OnClickListener l){
        ivBack.setOnClickListener(l);
    }

}
