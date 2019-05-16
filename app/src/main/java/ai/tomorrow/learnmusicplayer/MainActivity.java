package ai.tomorrow.learnmusicplayer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private boolean isRelease = true;
    private MediaPlayer mPlayer = null;

    // audio completion listener
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText(MainActivity.this, "I'm done!", Toast.LENGTH_SHORT).show();
            releaseMediaPlayer();
        }
    };

    // audio focus
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback because your Audio Focus was temporarily stolen, but will be back soon. i.e. for a phone call
                        mPlayer.pause();
                        // mPlayer.seekTo(0); // start from the begining
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Stop playback, because you lost the Audio Focus.
                        releaseMediaPlayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Lower the volume or pause, because something else is also playing audio over you.
                        mPlayer.pause();
                        // mPlayer.seekTo(0); // start from the begining
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback, because you hold the Audio Focus again!
                        mPlayer.start();
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      create audio manager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Button playButton = (Button) findViewById(R.id.play_button);
        Button pauseButton = (Button) findViewById(R.id.pause_button);
        Button stopButton = (Button) findViewById(R.id.stop_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // requesting audio focus, only before we start the music
                // Request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start playback
                    if (isRelease) {
                        mPlayer = MediaPlayer.create(MainActivity.this, R.raw.sample_music);
                        isRelease = false;
                    }
                    mPlayer.start();
                    // when the music is finished, show a toast.
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releaseMediaPlayer();
            }
        });
    }

//    @Override
//    protected void onStop() {
//        // when app is hide
//        super.onStop();
//        releaseMediaPlayer();
//    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            isRelease = true;
            mPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener); // abandon focus
        }
    }
}
