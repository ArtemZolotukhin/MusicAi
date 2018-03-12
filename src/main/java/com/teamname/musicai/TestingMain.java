package com.teamname.musicai;

import com.badlogic.audio.analysis.FFT;

import java.util.Arrays;

@Deprecated
public class TestingMain {

//    public static void main(String[] args) {
//
//        int samples = 128;
//
//        //int maxFreq = 10000;
//
//        FFT fft = new FFT(samples, 44100 );
//
//
//        float[] buffer = new float[samples];
//
//        for (int i = 0; i < samples; i++) {
//            buffer[i] = (float) (Math.random() * 2 - 1);
//        }
//
//        System.out.println("Before:\n" + Arrays.toString(buffer));
//
//        fft.forward(buffer);
//
//
//        float[] spect = fft.getSpectrum();
//
////        for (int i = 0; i < spect.length; i++) {
////            spect[i] /= (float) samples;
////        }
//
//        System.out.println("After:\n" + Arrays.toString(spect));
//
//
//
//        float[] bands = new float[fft.specSize()];
//
//        for (int i = 0; i < fft.specSize(); i++) {
//            bands[i] = fft.getBand(i);
//        }
//        System.out.println("Bands: " + Arrays.toString(bands));
//        System.out.println("BandWidth: " + fft.getBandWidth());
//
//
//        float[] freqs = new float[fft.specSize()];
//
//        for (float i = 0; i < 1000; i += 0.01) {
//            freqs[fft.freqToIndex(i)] = i;
//        }
//        System.out.println("Freqs: " + Arrays.toString(freqs));
//
//        for (float i = 125f; i < 126f; i += 0.01) {
//            System.out.println("FreqsGet " + i + ": " + fft.getFreq(i));
//
//            System.out.println("FreqsGet " + (i + 200) + ": " + fft.getFreq(i + 200));
//        }
//
//    }

}
