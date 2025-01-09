package horbatiuk.yehor.nure;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout_practice);

        textView = findViewById(R.id.textView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
    }

    public void onButtonClick1(View v) {
        textView.setText("Це новий напис!");
        textView.setTextColor(Color.RED);
        Toast.makeText(this, "Текст оновлено!", Toast.LENGTH_SHORT).show();
    }

    public void onButtonClick2(View v) {
        Toast.makeText(this, "Повідомлення на екрані!", Toast.LENGTH_LONG).show();
    }
}
