package com.viettel.marqueetext;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private long pressedTime;
    private static int counterPressedHome = 0;
    private StringBuilder textMessage;
    private boolean threadRunning = false;
    private TextView textView;
    private int counterBackSpace = 0;
    private int start;
    private int end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
        Log.d("MainActivity Lifecycle", "onCreate");
        textMessage = new StringBuilder("Viettel Cyber Security");
    }

    @Override
    protected void onStart() {
        super.onStart();
        threadRunning = true;
        if (counterPressedHome == 0) {
            textView.setTextColor(Color.parseColor("#EA1C2D"));
        } else {
            Random random = new Random();
            textView.setTextColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
        Thread thread = new Thread(() -> {
            start = textMessage.length();
            end = textMessage.length();
            int maxString = 0;
            String backSpace = " ";

            while (threadRunning) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (start > 0) {
                    start--;
                } else {
                    counterBackSpace++;
                }

                SpannableStringBuilder subString = new SpannableStringBuilder(textMessage.subSequence(start, end));

                for (int i = 0; i < counterBackSpace; ++i) {
                    subString.insert(0, backSpace);
                }

                try {
                    textView.setText(subString);
                } catch (Exception e) {
                    maxString = counterBackSpace;
                    Log.e("BREAK", String.valueOf(counterBackSpace));
                }

                if (maxString != 0 && counterBackSpace >= maxString) {
                    end--;
                }

                if (end == 0) {
                    start = textMessage.length();
                    end = textMessage.length();
                    counterBackSpace = 0;
                }
            }
        });
        thread.start();
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back to exit", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        counterPressedHome++;
        threadRunning = false;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
