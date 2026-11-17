package com.example.musicplayerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnPlayPause, btnStop;
    private TextView tvSongTitle;
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;
    private static final String AUDIO_URL = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        tvSongTitle = findViewById(R.id.tvSongTitle);

        initializeMediaPlayer();
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        btnPlayPause.setText("Play");
                    } else {
                        mediaPlayer.start();
                        btnPlayPause.setText("Pause");
                    }
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    btnPlayPause.setText("Play");
                    try {
                        mediaPlayer.prepare(); // Prepare it again so it can be replayed
                        mediaPlayer.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AUDIO_URL);
            mediaPlayer.prepareAsync(); // Use asynchronous preparation for network streams
            mediaPlayer.setOnPreparedListener(mp -> isPrepared = true);

            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlayPause.setText("Play");
                mediaPlayer.seekTo(0);
            });
        } catch (IOException e) {
            Toast.makeText(this, "Error initializing player: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Free up memory resources
            mediaPlayer = null;
        }
    }
}