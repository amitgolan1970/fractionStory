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
import android.widget.TextView;
import android.widget.Toast;

public class AddingFractionsActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final int REQUEST_CODE_FROM_ADD = 5;
    TextView tvAddLeftMone, tvAddLeftMekhane, tvAddRightMone, tvAddRightMekhane;
    TextView tvAddDescription;
    EditText etAddResultMone, etAddResultMekhane;
    Button btnBackToMainFromAdding;
    ImageButton ibtnSolveAddResult, ibtnHelpShowBoard;
    AddFractionsHelper afh;
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
        setContentView(R.layout.activity_adding_fractions);

        init();

        setListeners();

        displayFractions();

        displayDescription();
    }

    private void displayDescription() {
        String descStr = "סיבוב מספר ";
        descStr += fh.getSivuv();
        if (fh.getFails() > 0) {
            if (fh.getFails() == 1) {
                descStr += " כשלון אחד";
            } else {
                descStr += " כשלונות: ";
                descStr += fh.getFails();
            }
        }

        if(MainActivity.DEBUG) {
            descStr += "\n";
            descStr += String.valueOf(afh.getAddResultMone()) + "/" + String.valueOf(afh.getAddResultMekhane());
        }
        tvAddDescription.setText(descStr);
    }

    private void displayFractions() {

        tvAddLeftMone.setText(String.valueOf(afh.getLeftMone()));
        tvAddLeftMekhane.setText(String.valueOf(afh.getLeftMekhane()));
        tvAddRightMone.setText(String.valueOf(afh.getRightMone()));
        tvAddRightMekhane.setText(String.valueOf(afh.getRightMekhane()));

    }

    private void init() {
        tvAddLeftMone = findViewById(R.id.tvAddLeftMoneId);
        tvAddLeftMekhane = findViewById(R.id.tvAddLeftMekhaneId);
        tvAddRightMone = findViewById(R.id.tvAddRightMoneId);
        tvAddRightMekhane = findViewById(R.id.tvAddRightMekhaneId);
        tvAddDescription = findViewById(R.id.tvAddDescriptionId);
        etAddResultMone = findViewById(R.id.etAddResultMoneId);
        etAddResultMone.requestFocus();
        etAddResultMekhane = findViewById(R.id.etAddResultMekhaneId);
        btnBackToMainFromAdding = findViewById(R.id.btnBackToMainFromAddingId);
        ibtnSolveAddResult = findViewById(R.id.ibtnSolveAddResultId);
        ibtnHelpShowBoard = findViewById(R.id.btnHelpAddBoardId);
        afh = new AddFractionsHelper();

        try {
            String gameLevel = getIntent().getStringExtra(getString(R.string.level));
            if (gameLevel != null && !gameLevel.isEmpty()) {
                if (gameLevel.equalsIgnoreCase(getString(R.string.beginner))) {
                    afh.setLevel(AddFractionsHelper.Level.Beginner);
                    if (MainActivity.DEBUG) {
                        Log.i(MainActivity.DEBUGTAG, "received: " + gameLevel);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(MainActivity.DEBUGTAG, "exception when setting level to beginner: " + e.getMessage());
        }


        afh.init();
        fh = new FractionHelper();
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
        btnBackToMainFromAdding.setOnClickListener(this);
        ibtnSolveAddResult.setOnClickListener(this);
        ibtnHelpShowBoard.setOnClickListener(this);
        ibtnHelpShowBoard.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == ibtnHelpShowBoard) {
            Intent i = new Intent(this, BoardShowActivity.class);
            startActivity(i);
        }
        else if(v == ibtnSolveAddResult) {

            String tmpMoneRes = etAddResultMone.getText().toString().trim();
            if(tmpMoneRes.isEmpty()) {
                Toast.makeText(this, "ערך המונה שהוכנס ריק ובלתי חוקי", Toast.LENGTH_SHORT).show();
                etAddResultMone.requestFocus();
                return;
            }
            String tmpMekhaneRes = etAddResultMekhane.getText().toString().trim();
            if(tmpMekhaneRes.isEmpty()) {
                Toast.makeText(this, "ערך המכנה שהוכנס ריק ובלתי חוקי", Toast.LENGTH_SHORT).show();
                etAddResultMekhane.requestFocus();
                return;
            }
            int tmpMoneResInt = Integer.parseInt(tmpMoneRes);
            int tmoMekhaneResInt = Integer.parseInt(tmpMekhaneRes);
            boolean anErrorMone = false;
            boolean anErrorMekhane = false;
            if(tmpMoneResInt != afh.getAddResultMone()) {
                anErrorMone = true;
            }
            if(tmoMekhaneResInt != afh.getAddResultMekhane()) {
                anErrorMekhane = true;
            }
            if(anErrorMone && anErrorMekhane) {
                Toast.makeText(this, "טעות. המונה והמכנה אשר הוקלדו אינם נכונים", Toast.LENGTH_SHORT).show();
                etAddResultMone.setText(null);
                etAddResultMekhane.setText(null);
            } else if(anErrorMone) {
                Toast.makeText(this, "טעות בערך המונה שהוכנס. המונה אינו נכון", Toast.LENGTH_SHORT).show();
                etAddResultMone.setText(null);
            } else if(anErrorMekhane) {
                Toast.makeText(this, "טעות בערך המכנה שהוכנס. המכנה אינו נכון", Toast.LENGTH_SHORT).show();
                etAddResultMekhane.setText(null);
                etAddResultMekhane.requestFocus();
            }
            if(anErrorMekhane || anErrorMone) {
                fh.increaseError();
                if(fh.getError() == (FractionHelper.ERRORS_PER_FAIL)) {
                    int tmpRnd = (int)(Math.random() * 2);
                    if(tmpRnd % 2 == 0) {
                        soundpool.play(soundLaserInt, 1, 1, 0, 0, 1);
                    } else {
                        soundpool.play(soundTromboneInt, 1, 1, 0, 0, 1);
                    }
                    String errFinal = "טעות. התשובה הייתה: ";
                    errFinal += afh.getAddResultMekhane();
                    errFinal += " / ";
                    errFinal += afh.getAddResultMone();
                    tvAddDescription.setText(errFinal);
                    etAddResultMone.requestFocus();
                    Toast.makeText(this, errFinal, Toast.LENGTH_LONG).show();
                    fh.resetError();
                    fh.increaseFails();
                    fh.increaseSivuv();
                    if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
                        endGame();
                        return;
                    } else {
                        afh.init();
                        displayFractions();
                    }
                } else {
                    soundpool.play(soundPunchInt, 1, 1, 0, 0, 1);
                    Toast.makeText(this, "טעות. תשובה שגוייה.", Toast.LENGTH_SHORT).show();
                    String err = "טעות מס ";
                    err += fh.getError();
                    tvAddDescription.setText(err);
                    etAddResultMone.requestFocus();
                }
                return;
            }
            //  GOOD !!
            if(fh.getError() == 0) {
                Toast.makeText(this, "מעולה! תשובה נכונה. כל הכבוד", Toast.LENGTH_SHORT).show();
                soundpool.play(soundApplauseInt, 1, 1, 0, 0, 1);
            } else {
                Toast.makeText(this, "יופי! תשובה נכונה.", Toast.LENGTH_SHORT).show();
                soundpool.play(soundCheeringInt, 1, 1, 0, 0, 1);
            }
            //  increase rounds and test if passed rounds

            etAddResultMone.setText(null);
            etAddResultMekhane.setText(null);
            etAddResultMone.requestFocus();
            fh.resetError();
            afh.init();
            fh.increaseSivuv();
            if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
                endGame();
            } else {
                displayFractions();
                displayDescription();
            }
        }
        else if(v == btnBackToMainFromAdding) {
            Intent i = new Intent(this, MainMenuActivity.class);
            startActivity(i);
        }
    }

    private void endGame() {
        Toast.makeText(this, "המשחק נגמר", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, SummaryActivity.class);
        i.putExtra(getString(R.string.fails), fh.getFails());
        i.putExtra("game", "חיבור שברים");
        startActivityForResult(i, REQUEST_CODE_FROM_ADD);
    }

    @Override
    public boolean onLongClick(View v) {
        if(fh.getAssist() >= FractionHelper.ASSISTANCE) {
            Toast.makeText(this, "נגמרו הרמזים", Toast.LENGTH_SHORT).show();
            return true;
        }
        String helpStr = "המכנה המשותף הוא: ";
        helpStr += afh.getAddResultMekhane();
        Toast.makeText(this, helpStr, Toast.LENGTH_LONG).show();
        fh.increaseAssist();
        return true;
    }
}
