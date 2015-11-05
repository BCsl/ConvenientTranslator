package com.hellocsl.translator.quicklytranslator.receiver;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.hellocsl.translator.quicklytranslator.service.ListenClipboardService;

public class BootCompletedReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ListenClipboardService.startForWeakLock(context, intent);
    }
}
