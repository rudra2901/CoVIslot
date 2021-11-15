 package com.example.covislot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mobileField, pinField,otpField;
    Button getOTP, verifyOTP;
    private String mobileNumber, pinCode, OTP;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    FirebaseAuth auth;
    private String verificationCode;
    private DatabaseReference mDatabase;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        //TODO: Check if data is present in db, and if not enter it

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        //mDatabase.child("Users");
        //mDatabase.setValue("Hello World");

        sp =  getSharedPreferences("login",MODE_PRIVATE);

        StartFirebaseLogin();

        mobileField = findViewById(R.id.mobile);
        pinField = findViewById(R.id.pin_code);
        otpField = findViewById(R.id.gen_otp);

        getOTP = findViewById(R.id.get_otp_button);
        verifyOTP = findViewById(R.id.verify_otp_button);

        getOTP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (TextUtils.isEmpty(mobileField.getText().toString()) || mobileField.getText().length() != 10) {
                    // when mobile number field is empty
                    // displaying a toast message.
                    Toast.makeText(RegistrationActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    mobileNumber = "+91" + mobileField.getText().toString();
                }
                if (TextUtils.isEmpty(pinField.getText().toString()) || pinField.getText().length() != 6) {
                    // when pin number field is empty
                    // displaying a toast message.
                    Toast.makeText(RegistrationActivity.this, "Please enter a valid Pin code.", Toast.LENGTH_SHORT).show();
                } else {
                    pinCode = pinField.getText().toString();
                }

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber(mobileNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(RegistrationActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(otpField.getText().toString())) {
                    // when otp field is empty
                    // displaying a toast message.
                    Toast.makeText(RegistrationActivity.this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                } else {
                    OTP = otpField.getText().toString();
                }
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP);
                SignInWithPhone(credential);
            }
         });

    }

    private void SignInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sp.edit().putBoolean("logged", true).apply();
                            sp.edit().putString("phone", mobileNumber).apply();
                            sp.edit().putString("pinCode", pinCode).apply();

                            /*
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            Query query = ref.child("Users").orderByChild("number").equalTo(mobileNumber);

                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        //TODO: get the data here
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }
                            };
                            query.addValueEventListener(valueEventListener);
                             */

                            //startActivity(new Intent(RegistrationActivity.this,SignedInActivity.class));
                            startActivity(new Intent(RegistrationActivity.this,AdditionalDetails.class));
                            finish();
                        } else {
                            Toast.makeText(RegistrationActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(RegistrationActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(RegistrationActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(RegistrationActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
                getOTP.animate().alpha(0f).setDuration(500);
                otpField.setVisibility(View.VISIBLE);
                otpField.animate().alpha(1f).setDuration(500);
                otpField.requestFocus();
                verifyOTP.setVisibility(View.VISIBLE);
                verifyOTP.animate().alpha(1f).setDuration(600);
            }
        };
    }
}