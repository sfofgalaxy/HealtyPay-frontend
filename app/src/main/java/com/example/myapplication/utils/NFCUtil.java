package com.example.myapplication.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NFCUtil {
    /**
     * Created by pzf on 2020/7/12
     */
        // NFC适配器
        public NfcAdapter nfcAdapter = null;
        // 传达意图
        public PendingIntent pi = null;
        // 滤掉组件无法响应和处理的Intent
        public IntentFilter tagDetected = null;
        // 是否支持NFC功能的标签
        public boolean isNFC_support = false;

        public Intent intent;

        public Tag tagFromIntent;

        private static NFCUtil nfcOperator;

        private NFCUtil() {

        }

        public static NFCUtil getInstance() {
            if (nfcOperator == null) {
                nfcOperator = new NFCUtil();
            }
            return nfcOperator;
        }

        //传入当前activity
        public void initNFCData(Context context) {
            // 初始化设备支持NFC功能
            isNFC_support = true;
            // 得到默认nfc适配器
            nfcAdapter = NfcAdapter.getDefaultAdapter(context);
            // 提示信息定义
            String metaInfo = "";
            // 判定设备是否支持NFC或启动NFC
            if (nfcAdapter == null) {
                metaInfo = "设备不支持NFC！";
                Toast.makeText(context.getApplicationContext(), metaInfo, Toast.LENGTH_SHORT).show();
                isNFC_support = false;
            }
            if (!nfcAdapter.isEnabled()) {
                metaInfo = "请在系统设置中先启用NFC功能！";
                Toast.makeText(context.getApplicationContext(), metaInfo, Toast.LENGTH_SHORT).show();
                isNFC_support = false;
            }

            if (isNFC_support) {
                init_NFC(context);
            }
        }

        public void init_NFC(Context context) {
            // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
            pi = PendingIntent.getActivity(context, 0, new Intent(context, context.getClass())
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // 新建IntentFilter，使用的是第二种的过滤机制
            tagDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);


        }

        public String read() {
            Tag tag = tagFromIntent;
            if (tag != null) {
                String msg = "";
                Parcelable[] rawArray = intent
                        .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                if (rawArray != null) {
                    NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
                    NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
                    try {
                        if (mNdefRecord != null) {
                            msg = new String(mNdefRecord.getPayload(), "UTF-8");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return msg;
                }
            }
            return null;
        }

        // 写入方法
        public void write(String msg) throws IOException, FormatException {
            Tag tag = tagFromIntent;
            if (tag != null) {
                //新建NdefRecord数组，本例中数组只有一个元素
                NdefRecord[] records = {createRecord(msg)};
                //新建一个NdefMessage实例
                NdefMessage message = new NdefMessage(records);
                // 解析TAG获取到NDEF实例
                Ndef ndef = Ndef.get(tag);
                // 打开连接
                ndef.connect();
                // 写入NDEF信息
                ndef.writeNdefMessage(message);
                // 关闭连接
                ndef.close();
            }
        }

        //返回一个NdefRecord实例
        private NdefRecord createRecord(String msg) throws UnsupportedEncodingException {
            //组装字符串，准备好你要写入的信息
//        String msg = "BEGIN:VCARD\n" + "VERSION:2.1\n" + "中国湖北省武汉市\n"
//                + "武汉大学计算机学院\n" + "END:VCARD";
            //将字符串转换成字节数组
            byte[] textBytes = msg.getBytes();
            //将字节数组封装到一个NdefRecord实例中去
            return new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                    "text/plain".getBytes(), new byte[]{}, textBytes);
        }


        // 字符序列转换为16进制字符串
        private String bytesToHexString(byte[] src) {
            return bytesToHexString(src, true);
        }

        private String bytesToHexString(byte[] src, boolean isPrefix) {
            StringBuilder stringBuilder = new StringBuilder();
            if (isPrefix) {
                stringBuilder.append("0x");
            }
            if (src == null || src.length <= 0) {
                return null;
            }
            char[] buffer = new char[2];
            for (byte b : src) {
                buffer[0] = Character.toUpperCase(Character.forDigit(
                        (b >>> 4) & 0x0F, 16));
                buffer[1] = Character.toUpperCase(Character.forDigit(b & 0x0F,
                        16));
                System.out.println(buffer);
                stringBuilder.append(buffer);
            }
            return stringBuilder.toString();
        }

        /**
         * Parses the NDEF Message from the intent and prints to the TextView
         */
        public String processIntent(Intent intent) {
            String msg = "";
            if (!isNFC_support)
                return msg;

            this.intent = intent;

            // 取出封装在intent中的TAG
            tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Parcelable[] rawArray = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawArray != null) {
                NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
                NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
                try {
                    if (mNdefRecord != null) {
                        msg = new String(mNdefRecord.getPayload(), "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return msg;
        }

    }

