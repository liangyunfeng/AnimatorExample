package com.github.liangyunfeng.animator.circularview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.github.liangyunfeng.animator.R;
import com.github.liangyunfeng.animator.circularview.view.ClockCircularMorphingView;

/**
 * Created by yunfeng.l on 2017/03/31.
 */
public class CircularActivity extends Activity{

    private RelativeLayout mGridLayout;
    private GridView mGridView;
    private View mArrowUpView;
    private ClockCircularMorphingView mClockCircularMorphingView;

    private float firstPointY = 0;
    private float edVal = 0;
    private boolean mAnimForward = false;
    private boolean mAnimPosition = false;
    private boolean mSlideAnimFinish = true;
    private boolean isMoving = false;

    private final float SENSITIVITY_DIRECTION_CHANGE = 12f;
    private final float ZERO_MOVEMENT = 0.0f;
    private final boolean VI_DIR_UP = true;
    private final boolean VI_DIR_DOWN = false;
    private final boolean VI_POS_TOP = true;
    private final boolean VI_POS_BOTTOM = false;
    private final int SLIDEY = 678;

    private int mGridLayoutMarginTopAnim;
    private int mGridLayoutFromHeightAnim;
    private int mGridLayoutToHeightAnim;

    private ValueAnimator mViCircularLayoutSlideAnimator;
    private int mDuration = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_circular);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGridLayout = (RelativeLayout)findViewById(R.id.grid_layout);
        mGridView = (GridView)findViewById(R.id.grid_view);
        mArrowUpView = findViewById(R.id.arrow_up);
        mClockCircularMorphingView = (ClockCircularMorphingView)findViewById(R.id.clock_circular_morphing_view);
        mClockCircularMorphingView.setCircularMorphingViewCallback(mClockCircularMorphingViewCallback);

        mGridLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("lyf","CircularActivity - onTouch - 1");
                return handleTouchEvent(v, event);
            }
        });
        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("lyf","CircularActivity - onTouch - 1");
                return handleTouchEvent(v, event);
            }
        });

        mGridLayoutMarginTopAnim = (int)getResources().getDimension(R.dimen.grid_layout_margin_top_animator);
        mGridLayoutFromHeightAnim = (int)getResources().getDimension(R.dimen.grid_layout_from_height_animator);
        mGridLayoutToHeightAnim = (int)getResources().getDimension(R.dimen.grid_layout_to_height_animator);
    }


    private boolean handleTouchEvent(View v, MotionEvent event) {
        float curY = event.getY();
        float delta = firstPointY - curY;

        if (event.getAction() == MotionEvent.ACTION_MOVE && Math.abs(delta) >= SENSITIVITY_DIRECTION_CHANGE
                && edVal == 0) {
            if (delta > ZERO_MOVEMENT) {
                mAnimForward = VI_DIR_UP;
            } else if (delta < ZERO_MOVEMENT) {
                mAnimForward = VI_DIR_DOWN;
            }
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("lyf","CircularActivity - onTouch - down");
                firstPointY = curY;
                edVal = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.v("lyf","CircularActivity - onTouch - move");
                Log.v("lyf","CircularActivity - onTouch - move - 10 : delta = " + delta + ", edVal = " + edVal + ", curY = " + curY + ", firstPointY = " + firstPointY + ", mAnimPosition = " + mAnimPosition + ", mAnimForward = " + mAnimForward + ", isMoving = " + isMoving);
                if (Math.abs(delta) >= SENSITIVITY_DIRECTION_CHANGE && firstPointY != 0) {
                    edVal = Math.abs(curY - firstPointY) / SLIDEY;
                }

                if(edVal <= 1 && firstPointY != 0) {
                    if ((mAnimPosition == VI_POS_BOTTOM && mAnimForward == VI_DIR_UP) || (mAnimPosition == VI_POS_TOP && mAnimForward == VI_DIR_DOWN)) {
                        isMoving = true;
                        if (mAnimPosition == VI_POS_BOTTOM && mAnimForward == VI_DIR_UP) {
                            viGridLayoutMarginTopAnimator(edVal);		//GridLayout离Top的距离变化动画
                            viGridLayoutHeightAnimator(edVal);			//GridLayout高度变化动画，因为离Top距离变化后，需要更新GridLayout高度，占满屏幕
                            viArrowUpAnimator(edVal);					//Arrow图标动画过程中改变透明度
                            mClockCircularMorphingView.viCircularMorphingViewAnimator(edVal);	//时钟View的椭圆变化动画
                        } else if (mAnimPosition == VI_POS_TOP && mAnimForward == VI_DIR_DOWN) {
                            viGridLayoutMarginTopAnimator(1 - edVal);
                            viGridLayoutHeightAnimator(1 - edVal);
                            viArrowUpAnimator(1 - edVal);
                            mClockCircularMorphingView.viCircularMorphingViewAnimator(1 - edVal);
                        }
                    }
                } else if (edVal > 1) {
                    edVal = 1;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.v("lyf","CircularActivity - onTouch - up");
                isMoving = false;
                if ((mAnimPosition == VI_POS_BOTTOM && mAnimForward == VI_DIR_UP) || (mAnimPosition == VI_POS_TOP && mAnimForward == VI_DIR_DOWN)) {
                    upAndDownSlideAnimator(edVal);						//滑动过程中松开手后，自动完成剩下的动画
                }
                firstPointY = 0;
                break;
        }

        return false;
    }

    private void viGridLayoutMarginTopAnimator(float value) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)mGridLayout.getLayoutParams();
        params.setMargins(0, (int) (value * mGridLayoutMarginTopAnim), 0, 0);
        mGridLayout.requestLayout();
    }

    private void viGridLayoutHeightAnimator(float value) {
        ViewGroup.LayoutParams params = mGridLayout.getLayoutParams();
        params.height = (int) (mGridLayoutFromHeightAnim + value * mGridLayoutToHeightAnim);
        mGridLayout.setLayoutParams(params);
    }

    private void viArrowUpAnimator(float value) {
        mArrowUpView.setAlpha(0.7f * (1 - value));
        mArrowUpView.setTranslationY(mGridLayoutMarginTopAnim * value);
    }

    private void upAndDownSlideAnimator(float edVal) {
        mViCircularLayoutSlideAnimator = ValueAnimator.ofFloat(edVal, 1f);
        mViCircularLayoutSlideAnimator.setDuration((int)(mDuration * (1 - edVal)));
        //mViCircularLayoutSlideAnimator.setInterpolator();
        mViCircularLayoutSlideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                if (mAnimForward == VI_DIR_UP && mClockCircularMorphingView != null) {
                    viGridLayoutMarginTopAnimator(value);
                    viGridLayoutHeightAnimator(value);
                    viArrowUpAnimator(value);
                    mClockCircularMorphingView.viCircularMorphingViewAnimator(value);
                } else if (mAnimForward == VI_DIR_DOWN && mClockCircularMorphingView != null) {
                    viGridLayoutMarginTopAnimator(1 - value);
                    viGridLayoutHeightAnimator(1 - value);
                    viArrowUpAnimator(1 - value);
                    mClockCircularMorphingView.viCircularMorphingViewAnimator(1 - value);
                }
            }
        });

        mViCircularLayoutSlideAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSlideAnimFinish = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSlideAnimFinish = true;
                if (mAnimForward == VI_DIR_UP) {
                    mAnimPosition = VI_POS_TOP;
                } else if (mAnimForward == VI_DIR_DOWN) {
                    mAnimPosition = VI_POS_BOTTOM;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mViCircularLayoutSlideAnimator.start();
    }

    ClockCircularMorphingViewCallback mClockCircularMorphingViewCallback = new ClockCircularMorphingViewCallback() {
        @Override
        public void viAnimator(View v, MotionEvent event) {
            handleTouchEvent(v, event);
        }
    };
}
