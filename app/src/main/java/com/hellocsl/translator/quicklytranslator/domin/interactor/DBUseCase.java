package com.hellocsl.translator.quicklytranslator.domin.interactor;

import android.content.Context;
import android.text.TextUtils;

import com.hellocsl.translator.quicklytranslator.domin.excutor.PostExecutionThread;
import com.hellocsl.translator.quicklytranslator.domin.excutor.ThreadExecutor;
import com.hellocsl.translator.quicklytranslator.domin.excutor.impl.DBExecutor;
import com.hellocsl.translator.quicklytranslator.domin.excutor.impl.MainThreadScheduler;
import com.hellocsl.translator.quicklytranslator.utils.L;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by HelloCsl(cslgogogo@gmail.com) on 2015/9/1 0001.
 */
public class DBUseCase extends UseCase {
    private final String TAG = this.getClass().getSimpleName();

    public enum ACTION {
        SAVE, SAVE_ALL, DELETE_ONE, FIND_ALL
    }

    public enum RESULT {
        SUC, ERR
    }

    private ACTION mACTION;
    private Object mObject;
    private String mDName;
    private List<?> mEntitys;
    private Selector mSelector;
    private WeakReference<Context> mContext;

    public WeakReference<Context> getContext() {
        return mContext;
    }

    public void setContext(WeakReference<Context> context) {
        mContext = context;
    }

    public void setSelector(Selector selector) {
        mSelector = selector;
    }

    public void setEntitys(List<?> entitys) {
        mEntitys = entitys;
    }

    public void setACTION(ACTION ACTION) {
        mACTION = ACTION;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    public void setDName(String DName) {
        mDName = DName;
    }

    public DBUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    private DBUseCase(Builder builder) {
        super(builder.threadExecutor, builder.postExecutionThread);
        if (builder.context == null) {
            throw new IllegalStateException("context is not null allowed!");
        }
        setContext(builder.context);
        if (TextUtils.isEmpty(builder.dName)) {
            L.w(TAG, "Dangerous!!!try to use default XUtil.db");
        }
        if (builder.ACTION == null) {
            throw new IllegalStateException("operate action is undefined!");
        }
        setACTION(builder.ACTION);
        setDName(builder.dName);
        switch (builder.ACTION) {
            case SAVE:
                if (builder.object == null) {
                    throw new IllegalStateException("try to save null object!");
                }
                setObject(builder.object);
                break;
            case SAVE_ALL:
                if (builder.entitys == null || builder.entitys.isEmpty()) {
                    throw new IllegalStateException("try to save null entitys!");
                }
                setEntitys(builder.entitys);
                break;
            case FIND_ALL:
                if (builder.selector == null) {
                    throw new IllegalStateException("try to find with null selector!");
                }
                setSelector(builder.selector);
                break;
            case DELETE_ONE:
                if (builder.object == null) {
                    throw new IllegalStateException(new DbException("try to delete null entitys!"));
                }
                setObject(builder.object);
                break;
            default:
                throw new IllegalStateException("undefined operate action :" + mACTION);
        }
    }

    @Override
    protected Observable buildUseCaseObservable() {

        switch (mACTION) {
            case SAVE:
                return Observable.create(new Observable.OnSubscribe<RESULT>() {

                    @Override
                    public void call(Subscriber<? super RESULT> subscriber) {
                        subscriber.onStart();
                        DbUtils dbUtils = DbUtils.create(mContext.get(), mDName);
                        try {
                            dbUtils.saveOrUpdate(mObject);
                        } catch (DbException e) {
                            e.printStackTrace();
                            subscriber.onError(new DbException(e));
                        }
                        subscriber.onNext(RESULT.SUC);
                        subscriber.onCompleted();
                    }
                });
            case SAVE_ALL:
                return Observable.create(new Observable.OnSubscribe<RESULT>() {

                    @Override
                    public void call(Subscriber<? super RESULT> subscriber) {
                        subscriber.onStart();
                        DbUtils dbUtils = DbUtils.create(mContext.get(), mDName);
                        try {
                            dbUtils.saveOrUpdateAll(mEntitys);
                        } catch (DbException e) {
                            e.printStackTrace();
                            subscriber.onError(new DbException(e));
                        }
                        subscriber.onNext(RESULT.SUC);
                        subscriber.onCompleted();
                    }
                });
            case FIND_ALL:
                return Observable.create(new Observable.OnSubscribe<List>() {

                    @Override
                    public void call(Subscriber<? super List> subscriber) {
                        subscriber.onStart();

                        DbUtils dbUtils = DbUtils.create(mContext.get(), mDName);
                        List<?> result = null;
                        try {
                            result = dbUtils.findAll(mSelector);
                        } catch (DbException e) {
                            subscriber.onError(new DbException(e));
                        }
                        if (result != null) {
                            L.d(TAG, "find result:" + result.toString());
                        }
                        subscriber.onNext(result);
                        subscriber.onCompleted();
                    }
                });
            case DELETE_ONE:
                return Observable.create(new Observable.OnSubscribe<RESULT>() {

                    @Override
                    public void call(Subscriber<? super RESULT> subscriber) {
                        subscriber.onStart();
                        DbUtils dbUtils = DbUtils.create(mContext.get(), mDName);
                        try {
                            dbUtils.delete(mObject);
                        } catch (DbException e) {
                            e.printStackTrace();
                            subscriber.onError(new DbException(e));
                        }
                        subscriber.onNext(RESULT.SUC);
                        subscriber.onCompleted();
                    }
                });
            default:
                new IllegalStateException("illegal operate action :" + mACTION);
        }
        return null;
    }


    public static class Builder {
        private ACTION ACTION;
        private Object object;
        private String dName;
        private List<?> entitys;
        private ThreadExecutor threadExecutor = DBExecutor.getInstance();
        private PostExecutionThread postExecutionThread = new MainThreadScheduler();
        private Selector selector;
        private WeakReference<Context> context;


        public Builder(Context context) {
            this.context = new WeakReference<Context>(context);
        }

        public Builder setSelector(Selector selector) {
            this.selector = selector;
            return this;
        }

        public Builder setACTION(ACTION ACTION) {
            this.ACTION = ACTION;
            return this;
        }

        public Builder setObject(Object object) {
            this.object = object;
            return this;
        }

        public Builder setEntitys(List<?> entitys) {
            this.entitys = entitys;
            return this;
        }

        public Builder setdName(String dName) {
            this.dName = dName;
            return this;
        }

        public Builder setThreadExecutor(ThreadExecutor threadExecutor) {
            this.threadExecutor = threadExecutor;
            return this;
        }

        public Builder setPostExecutionThread(PostExecutionThread postExecutionThread) {
            this.postExecutionThread = postExecutionThread;
            return this;
        }

        public DBUseCase build() {
            return new DBUseCase(this);
        }


    }


}
