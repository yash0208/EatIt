package com.rajaryan.eatit;

public class VideoData {
    String title,youTubeId,rating,views,thumbnail,length;

    public VideoData() {
    }

    public VideoData(String title, String youTubeId, String rating, String views, String thumbnail, String length) {
        this.title = title;
        this.youTubeId = youTubeId;
        this.rating = rating;
        this.views = views;
        this.thumbnail = thumbnail;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYouTubeId() {
        return youTubeId;
    }

    public void setYouTubeId(String youTubeId) {
        this.youTubeId = youTubeId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
