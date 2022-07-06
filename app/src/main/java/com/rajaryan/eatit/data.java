package com.rajaryan.eatit;

public class data {
    String age,gender,country,max_calories,min_calories,diabities,suger;

    public data() {
    }

    public data(String age, String gender, String country, String max_calories, String min_calories, String diabities, String suger) {
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.max_calories = max_calories;
        this.min_calories = min_calories;
        this.diabities = diabities;
        this.suger = suger;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMax_calories() {
        return max_calories;
    }

    public void setMax_calories(String max_calories) {
        this.max_calories = max_calories;
    }

    public String getMin_calories() {
        return min_calories;
    }

    public void setMin_calories(String min_calories) {
        this.min_calories = min_calories;
    }

    public String getDiabities() {
        return diabities;
    }

    public void setDiabities(String diabities) {
        this.diabities = diabities;
    }

    public String getSuger() {
        return suger;
    }

    public void setSuger(String suger) {
        this.suger = suger;
    }
}
