package com.hellocsl.translator.quicklytranslator.utils;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/11/6 0006.
 */
public class Constants {
    public static class params {
        public static final String FROM = "from";//源语言语种：语言代码或auto	仅支持特定的语言组合，下面会单独进行说明
        public static final String TO = "to";  //目标语言语种：语言代码或auto	仅支持特定的语言组合，下面会单独进行说明
        public static final String CLIENT_ID = "client_id";    //开发者在百度开发者中心注册得到的授权API key	请阅读如何获取api keyhttp://developer.baidu.com/console#app/project
        public static final String Q = "q"; //待翻译内容	该字段必须为UTF-8编码，并且以GET方式调用API时，需要进行urlencode编码。
    }


    public static final String URL = "http://openapi.baidu.com/public/2.0/bmt/translate";
    public static final String API_KEY = "VfxM8ezGXrLIduQSCKUj5kaC";
    public static final String AUTO = "auto";
}
