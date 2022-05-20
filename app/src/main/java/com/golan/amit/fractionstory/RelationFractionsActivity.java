package com.golan.amit.fractionstory;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RelationFractionsActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    public static final int REQUEST_CODE_FROM_ADD = 7;
    ImageView ivBtnHelp;
    ImageView ivBtnLess, ivBtnEqualSign, ivBtnBigger;
    Button btnGoBackToMenuFromRelation;
    TextView tvRelationLeftMone, tvRelationLeftMekhane;
    TextView tvRelationRightMone, tvRelationRightMekhane;
    TextView tvRelationDescription;
    RelationFractionsHelper rfh;
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
        setContentView(R.layout.activity_relation_fractions);

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
        tvRelationDescription.setText(descStr);
    }

    private void displayFractions() {
        tvRelationLeftMone.setText(String.valueOf(rfh.getLeftRelationMone()));
        tvRelationLeftMekhane.setText(String.valueOf(rfh.getLeftRelationMekhane()));
        tvRelationRightMone.setText(String.valueOf(rfh.getRightRelationMone()));
        tvRelationRightMekhane.setText(String.valueOf(rfh.getRightRelationMekhane()));
    }

    private void setListeners() {
        btnGoBackToMenuFromRelation.setOnClickListener(this);
        ivBtnHelp.setOnLongClickListener(this);
    }

    private void init() {
        ivBtnHelp = findViewById(R.id.ibtnRelationHelpId);
        ivBtnLess = findViewById(R.id.ibtnLessRelationId);
        ivBtnEqualSign = findViewById(R.id.ibtnEqualRelationId);
        ivBtnBigger = findViewById(R.id.ibtnBiggerRelationId);
        btnGoBackToMenuFromRelation = findViewById(R.id.btnBackToMenuFromRelationId);

        tvRelationLeftMone = findViewById(R.id.tvRelationLeftMoneId);
        tvRelationLeftMekhane = findViewById(R.id.tvRelationLeftMekhaneId);
        tvRelationRightMone = findViewById(R.id.tvRightRelationMoneId);
        tvRelationRightMekhane = findViewById(R.id.tvRightRelationMekhaneId);
        tvRelationDescription = findViewById(R.id.tvRelationDescId);

        rfh = new RelationFractionsHelper();
        rfh.init();
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

    /**
     * Help
     * @param v
     */

    public void btnHelp_click(View v) {
        Intent i = new Intent(this, BoardShowActivity.class);
        startActivity(i);
    }

    /**
     * Evaluations
     * @param v
     */

    public void btnLess_click(View v) {
        if(rfh.isLeftLessThanRight()) {
            trueAnswer();
        } else {
            wrongAnswer();
        }
    }

    public void btnEqual_click(View v) {
        if(rfh.areEquals()) {
            trueAnswer();
        } else {
            wrongAnswer();
        }
    }

    public void btnBigger_click(View v) {
        if(rfh.isLeftBiggerThanRight()) {
            trueAnswer();
        } else {
            wrongAnswer();
        }
    }

    private void wrongAnswer() {
        fh.increaseError();
        if(fh.getError() == (FractionHelper.ERRORS_PER_FAIL - 2)) { //  ONLY 1 attemp permitted since it's a 3 choices answer.
            int tmpRnd = (int)(Math.random() * 2);
            if(tmpRnd % 2 == 0) {
                soundpool.play(soundLaserInt, 1, 1, 0, 0, 1);
            } else {
                soundpool.play(soundTromboneInt, 1, 1, 0, 0, 1);
            }

            String errFinal = "טעות. ";
            if(rfh.isLeftLessThanRight()) {
                errFinal += "השבר השמאלי קטן היה מהשבר הימני";
            } else if(rfh.isLeftBiggerThanRight()) {
                errFinal += "השבר השמאלי גדול היה מהשבר הימני";
            } else if(rfh.areEquals()) {
                errFinal += "השברים היו שווים";
            }
            tvRelationDescription.setText(errFinal);

            fh.resetError();
            fh.increaseFails();
            fh.increaseSivuv();
            if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
                endGame();
                return;
            } else {
                rfh.init();
                displayFractions();
            }
        } else {    //  in this 'game', this section is not invoked. see comment above.
            soundpool.play(soundPunchInt, 1, 1, 0, 0, 1);
            Toast.makeText(this, "טעות. תשובה שגוייה.", Toast.LENGTH_SHORT).show();
            String err = "טעות מס ";
            err += fh.getError();
            tvRelationDescription.setText(err);
        }
    }

    private void trueAnswer() {
        if(fh.getError() == 0) {
            Toast.makeText(this, "מעולה! תשובה נכונה. כל הכבוד", Toast.LENGTH_SHORT).show();
            soundpool.play(soundApplauseInt, 1, 1, 0, 0, 1);
        } else {
            Toast.makeText(this, "יופי! תשובה נכונה.", Toast.LENGTH_SHORT).show();
            soundpool.play(soundCheeringInt, 1, 1, 0, 0, 1);
        }

        fh.resetError();
        rfh.init();
        fh.increaseSivuv();
        if(fh.getSivuv() == (FractionHelper.ROUNDS + 1)) {
            endGame();
        } else {
            displayFractions();
            displayDescription();
        }
    }

    private void endGame() {
        Toast.makeText(this, "המשחק נגמר", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this, SummaryActivity.class);
        i.putExtra(getString(R.string.fails), fh.getFails());
        i.putExtra("game", "יחסים של שברים");
        startActivityForResult(i, REQUEST_CODE_FROM_ADD);
    }

    @Override
    public void onClick(View v) {
        if(v == btnGoBackToMenuFromRelation) {
            Intent i = new Intent(this, MainMenuActivity.class);
            startActivity(i);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(v == ivBtnHelp) {
            if(fh.getAssist() == FractionHelper.ASSISTANCE) {
                Toast.makeText(this, "נגמרו הרמזים", Toast.LENGTH_SHORT).show();
                return true;
            }
            if(rfh.getLeftRelationMekhane() == rfh.getRightRelationMekhane()) {
                Toast.makeText(this, "כבר מוצג מכנה משותף", Toast.LENGTH_SHORT).show();
                return true;
            }
            fh.increaseAssist();
//            tvRelationLeftMone.setText(String.valueOf(rfh.getCalculatedCommonLeftMone()));
//            tvRelationLeftMekhane.setText(String.valueOf(rfh.getCommonBase()));
//            tvRelationRightMone.setText(String.valueOf(rfh.getCalculatedCommonRightMone()));
//            tvRelationRightMekhane.setText(String.valueOf(rfh.getCommonBase()));
            new HelpDisplay().execute();
        }
        return true;
    }

    public class HelpDisplay extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvRelationLeftMone.setText(String.valueOf(rfh.getCalculatedCommonLeftMone()));
            tvRelationLeftMekhane.setText(String.valueOf(rfh.getCommonBase()));
            tvRelationRightMone.setText(String.valueOf(rfh.getCalculatedCommonRightMone()));
            tvRelationRightMekhane.setText(String.valueOf(rfh.getCommonBase()));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            displayFractions();
        }
    }
}
