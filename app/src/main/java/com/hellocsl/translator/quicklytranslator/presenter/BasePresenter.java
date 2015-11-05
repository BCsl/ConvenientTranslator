package com.hellocsl.translator.quicklytranslator.presenter;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/11/5 0005.
 */
public class BasePresenter {
    protected WeakReference<Context> mContext;

    public BasePresenter(Context context) {
        mContext = new WeakReference<Context>(context);
    }


}
