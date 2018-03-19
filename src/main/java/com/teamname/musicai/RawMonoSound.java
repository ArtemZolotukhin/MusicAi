package com.teamname.musicai;

import java.util.ArrayList;
import java.util.List;

public class RawMonoSound {
    private List<Float> raw;
    private int sampleRate;
    private int sampleSizeInBytes;

    public RawMonoSound(List<Float> raw, float sampleRate, int sampleSizeInBytes) {
        this.raw = raw;
        this.sampleRate = (int) sampleRate;
        this.sampleSizeInBytes = sampleSizeInBytes;
    }

    public RawMonoSound() {
    }

    public List<Float> getRaw() {
        return raw;
    }

    public void setRaw(ArrayList<Float> raw) {
        this.raw = raw;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSampleSizeInBytes() {
        return sampleSizeInBytes;
    }

    public void setSampleSizeInBytes(int sampleSizeInBytes) {
        this.sampleSizeInBytes = sampleSizeInBytes;
    }
}
