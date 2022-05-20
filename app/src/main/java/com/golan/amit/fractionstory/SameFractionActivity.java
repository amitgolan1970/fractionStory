package com.golan.amit.fractionstory;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SameFractionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_FROM_SAME = 4;
    TextView tvLeftMone, tvRightMone, tvLeftMekhane;
    TextView tvDescription;
    EditText etRightMekhane;
    ImageButton ibtnDoneGo;
    Button btnBackToMain;
    ImageView ivBtnHelpShowBoard;
    FractionHelper fh;

    SoundPool soundpool;
    private int soundPunchInt;
    private int soundLaserInt;
    private int soundTromboneInt;
    private int soundApplauseInt;
    private int soundCheeringInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_same_fraction);

        init();

        setListeners();

        display();

        displayDescription();
    }

    public void btnHelpShowBoard(View v) {
        if (v == ivBtnHelpShowBoard) {
            if (MainActivity.DEBUG) {
                Log.i(MainActivity.DEBUGTAG, "help clicked specific button");
            }
            Intent i = new Intent(this, BoardShowActivity.class);
            startActivity(i);
        }
    }

    private void displayDescription() {
        String descStr = "סיבוב מספר ";
        descStr += fh.getSivuv();
        if(fh.getFails() > 0) {
            if(fh.getFails() == 1) {
                descStr += " כשלון אחד";
            } else {
                descStr += " כשלונות: ";
                descStr += fh.getFails();
            }
        }
        tvDescription.setText(descStr);
    }

    private void display() {
        tvLeftMone.setText(String.valueOf(fh.getLeftMone()));
        tvLeftMekhane.setText(String.valueOf(fh.getLeftMekhane()));
        tvRightMone.setText(String.valueOf(fh.getRightMone()));
    }

    private void init() {
        tvLeftMone = findViewById(R.id.tvLeftMone);
        tvRightMone = findViewById(R.id.tvRightMone);
        tvLeftMekhane = findViewById(R.id.tvLeftMekhane);
        tvDescription = findViewById(R.id.tvDescriptionId);
        etRightMekhane = findViewById(R.id.etRightMekhane);
        ibtnDoneGo = findViewById(R.id.ibtnDoneGoId);
        btnBackToMain = findViewById(R.id.btnBackMainMenuFromSameId);
        ivBtnHelpShowBoard = findViewById(R.id.ivBtnHelpSameId);
        fh = new FractionHelper();
        fh.init();
        fh.increaseSivuv();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();
            soundpool = new SoundPool.Builder()
                    .setMaxStreams(10).setAudioAttributes(aa).build();
        } else {
            soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        soundPunchInt = soundpool.load(this, R.raw.punch, 1);
        soundLaserInt = soundpool.load(this, R.raw.laser, 1);
        soundTromboneInt = soundpool.load(this, R.raw.failtrombone, 1);
        soundApplauseInt = soundpool.load(this, R.raw.applause, 1);
        soundCheeringInt = soundpool.load(this, R.raw.cheering, 1);
    }

    private void setListeners() {
        ibtnDoneGo.setOnClickListener(this);
        btnBackToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnBackToMain) {
            Intent i = new Intent(this, MainMenuActivity.class);
            startActivity(i);
        }
        else if(v == ibtnDoneGo) {
            String tmpValStr = etRightMekhane.getText().toString().trim();
            if(tmpValStr.isEmpty()) {
                Toast.makeText(this, "ערכים ריקים אינם חוקיים", Toast.LENGTH_SHORT).show();
                return;
            }
            int tmpVal = Integer.parseInt(tmpValStr);
            if(tmpVal == fh.getRightMekhane()) {
                if(fh.getError() == 0) {
                    Toast.makeText(this, "מעולה! תשובה נכונה. כל הכבוד", Toast.LENGTH_SHORT).show();
                    soundpool.play(soundApplauseInt, 1, 1, 0, 0, 1);
                } else {
                    soundpool.play(soundCheeringInt, 1, 1, 0, 0, 1);
                    Toast.makeText(this, "יופי! תשובה נכונה.", Toast.LENGTH_SHORT).show();
                }
                etRightMekhane.setText(null);
                fh.resetError();
                fh.init();
                fh.increaseSivuv();
                if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
                    endGame();
                }
                else {
                    display();
                    displayDescription();
                }
            } else {
                etRightMekhane.setText(null);
                fh.increaseError();
                if(fh.getError() == (FractionHelper.ERRORS_PER_FAIL)) {
                    int tmpRnd = (int)(Math.random() * 2);
                    if(tmpRnd % 2 == 0) {
                        soundpool.play(soundLaserInt, 1, 1, 0, 0, 1);
                    } else {
                        soundpool.play(soundTromboneInt, 1, 1, 0, 0, 1);
                    }
                    String errFinal = "טעות. התשובה הייתה: ";
                    errFinal += fh.getRightMekhane();
                    tvDescription.setText(errFinal);
                    Toast.makeText(this, errFinal, Toast.LENGTH_LONG).show();
                    fh.resetError();
                    fh.increaseFails();
                    fh.increaseSivuv();
                    if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
                        endGame();
                        return;
                    } else {
                        fh.init();
                        display();
                    }
                } else{
                    soundpool.play(soundPunchInt, 1, 1, 0, 0, 1);
                    Toast.makeText(this, "טעות. תשובה שגוייה.", Toast.LENGTH_SHORT).show();
                    String err = "טעות מס ";
                    err += fh.getError();
                    tvDescription.setText(err);
                }

                return;
            }
        }
    }

    private void endGame() {
        Toast.makeText(this, "המשחק נגמר", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, SummaryActivity.class);
        i.putExtra(getString(R.string.fails), fh.getFails());
        i.putExtra("game", "דמיון בין שברים");
        startActivityForResult(i, REQUEST_CODE_FROM_SAME);
    }
}
