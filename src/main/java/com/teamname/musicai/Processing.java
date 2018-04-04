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


public class Processing {

    public static final int FLOAT_SIZE = 4;

    public Float[]  getParameters(String path) throws IOException, UnsupportedAudioFileException {

        WaveFileConverter waveFileConverter = new WaveFileConverterImpl();

        WaveFile audio = new WaveFile(new File(path));

        RawMonoSound rawMonoSound = waveFileConverter.toRawMonoSound(audio, 0,25);

        FFTSoundAnalyzer fftSoundAnalyzer = new FFTSoundAnalyzerImpl();

        //50 it is fake, todo later
        FFTResult fftResult = fftSoundAnalyzer.analyze(rawMonoSound, 50);

        return fftResult.getAverageAmplitude();

    }
}
