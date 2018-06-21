package com.github.liangyunfeng.animator.circularview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.liangyunfeng.animator.R;
import com.github.liangyunfeng.animator.circularview.ClockCircularMorphingViewCallback;

/**
 * Created by yunfeng.l on 2017/03/31.
 */
public class ClockCircularMorphingView extends RelativeLayout{

    private Context mContext;
    private ImageView mCircularViewBackground;
    private CircularMorphingView mCircularMorphingView;
    private LinearLayout mTimeTextLayout;

    private int mCircularViewTranslateYAnim;

    private boolean isActionDownActive = false;

    private ClockCircularMorphingViewCallback mClockCircularMorphingViewCallback;

    public ClockCircularMorphingView(Context context) {
        super(context);
        Log.v("lyf", "ClockCircularMorphingView - 1");
        mContext = context;
        init();
    }

    public ClockCircularMorphingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        Log.v("lyf", "ClockCircularMorphingView - 2");
    }

    public ClockCircularMorphingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        Log.v("lyf", "ClockCircularMorphingView - 3");
    }

    public ClockCircularMorphingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
        Log.v("lyf", "ClockCircularMorphingView - 4");
    }

    private void init() {
        Log.v("lyf", "init()");
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.circular_morphing_view_layout, this, true);

        mCircularViewBackground = (ImageView)findViewById(R.id.circular_morphing_bg);
        mCircularMorphingView = (CircularMorphingView)findViewById(R.id.circular_morphing_view);
        mTimeTextLayout = (LinearLayout)findViewById(R.id.time_text);

        mCircularMorphingView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("lyf","CircularMorphingView - onTouch - 1");
                boolean isMovingArea = true;
                double dx = Math.pow(Math.abs(mCircularMorphingView.mCenterX - event.getX()), 2);
                double dy = Math.pow(Math.abs(mCircularMorphingView.mCenterY - event.getY()), 2);
                double distance = Math.sqrt(dx + dy);

                if (Math.abs(distance - mCircularMorphingView.mRadius) > mCircularMorphingView.mTouchArea) {
                    isMovingArea = false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v("lyf","CircularMorphingView - onTouch - down : isMovingArea = " + isMovingArea);
                        if (isMovingArea) {
                            isActionDownActive = true;
                        }
                        if (!isActionDownActive && mClockCircularMorphingViewCallback != null) {
                            mClockCircularMorphingViewCallback.viAnimator(v,event);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v("lyf","CircularMorphingView - onTouch - move : isActionDownActive = " + isActionDownActive);
                        if (isActionDownActive) {

                        } else if (mClockCircularMorphingViewCallback != null) {
                            mClockCircularMorphingViewCallback.viAnimator(v,event);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        Log.v("lyf","CircularMorphingView - onTouch - up : ");
                        isActionDownActive = false;
                        if (mClockCircularMorphingViewCallback != null) {
                            mClockCircularMorphingViewCallback.viAnimator(v,event);
                        }
                        return false;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void viCircularMorphingViewAnimator(float value) {
        mCircularViewTranslateYAnim = (int) getResources().getDimension(R.dimen.circular_view_translateY_animator);

        mCircularViewBackground.setAlpha(1 - value);
        mCircularViewBackground.setScaleY(1 - value);
        mCircularViewBackground.setTranslationY(value * mCircularViewTranslateYAnim);

        mCircularMorphingView.setAlpha(1 - value);
        mCircularMorphingView.setScaleY(1 - value);
        mCircularMorphingView.setTranslationY(value * mCircularViewTranslateYAnim);

        mTimeTextLayout.setTranslationY(value * mCircularViewTranslateYAnim);
    }

    public void setCircularMorphingViewCallback(ClockCircularMorphingViewCallback callback) {
        mClockCircularMorphingViewCallback = callback;
    }
}
