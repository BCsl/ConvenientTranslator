package com.hellocsl.translator.quicklytranslator.utils;


import android.text.TextUtils;

import com.hellocsl.translator.quicklytranslator.data.BaseEntity;

import org.w3c.dom.Text;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/9/7 0007.
 */
public class ResponseUtils {
    /**
     * 是否请求成功
     *
     * @param entity
     * @return
     */
    public static boolean requestSuc(BaseEntity entity) {
        return TextUtils.isEmpty(entity.getError_code());
    }

    /**
     * @param entity
     * @param def
     * @return
     */
    public static String getResMeg(BaseEntity entity, String def) {
        return entity == null ? def : entity.getError_msg();
    }

    /**
     * @param entity
     * @return
     */
    public static String getResMeg(BaseEntity entity) {
        return getResMeg(entity, "");
    }

}
