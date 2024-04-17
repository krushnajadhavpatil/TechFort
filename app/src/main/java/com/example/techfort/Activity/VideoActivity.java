package com.example.techfort.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.techfort.R;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        bar = findViewById(R.id.progbar);
        videoView = findViewById(R.id.videoView);
        String uri = getIntent().getStringExtra("uri");

        playVideoFullscreen(uri);
    }

    private void playVideoFullscreen(String videoUrl) {
        videoView.setVideoURI(Uri.parse(videoUrl));
        videoView.setOnPreparedListener(mp -> {
            bar.setVisibility(View.GONE);
            // Create a MediaController
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);

            // Set the MediaController to the VideoView
            videoView.setMediaController(mediaController);

            // Start playing the video
            videoView.start();
        });

        // Hide the system UI for fullscreen playback
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
    }

    // Hide the system UI (status bar, navigation bar) for fullscreen playback
    private void hideSystemUI() {
        videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
