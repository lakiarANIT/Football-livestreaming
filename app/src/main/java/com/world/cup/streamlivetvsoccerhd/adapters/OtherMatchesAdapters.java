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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.squareup.picasso.Picasso;
import com.world.cup.streamlivetvsoccerhd.MatchEvent;
import com.world.cup.streamlivetvsoccerhd.R;
import com.world.cup.streamlivetvsoccerhd.models.MatchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OtherMatchesAdapters extends RecyclerView.Adapter<OtherMatchesAdapters.ViewHolder>{

    private com.google.android.gms.ads.interstitial.InterstitialAd mInterstitialAd;
    private Context context;
    private List<MatchModel> matchModelList;

    public OtherMatchesAdapters(InterstitialAd mInterstitialAd, Context context, List<MatchModel> matchModelList) {
        this.mInterstitialAd = mInterstitialAd;
        this.context = context;
        this.matchModelList = matchModelList;
    }

    @NonNull
    @Override
    public OtherMatchesAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        context = parent.getContext();
        return new OtherMatchesAdapters.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OtherMatchesAdapters.ViewHolder holder, int position) {
        final String title = matchModelList.get(position).getMatchTitle();
        final String embed = matchModelList.get(position).getvEmbed();
        final String url = matchModelList.get(position).getUrl();
        final String badge = matchModelList.get(position).gethBadge();
        final String date = matchModelList.get(position).getDate();
        final JSONObject competition = matchModelList.get(position).getCompetition();
        final JSONArray videos = matchModelList.get(position).getVideos();

        holder.settingValues(badge, title);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        private TextView mTitle;
        private ImageView mWebView;
        private ProgressBar progressBar;
        private ConstraintLayout mLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.tv_title);
            mWebView = itemView.findViewById(R.id.webview);
            progressBar = itemView.findViewById(R.id.progressBar);
            mLinearLayout = itemView.findViewById(R.id.mycond);
        }

        private void settingValues(String bage,String title){
            mTitle.setText(title);
            Picasso.get().load(bage).into(mWebView);


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
