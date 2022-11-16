package com.world.cup.streamlivetvsoccerhd.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.world.cup.streamlivetvsoccerhd.MatchEvent;
import com.world.cup.streamlivetvsoccerhd.Matches;
import com.world.cup.streamlivetvsoccerhd.R;
import com.world.cup.streamlivetvsoccerhd.models.MatchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MatchesAdapters extends RecyclerView.Adapter<MatchesAdapters.ViewHolder>{

    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    private Context context;
    private List<MatchModel> matchModelList;

    public MatchesAdapters(InterstitialAd mInterstitialAd, Context context, List<MatchModel> matchModelList) {
        this.mInterstitialAd = mInterstitialAd;
        this.context = context;
        this.matchModelList = matchModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String title = matchModelList.get(position).getMatchTitle();
        final String embed = matchModelList.get(position).getvEmbed();
        final String url = matchModelList.get(position).getUrl();
        final String badge = matchModelList.get(position).gethBadge();
        final String date = matchModelList.get(position).getDate();
        final JSONObject competition = matchModelList.get(position).getCompetition();
        final JSONArray videos = matchModelList.get(position).getVideos();

        try {
            holder.settingValues(title, competition.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("\n\n\nTHIS IS THE TEST "+title+" have been click\n\n\n");
                if (mInterstitialAd != null) {
                    // Show the ad
                    mInterstitialAd.show((Activity) context);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            holder.passValues(title,embed,url,badge,date,competition,videos);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                } else {
                    holder.passValues(title,embed,url,badge,date,competition,videos);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return matchModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView matchTitle, matchCompetiton;
        private LinearLayout mLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            matchCompetiton = itemView.findViewById(R.id.match_c);
            matchTitle = itemView.findViewById(R.id.match_t);
            mLinearLayout = itemView.findViewById(R.id.linear_main);
        }

        private void settingValues(String title, String competiton) {
            matchCompetiton.setText(competiton);
            matchTitle.setText(title);
        }

        private void passValues(String matchTitle,String embed,String url, String badge,String date, JSONObject competition,JSONArray videos){

            Intent intent = new Intent(context, MatchEvent.class);
            intent.putExtra("title", matchTitle);
            intent.putExtra("url", url);
            intent.putExtra("badge", badge);
            intent.putExtra("date", date);
            intent.putExtra("videos", videos.toString());
            intent.putExtra("competition", competition.toString());
            intent.putExtra("embed", embed);
            if (Build.VERSION.SDK_INT > 20) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context);
                context.startActivity(intent, options.toBundle());
            } else {
                context.startActivity(intent);
            }
        }


    }
}
