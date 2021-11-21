package com.example.bmi_kursovarabota;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends DBActivity {
    protected EditText editName, editHeight, editWeight;
    protected Button calc, btnSave, btnReg;
    protected TextView result, resultInfo;

    private void GoToReg(){
        finishActivity(200);
        Intent i = new Intent(MainActivity.this, LastSaved.class);
        startActivity(i);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        result = findViewById(R.id.result);
        resultInfo = findViewById(R.id.resultInfo);
        calc = findViewById(R.id.calc);
        btnSave = findViewById(R.id.btnSave);
        btnReg = findViewById(R.id.btnReg);

        try {
            initDB();
        } catch (Exception e) {
            e.printStackTrace();
        }

        result.setText("");

        if(result.getText().toString().trim().equals("")){
            btnSave.setEnabled(false);
        }else{
            btnSave.setEnabled(true);
        }

        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String heStr = editHeight.getText().toString();
                String weStr = editWeight.getText().toString();

                if (heStr != null && !"".equals(heStr)
                        && weStr != null && !"".equals(weStr)) {
                    float heValue = Float.parseFloat(heStr);
                    float weValue = Float.parseFloat(weStr);

                    float bmi = weValue / ((heValue / 100) * (heValue / 100));

                    displayBMI(bmi);
                }
            }

            public void displayBMI(float bmi) {
                String BMIRes;
                String bmiLabel = "";

                if (Float.compare(bmi, 16f) <= 0) {
                    bmiLabel = getString(R.string.severely_underweight);
                } else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <= 0) {
                    bmiLabel = getString(R.string.underweight);
                } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0) {
                    bmiLabel = getString(R.string.normal_weight);
                } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0) {
                    bmiLabel = getString(R.string.overweight);
                } else if (Float.compare(bmi, 30f) >= 0) {
                    bmiLabel = getString(R.string.obese);
                }

                BMIRes = bmi + "  ";
                result.setText(BMIRes);
                resultInfo.setText(bmiLabel);

                if(result.getText().toString().trim().equals("")){
                    btnSave.setEnabled(false);
                }else{
                    btnSave.setEnabled(true);
                }
            }
        });

        btnSave.setOnClickListener(view -> {
            try {
                ExecSQL(
                        "INSERT INTO BMICALCULA(Name, Height, Weight, BMI) " +
                                "VALUES(?, ?, ?, ?) ",
                        new Object[]{
                                editName.getText().toString(),
                                editHeight.getText().toString(),
                                editWeight.getText().toString(),
                                result.getText()
                        },
                        () -> Toast.makeText(getApplicationContext(),
                                "Record Inserted", Toast.LENGTH_LONG).show()
                );
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Insert Failed: " + e.getLocalizedMessage()
                        , Toast.LENGTH_SHORT).show();
            }

            editName.setText("");
            editHeight.setText("");
            editWeight.setText("");
            result.setText("");
            resultInfo.setText("");

            if(result.getText().toString().trim().equals("")){
                btnSave.setEnabled(false);
            }else{
                btnSave.setEnabled(true);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { GoToReg(); }
        });

    }
}

