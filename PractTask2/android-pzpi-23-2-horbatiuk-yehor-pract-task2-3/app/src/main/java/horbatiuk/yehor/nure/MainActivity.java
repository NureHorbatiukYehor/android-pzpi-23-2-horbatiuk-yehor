package horbatiuk.yehor.nure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private TextView counterText;
    private int counter = 0;
    private Handler timerHandler = new Handler();
    private int seconds = 0;
    private boolean isTimerRunning = false;
    private Runnable timerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate called");

        editText = findViewById(R.id.editText);
        counterText = findViewById(R.id.counterText);
        Button btnIncrement = findViewById(R.id.btnIncrement);
        Button btnNextActivity = findViewById(R.id.btnNextActivity);
        Button btnFinish = findViewById(R.id.btnFinish);
        TextView timerText = findViewById(R.id.timerText);

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter", 0);
            seconds = savedInstanceState.getInt("seconds", 0);
            editText.setText(savedInstanceState.getString("editText", ""));
            counterText.setText("Counter: " + counter);
            timerText.setText("Timer: " + seconds + "s");
        }

        btnIncrement.setOnClickListener(v -> {
            counter++;
            counterText.setText("Counter: " + counter);
        });

        btnNextActivity.setOnClickListener(v ->
                startActivity(new Intent(this, SecondActivity.class)));

        btnFinish.setOnClickListener(v -> finish());

        // Timer implementation
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTimerRunning) {
                    seconds++;
                    timerText.setText("Timer: " + seconds + "s");
                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
        startTimer();
    }

    private void startTimer() {
        isTimerRunning = true;
        timerHandler.removeCallbacks(timerRunnable); // Add this line
        timerHandler.post(timerRunnable);
    }

    private void pauseTimer() {
        isTimerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("counter", counter);
        outState.putInt("seconds", seconds);
        outState.putString("editText", editText.getText().toString());
        Log.d(TAG, "onSaveInstanceState called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        pauseTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }
}