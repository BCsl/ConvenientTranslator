package com.hellocsl.translator.quicklytranslator.domin.excutor.impl;


import com.hellocsl.translator.quicklytranslator.domin.excutor.PostExecutionThread;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/20 0020.
 */
public class MainThreadScheduler implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
