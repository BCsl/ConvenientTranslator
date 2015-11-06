package com.hellocsl.translator.quicklytranslator.controller;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.text.TextUtils;
import android.view.*;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hellocsl.translator.quicklytranslator.R;
import com.hellocsl.translator.quicklytranslator.data.ResultEntity;
import com.hellocsl.translator.quicklytranslator.domin.callback.DefaultSubscriber;
import com.hellocsl.translator.quicklytranslator.domin.interactor.BaseNetUseCase;
import com.hellocsl.translator.quicklytranslator.utils.Constants;
import com.hellocsl.translator.quicklytranslator.utils.L;
import com.hellocsl.translator.quicklytranslator.utils.ParamUtils;
import com.hellocsl.translator.quicklytranslator.utils.ResponseUtils;

import java.util.HashMap;
import java.util.List;

public final class TipViewController extends DefaultSubscriber<String> implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private final int DEFAULT_SHOW_TIME = 5 * 1000;//5s
    private final int ERROR_SHOW_TIME = 2 * 1000;//2s
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewGroup mWholeView;
    private ViewDismissHandler mViewDismissHandler;
    private CharSequence mContent;
    private TextView mResultView;
    private ViewDismissTask mDismissTask;
    private ProgressBar mProgressBar;
    private BaseNetUseCase mNetUseCase;

    public TipViewController(Context application, CharSequence content) {
        mContext = application;
        mContent = content;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        mDismissTask = new ViewDismissTask();

        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }

    public void updateContent(CharSequence content) {
        mContent = content;
        mResultView.setText(mContent);
        mResultView.removeCallbacks(mDismissTask);
//        mResultView.postDelayed(mDismissTask, DEFAULT_SHOW_TIME);
        actionTranslating();
    }

    WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();

    public void show() {
        L.d(TAG, "show content");
        ViewGroup view = (ViewGroup) View.inflate(mContext, R.layout.pop_result, null);
        mWholeView = view;
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        // display content
        mResultView = (TextView) view.findViewById(R.id.tv_result);
        mResultView.setText(mContent);


        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = 0;
        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.width = w;
        mLayoutParams.height = h;
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.type = type;
        if (mWholeView.getParent() != null) {
            mWindowManager.removeView(mWholeView);
        }
        mWholeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mResultView.removeCallbacks(mDismissTask);
                        break;
                    case MotionEvent.ACTION_UP:
                        mResultView.postDelayed(mDismissTask, DEFAULT_SHOW_TIME);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                return false;
            }
        });
        mWindowManager.addView(mWholeView, mLayoutParams);
//        mResultView.postDelayed(mDismissTask, DEFAULT_SHOW_TIME);
        actionTranslating();
    }

    private void actionTranslating() {
        if (mNetUseCase != null) {
            mNetUseCase.unsubscribe();
        }
        HashMap<String, String> params = ParamUtils.getParams();
        params.put(Constants.params.Q, mContent.toString());
        mNetUseCase = new BaseNetUseCase(Constants.URL, params);
        mNetUseCase.setIsNeedCache(true);
        mNetUseCase.execute(this);

    }


    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
    }

    private void removePoppedViewAndClear() {
        mResultView.removeCallbacks(mDismissTask);
        // remove view
        if (mNetUseCase != null) {
            mNetUseCase.unsubscribe();
            mNetUseCase = null;
        }

        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }


        // remove listeners
        mWholeView.setOnTouchListener(null);

    }


    public interface ViewDismissHandler {
        void onViewDismiss();
    }

    private class ViewDismissTask implements Runnable {

        @Override
        public void run() {
            removePoppedViewAndClear();
        }
    }
    //-----------------data callback-----------------------

    @Override
    public void onStart() {
        super.onStart();
        mProgressBar.setVisibility(View.VISIBLE);
        mResultView.setVisibility(View.GONE);
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        mProgressBar.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
        mResultView.setText(e.getMessage());
        mResultView.postDelayed(mDismissTask, ERROR_SHOW_TIME);
    }

    @Override
    public void onNext(String s) {
        super.onNext(s);
        mProgressBar.setVisibility(View.GONE);
        mResultView.setVisibility(View.VISIBLE);
        ResultEntity entity = null;
        TextUtils.isEmpty(s);
        if (!TextUtils.isEmpty(s) && (entity = new Gson().fromJson(s, ResultEntity.class)) != null && ResponseUtils.requestSuc(entity)) {
            mResultView.setText(getResultStr(entity));
            mResultView.postDelayed(mDismissTask, DEFAULT_SHOW_TIME);
        } else {
            mResultView.setText(entity == null ? mContext.getString(R.string.unknown_error) : entity.getError_msg());
            mResultView.postDelayed(mDismissTask, ERROR_SHOW_TIME);
        }
    }

    private String getResultStr(ResultEntity entity) {
        StringBuilder sb = new StringBuilder();
        List<ResultEntity.TransResultEntity> entitys = entity.getTrans_result();
        for (ResultEntity.TransResultEntity temp : entitys) {
            sb.append(temp.getDst());
        }
        return sb.toString();
    }
}
