package ai.tomorrow.learnmusicplayer;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private boolean isRelease = true;
    private MediaPlayer mPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer = MediaPlayer.create(this, R.raw.sample_music);
        Button playButton = (Button) findViewById(R.id.play_button);
        Button pauseButton = (Button) findViewById(R.id.pause_button);
        Button stopButton = (Button) findViewById(R.id.stop_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRelease){
                    mPlayer = MediaPlayer.create(MainActivity.this, R.raw.sample_music);
                    isRelease = false;
                }
                mPlayer.start();

                Button playButton = (Button) findViewById(R.id.play_button);
                Button pauseButton = (Button) findViewById(R.id.pause_button);
                Button stopButton = (Button) findViewById(R.id.stop_button);
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(true);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();

                Button playButton = (Button) findViewById(R.id.play_button);
                Button pauseButton = (Button) findViewById(R.id.pause_button);
                Button stopButton = (Button) findViewById(R.id.stop_button);
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.reset();
                mPlayer.release();
                isRelease = true;

                Button playButton = (Button) findViewById(R.id.play_button);
                Button pauseButton = (Button) findViewById(R.id.pause_button);
                Button stopButton = (Button) findViewById(R.id.stop_button);
                playButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(false);
            }
        });

    }
}
