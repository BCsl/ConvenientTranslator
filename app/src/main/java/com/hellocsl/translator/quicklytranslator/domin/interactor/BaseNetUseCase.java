package com.hellocsl.translator.quicklytranslator.domin.interactor;

import android.text.TextUtils;


import com.hellocsl.translator.quicklytranslator.domin.excutor.PostExecutionThread;
import com.hellocsl.translator.quicklytranslator.domin.excutor.ThreadExecutor;
import com.hellocsl.translator.quicklytranslator.domin.excutor.impl.JobExecutor;
import com.hellocsl.translator.quicklytranslator.domin.excutor.impl.MainThreadScheduler;
import com.hellocsl.translator.quicklytranslator.network.SysnNetworkService;
import com.hellocsl.translator.quicklytranslator.utils.L;
import com.hellocsl.translator.quicklytranslator.utils.ParamUtils;
import com.lidroid.xutils.http.HttpCache;
import com.lidroid.xutils.http.client.HttpRequest;


import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/19.
 */
public class BaseNetUseCase extends UseCase {
    private static final boolean DEBUG = true;
    private final String TAG = this.getClass().getSimpleName();
    public final static HttpCache sHttpCache = new HttpCache();

    private HashMap<String, String> mRequestParams;
    private String mUrl;
    private HttpRequest.HttpMethod mHttpMethod;
    private boolean isNeedCache = false;//默认关闭缓存

    public void setIsNeedCache(boolean isNeedCache) {
        this.isNeedCache = isNeedCache;
    }

    public BaseNetUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, String url, HttpRequest.HttpMethod httpMethod, HashMap<String, String> requestParams) {
        super(threadExecutor, postExecutionThread);
        mRequestParams = requestParams;
        mUrl = url;
        mHttpMethod = httpMethod;
    }

    public BaseNetUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, String url, HashMap<String, String> requestParams) {
        super(threadExecutor, postExecutionThread);
        mRequestParams = requestParams;
        mUrl = url;
        mHttpMethod = HttpRequest.HttpMethod.POST;
    }

    public BaseNetUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, String url) {
        super(threadExecutor, postExecutionThread);
        mRequestParams = null;
        mUrl = url;
        mHttpMethod = HttpRequest.HttpMethod.POST;
    }

    public BaseNetUseCase(String url, HashMap<String, String> requestParams) {
        super(JobExecutor.getInstance(), new MainThreadScheduler());
        mRequestParams = requestParams;
        mUrl = url;
        mHttpMethod = HttpRequest.HttpMethod.POST;
    }

    public BaseNetUseCase(String url) {
        super(JobExecutor.getInstance(), new MainThreadScheduler());
        mRequestParams = null;
        mUrl = url;
        mHttpMethod = HttpRequest.HttpMethod.POST;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> o) {
                o.onStart();
                String cache;
                if (isNeedCache && !TextUtils.isEmpty((cache = sHttpCache.get(mUrl)))) {
                    log("url:" + mUrl + "--cache--resutl-->" + cache);
                    o.onNext(cache);
                    o.onCompleted();
                    return;
                }
                String result = null;
                SysnNetworkService service;
                try {
                    String url = ParamUtils.JointUrl(mUrl, mRequestParams);
                    log("request url:" + url);
                    service = new SysnNetworkService(mHttpMethod, url, null);
                    result = service.startService();
                } catch (Exception e) {
                    e.printStackTrace();
                    o.onError(e);
                }
                o.onNext(result);
                o.onCompleted();
            }
        });
    }

    private void log(String s) {
        if (DEBUG) {
            L.d(TAG, s);
        }
    }


}
