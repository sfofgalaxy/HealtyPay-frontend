package com.example.myapplication.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.CustomTitleBar;

public class BillActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        CustomTitleBar titleBar = findViewById(R.id.bill_left);
        Button button = titleBar.findViewById(R.id.add_button);
        button.setText("下载");
        titleBar.setLeftIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillActivity.this.finish();
            }
        });
        titleBar.setButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BillActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
            }
        });

//        ListView mListView = findViewById(R.id.bill_entries);
//
//        int[] images = { R.drawable.bus2, R.drawable.subway,
//                R.drawable.bus2, R.drawable.subway, R.drawable.bus2,
//                R.drawable.subway, R.drawable.bus2,
//                R.drawable.subway, R.drawable.bus2, };
//        List< Map<String,Object> > mBillList = new ArrayList<>();
//        // 准备数据
//        for (int i = 0; i < images.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("img", images[i]);
//            map.put("text", "item" + i);
//            mBillList.add(map);
//        }
//        /**
//         * 将数据源的数据加载到适配器中 SimpleAdapter
//         * context: 上下文对象
//         * data：表示加载到适配器中的数据对象
//         * resource： 表示adapter控件中每项资源id
//         * from:表示数据源map 中key 的数组，表示key指定的值
//         * to：表示需要展示对应数据的控件资源id
//         *
//         * 通过from 和to的对应，将from 中key值对应的数据指定的显示到to 指定资源id的控件中
//         *
//         * **/
//        SimpleAdapter adapter = new SimpleAdapter(BillActivity.this,
//               mBillList, R.layout.activity_bill, new String[] {
//                "img", "text" }, new int[] { R.id.bill_image, R.id.bill_text });
//        // 将适配器中的数据添加到控件中
//        mListView.setAdapter(adapter);

    }
}
