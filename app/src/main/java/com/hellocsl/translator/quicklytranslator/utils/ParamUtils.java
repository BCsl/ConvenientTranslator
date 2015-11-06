package com.hellocsl.translator.quicklytranslator.utils;

import android.text.TextUtils;


import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/18 0018.
 */
public class ParamUtils {
    private static final String TAG = "ParamUtils";

    public static HashMap<String, String> getParams() {
        HashMap params = new HashMap();
        params.put(Constants.params.CLIENT_ID, Constants.API_KEY);
        params.put(Constants.params.FROM, Constants.AUTO);
        params.put(Constants.params.TO, Constants.AUTO);
        return params;
    }


    /**
     * @param requestUrl url
     * @param params     请求参数
     * @return
     */
    public static String JointUrl(String requestUrl, HashMap<String, String> params) {

        if (TextUtils.isEmpty(requestUrl)) {
            return null;
        }
        Boolean isFirstParma = true;
        StringBuilder sb = new StringBuilder();
        sb.append(requestUrl);
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

        while (iterator.hasNext())//拼接网络请求地址
        {
            Map.Entry<String, String> entry = iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (isFirstParma)//如果是第一个请求参数,使用？连接
            {
                sb.append("?");
                isFirstParma = false;
            } else {
                sb.append("&");
            }
            sb.append(key + "=" + URLEncoder.encode((String) value));
        }
        return sb.toString();

    }


}
