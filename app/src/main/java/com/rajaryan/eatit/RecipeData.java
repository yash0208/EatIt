package com.rajaryan.eatit;

public class RecipeData {
    String id,title,image,time;

    public RecipeData() {
    }

    public RecipeData(String id, String title, String image, String time) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
