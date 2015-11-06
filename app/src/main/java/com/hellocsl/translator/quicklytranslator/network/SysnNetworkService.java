package com.hellocsl.translator.quicklytranslator.network;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.IOException;

/**
 * 同步网络请求，哪个线程调用就在哪个线程运行
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/19.
 */
public class SysnNetworkService implements ISysnNetworkService {
    private RequestParams mRequestParams;
    private String mUrl;
    private HttpRequest.HttpMethod mHttpMethod;

    public SysnNetworkService(HttpRequest.HttpMethod httpMethod, String url, RequestParams requestParams) {
        mRequestParams = requestParams;
        mUrl = url;
        mHttpMethod = httpMethod;
    }

    @Override
    public String startService() throws HttpException, IOException {
        HttpUtils httpUtils = new HttpUtils();
        ResponseStream stream = httpUtils.sendSync(mHttpMethod, mUrl, mRequestParams);
        if (stream.getStatusCode() != 200) {
            stream.close();
            throw new HttpException(stream.getReasonPhrase());
        }
        String result = stream.readString();
        stream.close();
        return result;
    }

    @Override
    public void stopSercice() {
        // TO DO NOTHING
    }
}
