package com.github.reline.unacceptable;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private ImageView unacceptableButton;
    private MediaPlayer mediaPlayer;
    private Drawable lemon;
    private Drawable lemongrab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set application to control media volume instead of ring volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        lemon = ContextCompat.getDrawable(this, R.drawable.lemon);
        lemongrab = ContextCompat.getDrawable(this, R.drawable.lemongrab);
        final Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        final Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(this, R.raw.lemon_grab_unacceptable);
        final int duration = mediaPlayer.getDuration();

        // setup button
        unacceptableButton = (ImageView) findViewById(R.id.lemon_button);
        unacceptableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to lemongrab image
                unacceptableButton.setImageDrawable(lemongrab);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // change image back to normal
                        unacceptableButton.setImageDrawable(lemon);
                    }
                }, duration);

                // play media player
                mediaPlayer.start();

                // shake lemon
                unacceptableButton.startAnimation(shake);

                // vibrate phone
                vibrator.vibrate(duration);
            }
        });
    }
}
