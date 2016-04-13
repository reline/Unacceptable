package com.github.reline.unacceptable;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
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

        lemon = getDrawable(R.drawable.lemon);
        lemongrab = getDrawable(R.drawable.lemongrab);

        // setup media player
        mediaPlayer = MediaPlayer.create(this, R.raw.lemon_grab_unacceptable);

        // setup button
        unacceptableButton = (ImageView) findViewById(R.id.lemon_button);
        assert unacceptableButton != null;
        unacceptableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to lemongrab image
                unacceptableButton.setImageDrawable(lemongrab);

                // play media player
                mediaPlayer.start();

                // shake lemon
                Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                unacceptableButton.startAnimation(shake);

                // vibrate phone
                Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 2 seconds
                vibrator.vibrate(2000);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // change image back to normal
                        unacceptableButton.setImageDrawable(lemon);
                    }
                }, 2000);
            }
        });
    }
}
