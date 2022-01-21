package com.example.covislot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AdditionalDetails extends AppCompatActivity {

    Button submit;
    private EditText name, age;
    CheckBox fDose;
    SharedPreferences sp;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additional_detail_layout);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        sp = getSharedPreferences("login", MODE_PRIVATE);

        submit = findViewById(R.id.submit_details_button);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        fDose = findViewById(R.id.first_dose_detail);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(age.getText().toString())) {
                    //If any of name or age field is empty
                    // prompt to enter them.
                    Toast.makeText(AdditionalDetails.this, "Name & Age values cant be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    String number, uName;
                    int uAge;
                    boolean firstDose;
                    number = sp.getString("phone", "");
                    uName = name.getText().toString();
                    uAge = Integer.parseInt(age.getText().toString());
                    firstDose = fDose.isChecked();

                    UserInfo user = new UserInfo(uName, number, uAge, firstDose);

                    writeUserData(user, number);
                    sp.edit().putString("name", user.getName()).apply();
                    sp.edit().putInt("age", user.getAge()).apply();
                    sp.edit().putBoolean("firstDose", user.isFirstDose()).apply();

                    startActivity(new Intent(AdditionalDetails.this,SignedInActivity.class));
                }
            }
        });
    }

    public void writeUserData(UserInfo user, String num) {
        //TODO: Change security databse rules
        mDatabase.child("Users").child(num).setValue(user);
    }
}
