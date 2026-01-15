package com.example.ferrol_app;

import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("player") // Traduce "player" del JSON a la variable userName
    private String userName;

    @SerializedName("points") // Traduce "points" del JSON a la variable points
    private int points;

    public Score(String userName, int points) {
        this.userName = userName;
        this.points = points;
    }

    public String getUserName() { return userName; }
    public int getPoints() { return points; }
}