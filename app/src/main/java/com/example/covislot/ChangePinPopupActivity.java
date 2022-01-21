package com.example.covislot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ChangePinPopupActivity extends Activity {

    SharedPreferences sp;
    Button cancel, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin_popup);

        sp =  getSharedPreferences("login",MODE_PRIVATE);

        cancel = findViewById(R.id.button_cancel_popup);
        confirm = findViewById(R.id.button_change_popup);

        EditText newPin = findViewById(R.id.popup_new_pin);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;
        getWindow().setLayout((int)(width*(0.8)), (int)(height*(0.4)));

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newPin.getText().toString()) || newPin.getText().length() != 6) {
                    // when field is empty
                    // displaying a toast message.
                    Toast.makeText(ChangePinPopupActivity.this, "Please enter a Pin Number.", Toast.LENGTH_SHORT).show();
                } else {
                    String s = newPin.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("pinCode", s);
                    editor.apply();
                    Toast.makeText(ChangePinPopupActivity.this, "Pin Code Updated!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        });
    }
}