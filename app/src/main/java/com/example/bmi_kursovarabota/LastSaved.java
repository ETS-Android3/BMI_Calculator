package com.example.bmi_kursovarabota;

import androidx.annotation.CallSuper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class LastSaved extends DBActivity {

    protected ListView simpleList;
    protected Button btnBack;

    protected void FillListView() throws Exception{
        final ArrayList<String> listResults =
                new ArrayList<>();
        SelectSQL(
                "SELECT * FROM BMICALCULA ",
                null,
                (ID, Name, Height, Weight, BMI)->{
                    listResults.add(ID + " \t" + Name + " \t" + Height + " \t" + Weight + " \t"  + BMI + "\n");
                }
        );

        simpleList.clearChoices();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_listview,
                R.id.textView,
                listResults
        );

        simpleList.setAdapter(arrayAdapter);
    }


    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        try {
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BackToMain(){
        finishActivity(200);
        Intent i = new Intent(LastSaved.this, MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_saved);

        simpleList = findViewById(R.id.simpleList);
        btnBack = findViewById(R.id.btnBack);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView clickedText = view.findViewById(R.id.textView);
                String selected = clickedText.getText().toString();
                String[] elements = selected.split("\t");
                String ID = elements[0];
                String Name = elements[1];
                String Height = elements[2].trim();
                String Weight = elements[3].trim();
                String BMI = elements[4];

                Intent intent = new Intent(LastSaved.this,
                        UpdateDelete.class
                );
                Bundle b = new Bundle();
                b.putString("ID", ID);
                b.putString("Name", Name);
                b.putString("Height", Height);
                b.putString("Weight", Weight);
                b.putString("BMI", BMI);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);
            }
        });

        try {
            initDB();
            FillListView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { BackToMain(); }
        });
    }

}