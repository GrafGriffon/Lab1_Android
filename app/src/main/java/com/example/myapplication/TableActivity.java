package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.TableLayout;
import android.database.Cursor;

public class TableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        this.getSupportActionBar().setTitle("Results");
        //Используем цикл, в котором будем рассчитывать значения для нашей таблицы, пока вода в бассейне не закончится
        DBHelper dbHelper = new DBHelper(this);
        Cursor query = dbHelper.getData();
        while (query.moveToNext()) {
            addRow(query.getString(1),query.getString(2),query.getString(3));
        }
    }


    public void addRow(String date, String score, String user) {
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table);
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
        TextView tv = (TextView) tr.getChildAt(0);
        tv.setText(date);
        tv = (TextView) tr.getChildAt(1);
        tv.setText(score);
        tv = (TextView) tr.getChildAt(2);
        tv.setText(user);
        tableLayout.addView(tr);
    }
}