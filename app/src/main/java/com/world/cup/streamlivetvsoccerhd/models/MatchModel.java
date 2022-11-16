package com.world.cup.streamlivetvsoccerhd.models;

import org.json.JSONArray;
import org.json.JSONObject;

public class MatchModel {
    private String matchTitle,vEmbed,url,hBadge,date;
    private JSONObject competition;
    private JSONArray videos;

    public MatchModel(String matchTitle, String vEmbed, String url, String hBadge, String date, JSONObject competition, JSONArray videos) {
        this.matchTitle = matchTitle;
        this.vEmbed = vEmbed;
        this.url = url;
        this.hBadge = hBadge;
        this.date = date;
        this.competition = competition;
        this.videos = videos;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public void setMatchTitle(String matchTitle) {
        this.matchTitle = matchTitle;
    }

    public String getvEmbed() {
        return vEmbed;
    }

    public void setvEmbed(String vEmbed) {
        this.vEmbed = vEmbed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String gethBadge() {
        return hBadge;
    }

    public void sethBadge(String hBadge) {
        this.hBadge = hBadge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JSONObject getCompetition() {
        return competition;
    }

    public void setCompetition(JSONObject competition) {
        this.competition = competition;
    }

    public JSONArray getVideos() {
        return videos;
    }

    public void setVideos(JSONArray videos) {
        this.videos = videos;
    }
}
