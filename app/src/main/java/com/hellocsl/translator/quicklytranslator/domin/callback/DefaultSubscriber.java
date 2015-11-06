package com.hellocsl.translator.quicklytranslator.domin.callback;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/8/20 0020.
 */
public class DefaultSubscriber<T> extends rx.Subscriber<T> {
    @Override
    public void onCompleted() {
        // no-op by default.
    }

    @Override
    public void onError(Throwable e) {
        // no-op by default.
    }

    @Override
    public void onNext(T t) {
        // no-op by default.
    }
}