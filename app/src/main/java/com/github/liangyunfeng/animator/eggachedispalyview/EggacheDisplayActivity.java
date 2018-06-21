package com.github.liangyunfeng.animator.eggachedispalyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.liangyunfeng.animator.R;

/**
 * Refer to: https://github.com/DthFish/EggacheDisplayView
 */
public class EggacheDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggache_display_view);
        final EggacheDisplayView edv = (EggacheDisplayView)findViewById(R.id.edv);
        edv.postDelayed(new Runnable() {
            @Override
            public void run() {
                edv.collapse();
            }
        }, 2000);
    }
}
