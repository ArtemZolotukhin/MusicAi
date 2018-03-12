package com.teamname.musicai;

import com.teamname.musicai.service.FFTResult;
import com.teamname.musicai.service.FFTSoundAnalyzer;
import com.teamname.musicai.service.FFTSoundAnalyzerImpl;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class Main {

    public static final int FLOAT_SIZE = 4;

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        System.out.println("Path to wav:");
        WaveFile audio = new WaveFile(new File(new Scanner(System.in).nextLine()));

        AudioFormat audioFormat = audio.getAudioFormat();

        System.out.println(audioFormat);
        System.out.println(audio.getFramesCount());

        int byteRate = audioFormat.getFrameSize() / audioFormat.getChannels(); // кол-во байт на отсчет ( разрядность / 8 )
        System.out.println("ByteRate:" + byteRate);
        
        int sampleRate = Math.round(audio.getAudioFormat().getSampleRate());


        if (byteRate > FLOAT_SIZE) {
            throw new IllegalArgumentException("framSize too big");
        }

        int channels = audioFormat.getChannels(); // кол-во каналов стерео | моно
        System.out.println("Channels:" + channels);

        byte[] audioData = audio.getData();
        ArrayList<Float> samples = new ArrayList<Float>();

        byte[] tmp = new byte[FLOAT_SIZE];

        float koff = (float) Math.pow(2, byteRate * 8);

        for (int i = 30 * byteRate * channels * sampleRate; i < 60 * channels * byteRate * sampleRate; i += byteRate * channels){
            Arrays.fill(tmp, (byte) 0);
            for (int j = 0; j < byteRate; j++) {
                tmp[j] = audioData[i + j];
            }
            float f = ByteBuffer.wrap(tmp).order(ByteOrder.LITTLE_ENDIAN).getInt() / koff;
            //System.out.println(f);
            samples.add(f);
        }

        System.out.println(samples.size()); // 15 секунд получается, вместо 30.



        RawMonoSound rawMonoSound = new RawMonoSound(samples, sampleRate, byteRate);

        FFTSoundAnalyzer fftSoundAnalyzer = new FFTSoundAnalyzerImpl();

        //50 it is fake, todo later
        FFTResult fftResult = fftSoundAnalyzer.analyze(rawMonoSound, 50);

        System.out.println(Arrays.toString(fftResult.getAverageAmplitude()));
        System.out.println(Arrays.toString(fftResult.getRanges()));


    }
}
