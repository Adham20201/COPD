package com.leo.copd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PracticeActivity extends AppCompatActivity {

    ImageView imgPractice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_practice);

        imgPractice = findViewById(R.id.img_practice);

        Bundle bundle = getIntent().getExtras();
        String severity = bundle.getString("severity");

        if (severity.matches("Mild")){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.info_card_g));
            imgPractice.setImageResource(R.drawable.ic_mild);
        }
        else if (severity.matches("Moderate")){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            imgPractice.setImageResource(R.drawable.ic_moderate);
        }
        else if (severity.matches("Moderately-Severe")){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            imgPractice.setImageResource(R.drawable.ic_moderately_severe);
        }
        else if (severity.matches("Severe")){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            imgPractice.setImageResource(R.drawable.ic_severe);
        }
        else if (severity.matches("UnKnown")){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.info_card_g));
            imgPractice.setImageResource(R.drawable.ic_mild);
        }
    }
}