package com.teamname.musicai;

import com.teamname.musicai.service.*;

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

        WaveFileConverter waveFileConverter = new WaveFileConverterImpl();

        System.out.println("Path to wav:");
        WaveFile audio = new WaveFile(new File(new Scanner(System.in).nextLine()));

        RawMonoSound rawMonoSound = waveFileConverter.toRawMonoSound(audio, 30, 60);

        FFTSoundAnalyzer fftSoundAnalyzer = new FFTSoundAnalyzerImpl();

        //50 it is fake, todo later
        FFTResult fftResult = fftSoundAnalyzer.analyze(rawMonoSound, 50);

        System.out.println(Arrays.toString(fftResult.getAverageAmplitude()));
        System.out.println(Arrays.toString(fftResult.getRanges()));

    }
}
