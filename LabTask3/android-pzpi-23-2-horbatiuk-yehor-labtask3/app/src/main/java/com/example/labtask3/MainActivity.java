package com.example.labtask3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private String currentNumber = "";
    private String previousNumber = "";
    private String operation = "";
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        setNumberButtonListeners();
        setOperationButtonListeners();
    }

    private void setNumberButtonListeners() {
        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);


        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            if (isNewOperation) {
                currentNumber = "";
                isNewOperation = false;
            }
            currentNumber += button.getText().toString();
            tvResult.setText(currentNumber);
        };


        btn0.setOnClickListener(listener);
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(listener);
        btn6.setOnClickListener(listener);
        btn7.setOnClickListener(listener);
        btn8.setOnClickListener(listener);
        btn9.setOnClickListener(listener);
    }


    private void setOperationButtonListeners() {
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnSubtract = findViewById(R.id.btnSubtract);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);
        Button btnEquals = findViewById(R.id.btnEquals);


        btnAdd.setOnClickListener(this::onOperationClick);
        btnSubtract.setOnClickListener(this::onOperationClick);
        btnMultiply.setOnClickListener(this::onOperationClick);
        btnDivide.setOnClickListener(this::onOperationClick);
        btnEquals.setOnClickListener(this::onOperationClick);


        findViewById(R.id.btnClear).setOnClickListener(view -> {
            currentNumber = "";
            previousNumber = "";
            operation = "";
            tvResult.setText("0");
        });
    }


    private void onOperationClick(View view) {
        Button button = (Button) view;
        String op = button.getText().toString();

        if (!op.equals("=")) {
            operation = op;
            previousNumber = currentNumber;
            currentNumber = "";
        } else {
            double num1 = Double.parseDouble(previousNumber);
            double num2 = Double.parseDouble(currentNumber);

            double result = 0;
            switch (operation) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
                case "/":
                    result = num2 != 0 ? num1 / num2 : 0;
                    break;
            }

            tvResult.setText(String.valueOf(result));
            currentNumber = String.valueOf(result);
            isNewOperation = true;
        }
    }
}
