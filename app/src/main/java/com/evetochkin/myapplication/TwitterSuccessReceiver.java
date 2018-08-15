package com.evetochkin.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TwitterSuccessReceiver extends BroadcastReceiver {
    private final Activity host;
    private TwitterCallback callback;

    public TwitterSuccessReceiver(Activity host, TwitterCallback callback) {
        this.host = host;
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            callback.onSuccess();
            Log.d("TwitterSuccessReceiver", "onSuccess");
        } else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            callback.onError();
            Log.d("TwitterSuccessReceiver", "onError");
        } else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            callback.onSuccess();
            Log.d("TwitterSuccessReceiver", "onCancel");
        }

    }

    public void register() {
        host.registerReceiver(this, getIntentFilter());
    }

    public void unregister() {
        host.unregisterReceiver(this);
    }

    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TweetUploadService.UPLOAD_SUCCESS);
        filter.addAction(TweetUploadService.UPLOAD_FAILURE);
        filter.addAction(TweetUploadService.TWEET_COMPOSE_CANCEL);
        return filter;
    }


}