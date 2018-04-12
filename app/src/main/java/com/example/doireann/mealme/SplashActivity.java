package com.example.doireann.mealme;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "mealme";
    private final String VERSION = BuildConfig.VERSION_NAME;
    ImageView image;
    TextView splashText;
    TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //text animation
        splashText = findViewById(R.id.id_splash_txt);
        Animation textAnim;
        Animation imageAnim;

        // image animation
        image = findViewById(R.id.id_splash_img);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            textAnim = AnimationUtils.loadAnimation(this, R.anim.splash_txt_animp);
            imageAnim = AnimationUtils.loadAnimation(this, R.anim.splash_img_animp);
        } else {
            textAnim = AnimationUtils.loadAnimation(this, R.anim.splash_txt_animl);
            imageAnim = AnimationUtils.loadAnimation(this, R.anim.splash_img_animl);
        }
        splashText.startAnimation(textAnim);
        image.startAnimation(imageAnim);
        imageAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                image.setVisibility(View.GONE);
                splashText.setVisibility(View.GONE);
                startActivity(new Intent(SplashActivity.this, MenuActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // version text
        versionText = findViewById(R.id.id_splash_version_txt);
        versionText.setText(getResources().getString(R.string.version) + " " + VERSION);
    }
}
