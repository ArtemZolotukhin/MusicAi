package com.teamname.musicai.service;

import com.teamname.musicai.RawMonoSound;
import com.teamname.musicai.WaveFile;

public interface WaveFileConverter {

    RawMonoSound toRawMonoSound(WaveFile waveFile, int startSec, int endSec);

}
