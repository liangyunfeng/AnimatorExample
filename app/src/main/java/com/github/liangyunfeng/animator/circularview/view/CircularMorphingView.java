package com.github.liangyunfeng.animator.circularview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.liangyunfeng.animator.R;

/**
 * Created by yunfeng.l on 2017/03/31.
 */
public class CircularMorphingView extends View {

    private Context mContext;

    public float mCenterX;
    public float mCenterY;
    public float mRadius;
    public float mTouchArea;

    public CircularMorphingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CircularMorphingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CircularMorphingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public CircularMorphingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterX = getWidth() / 2.0f;
        mCenterY = getHeight() / 2.0f;
    }

    private void init () {
        mRadius = getResources().getDimension(R.dimen.circular_default_radius);
        mTouchArea = getResources().getDimension(R.dimen.circular_touch_area);
    }
}
