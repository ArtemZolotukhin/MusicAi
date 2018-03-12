package com.teamname.musicai.service;

public class Range {

    private float start;

    private float end;

    public Range(float start, float end) {
        this.start = start;
        this.end = end;
    }

    public Range() {
        this(0f, 0f);
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "{" + start + "; " + end + '}';
    }
}
