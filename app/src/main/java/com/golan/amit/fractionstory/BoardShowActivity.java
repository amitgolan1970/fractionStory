package com.golan.amit.fractionstory;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class BoardShowActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView ivBoardTable;
    Animation scaleAnim;

    /**
     * Timer
     * @param savedInstanceState
     */

    private static final long MINUTES = 5;
    private static final long TIMER = MINUTES * 60 * 1000;
    private int countDownInterval;
    CountDownTimer cTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_show);

        init();

        timerDemo(TIMER);
    }

    private void init() {
        ivBoardTable = (ImageView)findViewById(R.id.ivBoardId);
        ivBoardTable.setOnTouchListener(this);
        scaleAnim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        ivBoardTable.startAnimation(scaleAnim);

        cTimer = null;
        countDownInterval = 1000;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == ivBoardTable) {
            cTimer.cancel();
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cTimer.cancel();
        } catch (Exception e) {
        }
    }

    private void timerDemo(final long millisInFuture) {
        cTimer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }
}
