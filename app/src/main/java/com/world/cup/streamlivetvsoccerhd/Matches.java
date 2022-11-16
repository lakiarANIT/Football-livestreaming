package com.world.cup.streamlivetvsoccerhd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.button.MaterialButton;
import com.world.cup.streamlivetvsoccerhd.adapters.MatchesAdapters;
import com.world.cup.streamlivetvsoccerhd.models.MatchModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matches extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private MatchesAdapters matchesAdapters;
    private List<MatchModel> matchModelList;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private NativeAd native_Ads;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        // Start loading ads here...

        getData();
        loadBunnerAd();
        loadInterstitial();




        recyclerView =findViewById(R.id.recycler_main);
        matchModelList=new ArrayList<>();
        matchesAdapters=new MatchesAdapters(mInterstitialAd,this,matchModelList);

        final LinearLayoutManager layoutManager = new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void getData() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Util._base_url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i("res", "[" + response + "]");
                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObject = response.getJSONObject(i);

                        String title = jsonObject.getString("title");
                        String embed = jsonObject.getString("embed");
                        String url = jsonObject.getString("url");
                        String badge = jsonObject.getString("thumbnail");
                        String date = jsonObject.getString("date");
                        JSONArray videos = jsonObject.getJSONArray("videos");
                        JSONObject competition = jsonObject.getJSONObject("competition");

                        //setting data to list
                        MatchModel matchModel = new MatchModel(title, embed, url, badge, date, competition, videos);
                        matchModelList.add(matchModel);

                        //setting adatpte to recycler
                        recyclerView.setAdapter(matchesAdapters);


                    }
                    matchesAdapters.notifyDataSetChanged();
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            /**
             * Callback method that an error has been occurred with the provided error code and optional
             * user-readable message.
             *
             * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();

            }

            }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("x-rapidapi-key", "6ed701d63bmsh4ae9f612c1a04c7p1e3a9bjsn56bfe813e002");
                headers.put("x-rapidapi-host", "free-football-soccer-videos.p.rapidapi.com");
                return headers;
            }

        };
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //adding the string request to request queue
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    protected void onDestroy() {
        if (mInterstitialAd != null) {
            mInterstitialAd = null;
        }
        if (native_Ads != null) {
            native_Ads.destroy();
        }
        super.onDestroy();
    }



    private void loadBunnerAd() {
        mAdView = findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void loadInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        com.google.android.gms.ads.interstitial.InterstitialAd.load(this, getResources().getString(R.string.interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });
    }
}