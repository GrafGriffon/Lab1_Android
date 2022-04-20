package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CatActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    int currentTup = 1;
    private DBHelper helper;
    private GestureDetectorCompat gd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        helper = new DBHelper(this);
        setContentView(R.layout.activity_cat);
        gd = new GestureDetectorCompat(this, this);
        updateScore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        gd.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void sendMessage(View view) {
        TextView textView = (TextView) findViewById(R.id.textViewScore);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "FeedTheCat: My result - "+ Integer.parseInt(textView.getText().toString()));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void sendClick(View view) {

        TextView textView = (TextView) findViewById(R.id.textViewScore);
//        if (currentTup % 15 == 0) {
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);
        String date = dateText + "--" + timeText;
        ImageView img = (ImageView) findViewById(R.id.imageView);

        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(1 / 2);
        anim.setDuration(700);
        img.startAnimation(anim);
        int num = Integer.parseInt(textView.getText().toString());
        num++;
        helper.addData(date,num+"",mAuth.getCurrentUser().getEmail());
        textView.setText(num + "");
//        }
        currentTup++;
    }

    public void viewTable(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Intent intent = new Intent(this, TableActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    private void updateScore(){
        DBHelper dbHelper = new DBHelper(this);
        Cursor query = dbHelper.getData();
        String num = null;
        while (query.moveToNext()) {
            if (query.getString(3).equals(mAuth.getCurrentUser().getEmail())) {
                num = query.getString(2);
            }
        }
        TextView textView = (TextView) findViewById(R.id.textViewScore);
        textView.setText(num);
        query.close();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}