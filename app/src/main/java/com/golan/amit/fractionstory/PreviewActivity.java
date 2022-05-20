package com.golan.amit.fractionstory;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class PreviewActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView ivLioriPicPreview;
    Animation fade;

    /**
     * Timer
     * @param savedInstanceState
     */

    private static final long MINUTES = 1;
    private static final long TIMER = MINUTES * 10 * 1000;
//    private long secondsRemain;
    private int countDownInterval;
    private long timeToRemain;
    CountDownTimer cTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        init();

//        timerDemo(TIMER);
    }

    private void init() {
        ivLioriPicPreview = findViewById(R.id.ivLioriPreviewId);
        ivLioriPicPreview.setOnTouchListener(this);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ivLioriPicPreview.startAnimation(fade);

        cTimer = null;
        timeToRemain = TIMER;
        countDownInterval = 1000;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == ivLioriPicPreview) {
            try {
                if(MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "cancelling timer and redirecting");
                }
                cTimer.cancel();
                redirectToMenu();
            } catch (Exception e) {
            }
        }
        return true;
    }

    private void redirectToMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivityForResult(intent, 2);
    }

    private void timerDemo(final long millisInFuture) {

        cTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeToRemain = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                redirectToMenu();
                return;
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerDemo(timeToRemain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cTimer.cancel();
    }
}
