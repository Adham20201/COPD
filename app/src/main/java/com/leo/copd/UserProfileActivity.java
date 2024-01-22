package com.leo.copd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    RelativeLayout btnUpdate;
    LottieAnimationView btnUpdate_animation;
    TextView txt_severity_level, txt_action, name_field, btnUpdate_txt ;
    CardView btnAction;

    String severityLevel;

    FirebaseAuth mAuth;
    DatabaseReference database;
    Users user;

    ProgressBar progressBar;
    LinearLayout container;

    TextInputLayout infoAge, infoMWT1, infoPackHistory, infoFEV1, infoFVC, infoSmoking, infoGender;

    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.color_primary));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_user_profile);

        btnUpdate_txt = findViewById(R.id.btn_update_txt);
        btnUpdate_animation = findViewById(R.id.btn_update_animation);

        infoAge = findViewById(R.id.info_Age);
        infoMWT1 = findViewById(R.id.info_MWT1);
        infoPackHistory = findViewById(R.id.info_Packhistory);
        infoFEV1 = findViewById(R.id.info_FEV1);
        infoFVC = findViewById(R.id.info_FVC);
        infoSmoking = findViewById(R.id.info_Smoking);
        infoGender = findViewById(R.id.info_Gender);


        name_field = findViewById(R.id.fullname_field);

        progressBar = findViewById(R.id.progressBar);
        container = findViewById(R.id.profile_container);

        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://copd-44e63-default-rtdb.firebaseio.com/").getReference("Users").child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Users.class);
                name_field.setText(user.getFirstName() + " " + user.getLastName());

                database.child("Info").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userInfo = dataSnapshot.getValue(UserInfo.class);

                        if (userInfo != null){
                            infoAge.getEditText().setText(userInfo.getAge());
                            infoMWT1.getEditText().setText(userInfo.getMwt1());
                            infoPackHistory.getEditText().setText(userInfo.getPackHistory());
                            infoFEV1.getEditText().setText(userInfo.getFev1());
                            infoFVC.getEditText().setText(userInfo.getFvc());
                            infoSmoking.getEditText().setText(userInfo.getSmoking());
                            infoGender.getEditText().setText(userInfo.getGender());

                            Python py = Python.getInstance();
                            PyObject pyobj = py.getModule("script");
                            PyObject obj = pyobj.callAttr("main", userInfo.getAge(),userInfo.getMwt1(),userInfo.getPackHistory(),userInfo.getFev1(),userInfo.getFvc(),userInfo.getSmoking(),userInfo.getGender());

                            SeverityLevel(obj.toString());
                        }

                        progressBar.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        container.setVisibility(View.VISIBLE);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        severityLevel = "Mild";

        btnUpdate = findViewById(R.id.btn_update);
        btnAction = findViewById(R.id.btn_action);
        txt_severity_level = findViewById(R.id.txt_severity_level);
        txt_action = findViewById(R.id.txt_action);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSeverityLevel();

            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, PracticeActivity.class);
                intent.putExtra("severity", severityLevel);
                startActivity(intent);
            }
        });


    }

    private void updateSeverityLevel() {

        String age = infoAge.getEditText().getText().toString().trim();
        String mwt1 = infoMWT1.getEditText().getText().toString().trim();
        String packHistory = infoPackHistory.getEditText().getText().toString().trim();
        String fev1 = infoFEV1.getEditText().getText().toString().trim();
        String fvc = infoFVC.getEditText().getText().toString().trim();
        String smoking = infoSmoking.getEditText().getText().toString().trim();
        String gender = infoGender.getEditText().getText().toString().trim();


        btnUpdate_txt.setVisibility(View.GONE);
        btnUpdate_animation.setVisibility(View.VISIBLE);
        btnUpdate_animation.playAnimation();

        userInfo = new UserInfo(age, mwt1, packHistory, fev1, fvc, smoking, gender);
        database.child("Info").setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                btnUpdate_txt.setVisibility(View.VISIBLE);
                btnUpdate_animation.setVisibility(View.GONE);
                btnUpdate_animation.pauseAnimation();

                if (task.isSuccessful()){
                    Python py = Python.getInstance();
                    PyObject pyobj = py.getModule("script");
                    PyObject obj = pyobj.callAttr("main", age,mwt1,packHistory,fev1,fvc,smoking,gender);
                    Toast.makeText(UserProfileActivity.this,obj.toString(),Toast.LENGTH_LONG).show();
                    SeverityLevel(obj.toString());
                }
            }
        });


    }
    private void SeverityLevel(String severity) {
        if (severity.matches("1")){
            severityLevel = "Mild";
            txt_action.setText("Good");
            btnAction.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_card_g));
        }
        else if (severity.matches("2")){
            severityLevel = "Moderate";
            txt_action.setText("Go Practice");
            btnAction.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_card_y));
        }
        else if (severity.matches("3")){
            severityLevel = "Moderately-Severe";
            txt_action.setText("Go Practice");
            btnAction.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_card_y));
        }
        else if (severity.matches("4")){
            severityLevel = "Severe";
            txt_action.setText("Go Practice");
            btnAction.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_card_y));
        }
        else {
            severityLevel = "UnKnown";
            txt_action.setText("UnKnown");
            btnAction.setCardBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.color_card_g));
        }
        txt_severity_level.setText(severityLevel);
    }
}