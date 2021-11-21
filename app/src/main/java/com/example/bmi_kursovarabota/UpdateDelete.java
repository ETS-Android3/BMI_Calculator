package com.example.bmi_kursovarabota;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class UpdateDelete extends DBActivity {
    protected EditText editName, editHeight, editWeight;
    protected Button calc, btnUpdate, btnDelete;
    protected TextView result, resultInfo;
    protected String ID;

    private void GoToReg(){
        finishActivity(200);
        Intent i = new Intent(UpdateDelete.this, LastSaved.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        editName = findViewById(R.id.editName);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        calc = findViewById(R.id.calc);
        result = findViewById(R.id.result);
        resultInfo = findViewById(R.id.resultInfo);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            ID = b.getString("ID");
            editName.setText(b.getString("Name"));
            editHeight.setText(b.getString("Height"));
            editWeight.setText(b.getString("Weight"));
            result.setText(b.getString("BMI"));
        }

        resultInfo.setText("");

        if(resultInfo.getText().toString().trim().equals("")){
            btnUpdate.setEnabled(false);
        }else{
            btnUpdate.setEnabled(true);
        }

        btnDelete.setOnClickListener(view -> {
            try{
                ExecSQL("DELETE FROM BMICALCULA WHERE " +
                                "ID = ?",
                        new Object[]{ID},
                        ()-> Toast.makeText(getApplicationContext(),
                                "Delete Successful", Toast.LENGTH_LONG).show()
                );

            }catch (Exception exception){
                Toast.makeText(getApplicationContext(),
                        "Delete Error: " + exception.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                GoToReg();
            }
        });

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

                if(resultInfo.getText().toString().trim().equals("")){
                    btnUpdate.setEnabled(false);
                }else{
                    btnUpdate.setEnabled(true);
                }
            }
        });

        btnUpdate.setOnClickListener(view -> {
            try{
                ExecSQL("UPDATE BMICALCULA SET " +
                                "Name = ?, " +
                                "Height = ?, " +
                                "Weight = ?, " +
                                "BMI = ? " +
                                "WHERE ID = ?",
                        new Object[]{
                                editName.getText().toString(),
                                editHeight.getText().toString(),
                                editWeight.getText().toString(),
                                result.getText().toString(),
                                ID},
                        ()-> Toast.makeText(getApplicationContext(),
                                "Update Successful", Toast.LENGTH_LONG).show()
                );

            }catch (Exception exception){
                Toast.makeText(getApplicationContext(),
                        "Update Error: " + exception.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }finally {
                GoToReg();
            }
        });
    }
}