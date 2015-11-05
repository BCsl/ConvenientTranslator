package com.hellocsl.translator.quicklytranslator;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.view.*;
import android.widget.TextView;

public final class TipViewController implements View.OnClickListener {
    private final int SHOW_TIME = 5 * 1000;//5s
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewGroup mWholeView;
    private View mContentView;
    private ViewDismissHandler mViewDismissHandler;
    private CharSequence mContent;
    private TextView mTextView;
    private ViewDismissTask mDismissTask;

    public TipViewController(Context application, CharSequence content) {
        mContext = application;
        mContent = content;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        mDismissTask = new ViewDismissTask();
    }

    public void setViewDismissHandler(ViewDismissHandler viewDismissHandler) {
        mViewDismissHandler = viewDismissHandler;
    }

    public void updateContent(CharSequence content) {
        mContent = content;
        mTextView.setText(mContent);
        mTextView.removeCallbacks(mDismissTask);
        mTextView.postDelayed(mDismissTask, SHOW_TIME);
    }

    public void show() {

        ViewGroup view = (ViewGroup) View.inflate(mContext, R.layout.pop_result, null);
        mWholeView = view;
        // display content
        mTextView = (TextView) view.findViewById(R.id.pop_view_text);
        mTextView.setText(mContent);

        mContentView = view.findViewById(R.id.pop_view_content_view);

        // event listeners
        mContentView.setOnClickListener(this);

        int w = WindowManager.LayoutParams.MATCH_PARENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = 0;
        int type = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP;
        mWindowManager.addView(mWholeView, layoutParams);
        mTextView.postDelayed(mDismissTask, SHOW_TIME);
    }

    @Override
    public void onClick(View v) {
        removePoppedViewAndClear();
    }

    private void removePoppedViewAndClear() {
        mTextView.removeCallbacks(mDismissTask);
        // remove view
        if (mWindowManager != null && mWholeView != null) {
            mWindowManager.removeView(mWholeView);
        }

        if (mViewDismissHandler != null) {
            mViewDismissHandler.onViewDismiss();
        }

        // remove listeners
        mContentView.setOnClickListener(null);
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
}
