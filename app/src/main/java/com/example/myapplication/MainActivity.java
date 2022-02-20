package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.AccountPicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DBHelper helper;
    private GestureDetectorCompat gd;

    String url = "https://fakestoreapi.com/products/";

    RequestQueue rq;

    TextView tv1, tv2, tv3;
    TableRow tr;
    TableLayout tableLayout;
    LayoutInflater inflater;
    ImageView imageView;
    Context cont;
    EditText editText;

    String name, img, price, edtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("Catalog");
        //Используем цикл, в котором будем рассчитывать значения для нашей таблицы, пока вода в бассейне не закончится
        DBHelper dbHelper = new DBHelper(this);
        Cursor query = dbHelper.getData();

        rq = Volley.newRequestQueue(this);

        cont=this;
        edtext="";
        editText = findViewById(R.id.editTextTextPersonName2);
        tableLayout = findViewById(R.id.table);
        inflater = LayoutInflater.from(this);
        showTable();
    }

    public void showTable(){
        if (hasConnection(this)) {
            for (int i = 0; i < 20; i++) {
                sendjsonrequest(i);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No internet connection", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void sendjsonrequest(int num) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + num, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    name = response.getString("title");
                    img = response.getString("image");
                    price = response.getString("price");
                    if (name.contains(edtext)){
                        tr = (TableRow) inflater.inflate(R.layout.table_row, null);
                        imageView = (ImageView) tr.getChildAt(0);
                        Picasso.with(cont)
                                .load(img)
                                .placeholder(R.drawable.cat)
                                .error(R.drawable.cat)
                                .into(imageView);
                        tv1 = (TextView) tr.getChildAt(1);
                        tv2 = (TextView) tr.getChildAt(2);
                        tv1.setText(name);
                        tv2.setText(price+"$");
                        tableLayout.addView(tr);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        rq.add(jsonObjectRequest);
    }

    public void viewInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
    public void viewSearch(View view) {
        tableLayout.removeAllViews();
        edtext = editText.getText().toString();
        showTable();
    }
}