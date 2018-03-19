package com.teamname.musicai.service;

import com.badlogic.audio.analysis.FFT;
import com.teamname.musicai.ArrayUtils;
import com.teamname.musicai.RawMonoSound;

import java.util.Arrays;

public class FFTSoundAnalyzerImpl implements FFTSoundAnalyzer {


    public static final int RANGES = 8;

    private Range[] ranges;

    /**
     *
     * @param rawMonoSound
     * WARNING! it is just test version, so it is can work only with sound, wich has 44100 sampleRate
     * @param blockTimeSec - it is fake //todo
     * @return
     */
    @Override
    public FFTResult analyze(RawMonoSound rawMonoSound, int blockTimeSec) {
        //TODO remove this and make normal round to power of 2
        int blockSize = 2048; //closest ceil power of 2 to 2205 (50milisec for sampleRate = 44100)
        int sampleRate = rawMonoSound.getSampleRate();


        Float[] raw = new Float[rawMonoSound.getRaw().size()];
        raw = rawMonoSound.getRaw().toArray(raw);

        Float[] averageAmplitude = new Float[RANGES];
        long[] averageAmplitudeIterations= new long[RANGES];
        Arrays.fill(averageAmplitude, 0f);
        Arrays.fill(averageAmplitudeIterations, 0l);

        for (int i = 0; i < raw.length; i += blockSize) {

            if (raw.length - i < blockSize) {
                break;
            }

            float[] rawBlock = ArrayUtils.toPrimitiveFloat(
                    Arrays.copyOfRange(raw, i, i + blockSize));


            FFT fft = new FFT(blockSize, sampleRate);
            fft.forward(rawBlock);

            float[] spectrum = fft.getSpectrum();


            //Dirt, shit and blasphemy
            float freqStep = 1f;
            Range range;
            for (int j = 0; j < RANGES; j++) {
                range = getRanges()[j];
                for (float k = range.getStart(); k < range.getEnd(); k += freqStep) {
                    averageAmplitude[j] +=  fft.getFreq(k);
                    averageAmplitudeIterations[j] += 1;
                }
            }

        }


        //Calculate average freqs
        for (int i = 0; i < averageAmplitude.length; i++) {
            //violence torture for eyes
            averageAmplitude[i] /= blockSize *
                    (averageAmplitudeIterations[i] == 0l ? 1 : averageAmplitudeIterations[i]);
        }


        FFTResult fftResult = new FFTResult();
        fftResult.setAverageAmplitude(averageAmplitude);
        fftResult.setRanges(getRanges());

        return fftResult;
    }

    private Range[] getRanges() {

        if (ranges == null) {
            //Yes, it is stupid code, but i have not time
            ranges = new Range[RANGES];
            ranges[0] = new Range(0, 200);
            ranges[1] = new Range(200, 400);
            ranges[2] = new Range(400, 600);
            ranges[3] = new Range(600, 800);
            ranges[4] = new Range(800, 1200);
            ranges[5] = new Range(1200, 1600);
            ranges[6] = new Range(1600, 3200);
            ranges[7] = new Range(3200, 22050);

        }

        return ranges;
    }


}
