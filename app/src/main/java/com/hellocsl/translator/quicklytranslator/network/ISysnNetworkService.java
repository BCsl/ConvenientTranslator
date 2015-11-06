package com.hellocsl.translator.quicklytranslator.network;

import com.lidroid.xutils.exception.HttpException;

import java.io.IOException;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/19 0019.
 */
public interface ISysnNetworkService<T> {
    /**
     * 开启服务
     * @return
     */
    T startService() throws HttpException, IOException;

    /**
     * 关闭服务
     */
    void stopSercice();
}
