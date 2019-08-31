package com.octoriz.cwcci;

import android.animation.Animator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_detail);

        ImageView imageView = findViewById(R.id.octoriz_logo);
//
//        YoYo.with(Techniques.Pulse)
//                .duration(1000)
//                .repeat(YoYo.INFINITE)
//                .playOn(findViewById(R.id.rl));

//        YoYo.with(Techniques.FadeIn).duration(2000)
//                .withListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//                        YoYo.with(Techniques.FadeOut)
//                                .duration(1000)
//                                .playOn(findViewById(R.id.octoriz_logo));
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {}
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//                        // This is being called when the animation starts over, I guess
//                        YoYo.with(Techniques.FadeOut)
//                                .duration(1000)
//                                .playOn(findViewById(R.id.octoriz_logo));
//                    }
//                })
//                .playOn(imageView);
    }
}
