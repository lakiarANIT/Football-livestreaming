package com.world.cup.streamlivetvsoccerhd;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.MobileAds;

public class SplashScreen extends AppCompatActivity {
    private LinearLayout mProgress,checknetAgain;
    private Button checknet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        MobileAds.initialize(this);
        mProgress = findViewById(R.id.linear_main_loading);
        checknetAgain = findViewById(R.id.linearo);
        checknet = findViewById(R.id.btn_refresh);

        checknet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
                checknetAgain.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    protected void onStart() {
        checkingInternet();
        super.onStart();
    }

    private void checkingInternet(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://shop4Me.yangu.me/gettingAllPlaces.php",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //opening dashbaord
                        new Handler().postDelayed(new Runnable() {
                            @SuppressLint("ObsoleteSdkInt")
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashScreen.this,WelcomePage.class));
                                SplashScreen.this.finish();
                            }
                        }, 2000);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            checknetAgain.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                            //This indicates that the reuest has either time out or there is no connection
                        } else if (error instanceof AuthFailureError) {
                            checknetAgain.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                            //Error indicating that there was an Authentication Failure while performing the request
                        } else if (error instanceof ServerError) {
                            checknetAgain.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                            //Indicates that the server responded with a error response
                        } else if (error instanceof NetworkError) {
                            Log.i("res", "["+error.networkResponse+"]");
                            checknetAgain.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                        } else if (error instanceof ParseError) {
                            checknetAgain.setVisibility(View.VISIBLE);
                            mProgress.setVisibility(View.GONE);
                            // Indicates that the server response could not be parsed
                        }
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}