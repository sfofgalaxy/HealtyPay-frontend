package com.example.myapplication.ui.my;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.jar.Attributes;

public class CardInfo extends FrameLayout {

    private LinearLayout cardInfoLayout; //组合控件的布局
    private TextView message; //标题
    private Button button; //向右的箭头
    private ImageView Icon;



    public CardInfo(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CardInfo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttrs(context, attrs);
    }

    public CardInfo(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initAttrs(context,attrs);
    }

    //初始化View
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_info, null);
        cardInfoLayout = (LinearLayout) view.findViewById(R.id.card_info_layout);
        message = (TextView) view.findViewById(R.id.bank_message);
        button = (Button) view.findViewById(R.id.bank_unbind_button);
        Icon = (ImageView) view.findViewById(R.id.bank_icon);
        addView(view); //把自定义的这个组合控件的布局加入到当前FramLayout
    }
    /**
     * 初始化相关属性，引入相关属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        //标题的默认字体颜色
        int defaultTitleColor = context.getResources().getColor(R.color.black);
        //输入框的默认字体颜色
        int defaultEdtColor = context.getResources().getColor(R.color.black);
        //输入框的默认的提示内容的字体颜色
        int defaultHintColor = context.getResources().getColor(R.color.black);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemGroup);
        String title = typedArray.getString(R.styleable.ItemGroup_card_title);
        float paddingLeft = typedArray.getDimension(R.styleable.ItemGroup_paddingLeft, 15);
        float paddingRight = typedArray.getDimension(R.styleable.ItemGroup_paddingRight, 5);
        float paddingTop = typedArray.getDimension(R.styleable.ItemGroup_paddingTop, 5);
        float paddingBottom = typedArray.getDimension(R.styleable.ItemGroup_paddingTop, 5);
        float titleSize = typedArray.getDimension(R.styleable.ItemGroup_title_size, 20);
        int titleColor = typedArray.getColor(R.styleable.ItemGroup_title_color, defaultTitleColor);
//        String content = typedArray.getString(R.styleable.ItemGroup_edt_content);
//        float contentSize = typedArray.getDimension(R.styleable.ItemGroup_edt_text_size, 13);
//        int contentColor = typedArray.getColor(R.styleable.ItemGroup_edt_text_color, defaultEdtColor);
//        String hintContent = typedArray.getString(R.styleable.ItemGroup_edt_hint_content);
//        int hintColor = typedArray.getColor(R.styleable.ItemGroup_edt_hint_text_color, defaultHintColor);
        //默认输入框可以编辑
//        boolean isEditable = typedArray.getBoolean(R.styleable.ItemGroup_isEditable, true);

        int drawableId = typedArray.getResourceId(R.styleable.ItemGroup_icon_src,0);
        typedArray.recycle();

        //设置数据
        //设置item的内边距
        cardInfoLayout.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight, (int) paddingBottom);
        message.setText(title);
        message.setTextSize(titleSize);
        message.setTextColor(titleColor);

//        contentEdt.setText(content);
//        contentEdt.setTextSize(contentSize);
//        contentEdt.setTextColor(contentColor);
//        contentEdt.setHint(hintContent);
//        contentEdt.setHintTextColor(hintColor);
//        contentEdt.setFocusable(isEditable); //设置输入框是否可以编辑
//        contentEdt.setClickable(true);
//        contentEdt.setKeyListener(null);
        if (drawableId != 0){
            Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            Icon.setImageBitmap(gameStatusBitmap);
        }else{
            Icon.setVisibility(View.GONE);
        }

    }

    public CardInfo(@NonNull Context context, @Nullable Attributes attrs) {
        super(context);
        initView(context);
        //标题的默认字体颜色
        int defaultTitleColor = context.getResources().getColor(R.color.black);
        //输入框的默认字体颜色
        int defaultEdtColor = context.getResources().getColor(R.color.black);
        //输入框的默认的提示内容的字体颜色
        int defaultHintColor = context.getResources().getColor(R.color.black);


        String title = attrs.getValue("bank_title");
        float paddingLeft = 15;
        float paddingRight = 5;
        float paddingTop = 5;
        float paddingBottom = 5;
        float titleSize = 20;
        int titleColor = defaultTitleColor;

        int drawableId = Integer.parseInt(attrs.getValue("drawableId"));

        //设置数据
        //设置item的内边距
//        cardInfoLayout.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingRight, (int) paddingBottom);
        message.setText(title);
        message.setTextSize(titleSize);
        message.setTextColor(titleColor);
        if (drawableId != 0){
            Bitmap gameStatusBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            Icon.setImageBitmap(gameStatusBitmap);
        }else{
            Icon.setVisibility(View.GONE);
        }
    }
}
