package com.example.myapplication.config;

/**
 * Created by pzf on 2020/7/11
 */

public class Config {
    public static final String URL_PROTOCOL = "http://";
    public static final String URL_DOMAIN_NAME = "47.94.46.115";
    public static final String URL_HOST = ":1616";

    public static String getFullUrl(String name) {
        return URL_PROTOCOL + URL_DOMAIN_NAME + URL_HOST + name;
    }
}
