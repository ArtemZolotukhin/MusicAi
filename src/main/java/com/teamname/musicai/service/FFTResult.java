package com.teamname.musicai.service;

public class FFTResult {

    private Float[] averageAmplitude;

    private Range[] ranges;

    public Float[] getAverageAmplitude() {
        return averageAmplitude;
    }

    public void setAverageAmplitude(Float[] averageAmplitude) {
        this.averageAmplitude = averageAmplitude;
    }

    public Range[] getRanges() {
        return ranges;
    }

    public void setRanges(Range[] ranges) {
        this.ranges = ranges;
    }
}
