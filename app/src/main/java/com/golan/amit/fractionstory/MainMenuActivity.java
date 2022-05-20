package com.golan.amit.fractionstory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGoToSimilarFractions, btnGoToFractionsAdvanceAdding, btnGoToFractionAddingBeginning, btnGoToFractionRelation;
    private static final int REQUEST_CODE_FOR_ADDING_BEGINNER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        init();

        setListeners();
    }

    private void init() {
        btnGoToFractionsAdvanceAdding = findViewById(R.id.btnFractionAddAdvanceId);
        btnGoToFractionAddingBeginning = findViewById(R.id.btnFractionAddEasyId);
        btnGoToSimilarFractions = findViewById(R.id.btnFractionSimilarId);
        btnGoToFractionRelation = findViewById(R.id.btnFractionRelationId);
    }

    private void setListeners() {
        btnGoToSimilarFractions.setOnClickListener(this);
        btnGoToFractionsAdvanceAdding.setOnClickListener(this);
        btnGoToFractionAddingBeginning.setOnClickListener(this);
        btnGoToFractionRelation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        if(v == btnGoToFractionRelation) {
            i = new Intent(this, RelationFractionsActivity.class);
            startActivity(i);
        }
        else if(v == btnGoToFractionAddingBeginning) {
            i = new Intent(this, AddingFractionsActivity.class);
            i.putExtra(getString(R.string.level), getString(R.string.beginner));
            startActivityForResult(i, REQUEST_CODE_FOR_ADDING_BEGINNER);
        }
        else if (v == btnGoToFractionsAdvanceAdding) {
            i = new Intent(this, AddingFractionsActivity.class);
            startActivity(i);
        } else if (v == btnGoToSimilarFractions) {
            i = new Intent(this, SameFractionActivity.class);
            startActivity(i);
        }
    }
}
