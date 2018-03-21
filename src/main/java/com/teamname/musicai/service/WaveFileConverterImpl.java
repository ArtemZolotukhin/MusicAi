package com.teamname.musicai.service;

import com.sun.org.apache.bcel.internal.generic.FLOAD;
import com.teamname.musicai.RawMonoSound;
import com.teamname.musicai.WaveFile;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaveFileConverterImpl implements WaveFileConverter {

    public static final int INT_SIZE = 4;


    public RawMonoSound toRawMonoSound(WaveFile waveFile, int startSec, int endSec) {



        AudioFormat audioFormat = waveFile.getAudioFormat();

        int byteRate = audioFormat.getFrameSize() / audioFormat.getChannels(); // кол-во байт на отсчет ( разрядность / 8 )

        int sampleRate = Math.round(waveFile.getAudioFormat().getSampleRate());

        if (byteRate > INT_SIZE) {
            throw new IllegalArgumentException("framSize too big");
        }

        int channels = audioFormat.getChannels(); // кол-во каналов стерео | моно

        double duration = waveFile.getDurationTime();
        startSec = (int) Math.floor(Math.min(startSec, duration));
        startSec = (int) Math.round(Math.max(0, startSec));
        endSec = (int) Math.round(Math.min(endSec, duration));



        int sampleCount = (endSec - startSec) * sampleRate;

        byte[] audioData = waveFile.getData();

        List<Float> samples = new ArrayList<Float>(sampleCount + 1);


        byte[] tmp = new byte[INT_SIZE];

        for (int i = startSec * byteRate * channels * sampleRate; i < endSec * channels * byteRate * sampleRate; i += byteRate * channels){
            Arrays.fill(tmp, (byte) 0);
            for (int j = 0; j < byteRate; j++) {
                tmp[j] = audioData[i + j];
            }

            float f = formatBytesToAmplitude(tmp, byteRate);
            samples.add(f);
        }

        return new RawMonoSound(samples, sampleRate, byteRate);
    }

    private float formatBytesToAmplitude(byte[] bytes, int byteRate) {
        //time to bad code again
        float maxAbsSignedValue = (float) Math.pow(2, byteRate * 8);
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt() / maxAbsSignedValue;
    }

}
