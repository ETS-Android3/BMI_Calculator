package com.example.bmi_kursovarabota;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public  class DBActivity extends AppCompatActivity {
    protected interface OnQuerySuccess {
        public void OnSuccess();
    }

    protected interface OnSelectSuccess {
        public void OnElementSelected(
                String ID, String Name, String Height, String Weight, String BMI
        );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void SelectSQL(String SelectQ,
                             String[] args,
                             OnSelectSuccess success
    )throws Exception {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath() + "/BMICALCULA.db", null);
        Cursor cursor = db.rawQuery(SelectQ, args);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String ID = cursor.getString(cursor.getColumnIndex("ID"));
            @SuppressLint("Range") String Name = cursor.getString(cursor.getColumnIndex("Name"));
            @SuppressLint("Range") String Height = cursor.getString(cursor.getColumnIndex("Height"));
            @SuppressLint("Range") String Weight = cursor.getString(cursor.getColumnIndex("Weight"));
            @SuppressLint("Range") String BMI = cursor.getString(cursor.getColumnIndex("BMI"));
            success.OnElementSelected(ID, Name, Height, Weight, BMI);
        }
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception {
        SQLiteDatabase db = SQLiteDatabase
                .openOrCreateDatabase(getFilesDir().getPath() + "/BMICALCULA.db", null);
        if (args != null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);

        db.close();
        success.OnSuccess();
    }

    protected void initDB() throws Exception {
        ExecSQL(
                "CREATE TABLE if not exists BMICALCULA( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null, " +
                        "Height text not null, " +
                        "Weight text not null, " +
                        "BMI text not null " +
                        ")",
                null,
                () -> Toast.makeText(getApplicationContext(),
                        "DB Init Successful", Toast.LENGTH_LONG).show()
        );
    }

}
