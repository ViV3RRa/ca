// Copyright 2011 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package supercoolgroupname.smartsound;

import android.media.MediaRecorder;

public class SmartSoundMeasurement implements
        MicrophoneInputListener {

    MicrophoneInput micInput;  // The micInput object provides real time audio.

    // The Google ASR input requirements state that audio input sensitivity
    // should be set such that 90 dB SPL at 1000 Hz yields RMS of 2500 for
    // 16-bit samples, i.e. 20 * log_10(2500 / mGain) = 90.
    double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
    // For displaying error in calibration.
    double mRmsSmoothed;  // Temporally filtered version of RMS.
    double mAlpha = 0.9;  // Coefficient of IIR smoothing filter for RMS.
    private int mSampleRate = 8000; // The audio sampling rate to use.
    private int mAudioSource = MediaRecorder.AudioSource.VOICE_RECOGNITION;  // The audio source to use.

    public SmartSoundMeasurement()
    {
        // Here the micInput object is created for audio capture.
        // It is set up to call this object to handle real time audio frames of
        // PCM samples. The incoming frames will be handled by the
        // processAudioFrame method below.
        micInput = new MicrophoneInput(this);
        micInput.setSampleRate(mSampleRate);
        micInput.setAudioSource(mAudioSource);
    }

    public void start()
    {
        micInput.start();
    }

    public void stop()
    {
        micInput.stop();
    }

    /**
     * This method gets called by the micInput object owned by this activity.
     * It first computes the RMS value and then it sets up a bit of
     * code/closure that runs on the UI thread that does the actual drawing.
     */
    @Override
    public void processAudioFrame(short[] audioFrame) {
        // Compute the RMS value. (Note that this does not remove DC).
        double rms = 0;
        for (short anAudioFrame : audioFrame) {
            rms += anAudioFrame * anAudioFrame;
        }
        rms = Math.sqrt(rms / audioFrame.length);

        // Compute a smoothed version for less flickering of the display.
        mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
        final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);
        final double rmsdBActual = 20.0 * Math.log10(mGain*rms);
        Classifier.appendReading(20 + rmsdBActual);
    }
}
