package com.rajaryan.eatit;

public class Nutritions {
    String amount,percentOfDailyNeeds,name;

    public Nutritions() {
    }

    public Nutritions(String amount, String percentOfDailyNeeds, String name) {
        this.amount = amount;
        this.percentOfDailyNeeds = percentOfDailyNeeds;
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercentOfDailyNeeds() {
        return percentOfDailyNeeds;
    }

    public void setPercentOfDailyNeeds(String percentOfDailyNeeds) {
        this.percentOfDailyNeeds = percentOfDailyNeeds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
