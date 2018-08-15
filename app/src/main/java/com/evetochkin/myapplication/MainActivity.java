package com.evetochkin.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class MainActivity extends AppCompatActivity {
    private static final String CONSUMER_KEY = "ilQajM4xvSNGtMDliIbuzAxkU";
    private static final String CONSUMER_SECRET = "hKXS3kHbnApcS8zXk8rdztwlAgcVDs8mi1qzHtyoIGu7KQ3ca3";

    private TwitterSuccessReceiver receiver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTwitter();
        initBroadcastReceivers();

        invoke();
    }

    private void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    public void invoke() {
        final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();
        if (session == null) {
            authWithTwitter();
        } else {
            shareWithTwitter(session);
        }
    }

    private void authWithTwitter() {
        new TwitterAuthClient().authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                final TwitterSession session = result.data;
                shareWithTwitter(session);
            }

            @Override
            public void failure(final TwitterException e) {
                e.printStackTrace();

            }
        });
    }

    private void shareWithTwitter(TwitterSession session) {
        final Intent intent = new ComposerActivity.Builder(this)
                .session(session)
                .text("TGES")
                .createIntent();
        startActivity(intent);
    }


    private void initBroadcastReceivers() {
        receiver = new TwitterSuccessReceiver(this, new TwitterCallback() {
            @Override
            public void onSuccess() {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError() {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        receiver.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiver.unregister();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new TwitterAuthClient().onActivityResult(requestCode, resultCode, data);
    }

    public static Intent intent(Context context) {
        return new Intent(context, MainActivity.class);
    }
}
