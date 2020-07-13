package com.example.myapplication.ui.qrCode;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.R;
import com.example.myapplication.config.Config;
import com.example.myapplication.ui.nfc.NFCViewModel;
import com.example.myapplication.utils.BitMapUtil;
import com.example.myapplication.utils.HttpRequestUtil;
import com.example.myapplication.utils.JsonUtil;
import com.example.myapplication.utils.QRCodeUtil;
import com.example.myapplication.utils.SharedPreferencesUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class QRCodeFragment extends Fragment {

    private NFCViewModel nfcViewModel;
    private String mGetHealthStateUrl = Config.getFullUrl("/health/checkHealth");
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        nfcViewModel =
                ViewModelProviders.of(this).get(NFCViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_qrcode, container, false);

        ImageView qrCodeImageView = root.findViewById(R.id.qrcodeImage);

        String token = SharedPreferencesUtil.getString(context,"token",null);
        if(token==null){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else if(token.equals("")){
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }else {
            Map<String,String> param = new HashMap<>();
            param.put("token",token);
            String res = HttpRequestUtil.sendPost(mGetHealthStateUrl,param,token);
            if(res.equals("")){
                Toast.makeText(context,"查询健康状况失败",Toast.LENGTH_SHORT).show();
            }else{
                try {
                    String state =  JsonUtil.stringToJsonObject(res).getString("message");
                    if(state.equals("1")){
                        qrCodeImageView.setImageBitmap(BitMapUtil.convertToGreen(QRCodeUtil.createQRImage("http://47.94.46.115/", 300, 300)));
                    }else if(state.equals("2")){
                        qrCodeImageView.setImageBitmap(BitMapUtil.convertToYellow(QRCodeUtil.createQRImage("http://47.94.46.115/", 300, 300)));
                    }else {
                        qrCodeImageView.setImageBitmap(BitMapUtil.convertToRed(QRCodeUtil.createQRImage("http://47.94.46.115/", 300, 300)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ImageButton trImageButton = root.findViewById(R.id.transportImageButton);
        Log.d("button", "findSuccess");
        trImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageView imageView = root.findViewById(R.id.qrcodeBankSymbol);
                imageView.setImageResource(R.drawable.bus2);
                TextView textView = root.findViewById(R.id.qrBankCard);
                Log.d("button", textView.getText().toString());
                textView.setText("杭州公交乘车码");
            }
        } );

        ImageButton payImageButton = root.findViewById(R.id.payImageButton);
        payImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageView imageView = root.findViewById(R.id.qrcodeBankSymbol);
                imageView.setImageResource(R.drawable.abc);
                TextView textView = root.findViewById(R.id.qrBankCard);
                Log.d("button", textView.getText().toString());
                textView.setText("农业银行储蓄卡");
            }
        });


        //NavHostController navHostController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //navHostController.navigate(R.id.secondFragment);
        //navHostController.navigateUp();
        //final TextView textView = root.findViewById(R.id.text_nfc);
        /*nfcViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}