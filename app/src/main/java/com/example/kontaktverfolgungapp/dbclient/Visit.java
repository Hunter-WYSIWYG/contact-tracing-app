package com.example.kontaktverfolgungapp.dbclient;

public class Visit {

    private int PID;
    private String PlaceName;
    private String DateTime;
    private double Timeframe;

    public Visit(int PID, String PlaceName, String DateTime, double Timeframe) {
        this.PID = PID;
        this.PlaceName = PlaceName;
        this.DateTime = DateTime;
        this.Timeframe = Timeframe;
    }

    public int getPID() {
        return this.PID;
    }

    public String getPlaceName() {
        return this.PlaceName;
    }

    public String getDateTime() {
        return this.DateTime;
    }

    public double getTimeframe() {
        return this.Timeframe;
    }
}
