package com.golan.amit.fractionstory;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivLioriSummaryLove;
    Animation fade;
    TextView tvSummaryDescription;
    Button btnSummaryBackToMenu, btnSummaryExit;
    private static final int MY_PERMISSION_REQUEST_SMS = 1;
    String endMessage = "";
    private String telNumber = "0522412342";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        init();

        setListeners();

        displaySummry();

    }

    private void sendSMS(String endMessage) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SMS);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {
            try {
                if(MainActivity.DEBUG) {
                    Log.i(MainActivity.DEBUGTAG, "sending sms to " + telNumber);
                }
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(telNumber, null, endMessage, null, null);
            } catch (Exception esms) {
                Log.e(MainActivity.DEBUGTAG, "exception when trying to send SMS");
            }
        }
    }

    private void displaySummry() {
        Intent i = getIntent();
        try {
            String game = i.getStringExtra("game");
            int fails = i.getIntExtra(getString(R.string.fails), -1);
            if (fails != -1) {
                String info = "המשחק";
                if (game != null && !game.isEmpty()) {
                    info += " ";
                    info += game;
                    info += " ";
                } else {
                    info += " ";
                }
                info += "נגמר ";
                if (fails == 0) {
                    info += "ללא טעויות. מעולה";
                } else if (fails == 1) {
                    info += "עם טעות אחת. ממש מצויין.";
                } else {
                    info += "עם ";
                    info += String.valueOf(fails);
                    info += " שגיאות.";
                }
                tvSummaryDescription.setText(info);
                sendSMS(info);
            }
        } catch (Exception e) {
            if (MainActivity.DEBUG) {
                Log.e(MainActivity.DEBUGTAG, "exception when receiving data from intent: " + e.getMessage());
            }
        }
    }

    private void setListeners() {
        btnSummaryBackToMenu.setOnClickListener(this);
        btnSummaryExit.setOnClickListener(this);
    }

    private void init() {
        ivLioriSummaryLove = findViewById(R.id.ivLioriSummaryLoveId);
        tvSummaryDescription = findViewById(R.id.tvSummaryDescId);
        btnSummaryBackToMenu = findViewById(R.id.btnSummaryBackMenuId);
        btnSummaryExit = findViewById(R.id.btnSummaryExitId);
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        ivLioriSummaryLove.startAnimation(fade);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSummaryBackToMenu) {
            Intent i = new Intent(this, MainMenuActivity.class);
            startActivity(i);
        } else if (v == btnSummaryExit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        }
    }
}
