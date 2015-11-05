package com.hellocsl.translator.quicklytranslator.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;

import com.hellocsl.translator.quicklytranslator.MainActivity;
import com.hellocsl.translator.quicklytranslator.R;
import com.hellocsl.translator.quicklytranslator.TipViewController;
import com.hellocsl.translator.quicklytranslator.clipboard.ClipboardManagerCompat;
import com.hellocsl.translator.quicklytranslator.receiver.BootCompletedReceiver;

public final class ListenClipboardService extends Service implements TipViewController.ViewDismissHandler {

    private static final String KEY_FOR_WEAK_LOCK = "weak-lock";
    private static final String KEY_FOR_CMD = "cmd";
    private static final String KEY_FOR_CONTENT = "content";
    private static final String CMD_TEST = "test";

    private static CharSequence sLastContent = null;
    private ClipboardManagerCompat mClipboardWatcher;
    private TipViewController mTipViewController;

    private ClipboardManagerCompat.OnPrimaryClipChangedListener mOnPrimaryClipChangedListener = new ClipboardManagerCompat.OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }
    };
    private final int NOTIFICATIONID = 1;

    public static void start(Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);
    }
    public static void stopService(Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.stopService(serviceIntent);
    }

    public static void startForWeakLock(Context context, Intent intent) {

        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);

        intent.putExtra(ListenClipboardService.KEY_FOR_WEAK_LOCK, true);
        Intent myIntent = new Intent(context, ListenClipboardService.class);

        // using wake lock to start service
        WakefulBroadcastReceiver.startWakefulService(context, myIntent);
    }

    @Override
    public void onCreate() {
        mClipboardWatcher = ClipboardManagerCompat.create(this);
        mClipboardWatcher.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelAppNotification();
        mClipboardWatcher.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener);
        sLastContent = null;
        if (mTipViewController != null) {
            mTipViewController.setViewDismissHandler(null);
            mTipViewController = null;
        }
    }

    private void showAppNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_global_menu_search)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.app_is_running));
        Intent resultIntent = MainActivity.newIntent(this);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_NO_CREATE);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATIONID, builder.build());
    }

    private void cancelAppNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATIONID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            showAppNotification();
            // remove wake lock
            if (intent.getBooleanExtra(KEY_FOR_WEAK_LOCK, false)) {
                BootCompletedReceiver.completeWakefulIntent(intent);
            }
            String cmd = intent.getStringExtra(KEY_FOR_CMD);
            if (!TextUtils.isEmpty(cmd)) {
                if (cmd.equals(CMD_TEST)) {
                    String content = intent.getStringExtra(KEY_FOR_CONTENT);
                    showContent(content);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        CharSequence content = mClipboardWatcher.getText();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        showContent(content);
    }

    private void showContent(CharSequence content) {
        if (sLastContent != null && sLastContent.equals(content) || content == null) {
            return;
        }
        sLastContent = content;

        if (mTipViewController != null) {
            mTipViewController.updateContent(content);
        } else {
            mTipViewController = new TipViewController(getApplication(), content);
            mTipViewController.setViewDismissHandler(this);
            mTipViewController.show();
        }
    }

    @Override
    public void onViewDismiss() {
        sLastContent = null;
        mTipViewController = null;
    }

}