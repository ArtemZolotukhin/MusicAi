package com.teamname.musicai.service;

import com.teamname.musicai.RawMonoSound;

public interface FFTSoundAnalyzer {

    FFTResult analyze(RawMonoSound rawMonoSound, int blockTimeMillisec);

}
