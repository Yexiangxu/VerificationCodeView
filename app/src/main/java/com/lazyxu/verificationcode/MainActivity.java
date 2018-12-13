package com.lazyxu.verificationcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lazyxu.verificationcodelibrary.VerificationCodeView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VerificationCodeView input = findViewById(R.id.verificationCodeInput);
        input.setOnCompleteListener(new VerificationCodeView.Listener() {
            @Override
            public void onComplete(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
