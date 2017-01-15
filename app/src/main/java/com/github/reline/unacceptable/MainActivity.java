package com.github.reline.unacceptable;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView unacceptableButton;
    private MediaPlayer mediaPlayer;
    private Drawable lemon;
    private Drawable lemongrab;
    private Animation shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set application to control media volume instead of ring volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.lemon_grab_unacceptable);
        lemon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.lemon);
        lemongrab = ContextCompat.getDrawable(getApplicationContext(), R.drawable.lemongrab);
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final int duration = mediaPlayer.getDuration();
        unacceptableButton = (ImageView) findViewById(R.id.lemon_button);

        shake.restrictDuration(duration);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                unacceptableButton.setOnClickListener(null);
                unacceptableButton.setImageDrawable(lemongrab);
                mediaPlayer.start();
                vibrator.vibrate(duration);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                unacceptableButton.setImageDrawable(lemon);
                vibrator.cancel();
                unacceptableButton.setOnClickListener(MainActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        unacceptableButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        unacceptableButton.startAnimation(shake);
    }
}
