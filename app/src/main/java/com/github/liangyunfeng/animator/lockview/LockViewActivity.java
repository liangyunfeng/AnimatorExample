package com.github.liangyunfeng.animator.lockview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.liangyunfeng.animator.R;

/**
 * Created by yunfeng.l on 2018/6/1.
 */

public class LockViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_view);

        LockView lockView = (LockView) findViewById(R.id.lock_view);
        lockView.setCallBack(new LockView.CallBack() {
            @Override
            public void onLocked() {
                Toast.makeText(LockViewActivity.this, "onLocked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnLocked() {
                Toast.makeText(LockViewActivity.this, "onUnLocked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
