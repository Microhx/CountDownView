package com.xing.lib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * @version : 1.0
 * <p>
 * @desc : 自定义倒计时控件
 * <p>
 * @author: javainstalling@163.com
 * <p>
 * 纵然万劫不复，纵然相思入骨，我依然待你眉眼如初，岁月如故。
 * <p>
 * <p>
 * @date : 2019/4/17
 * <p>
 */
public class CountDownView extends View {

    private static final int TOTAL_ANGLE = 360;

    /**
     * 背景Paint
     */
    private Paint mbgPaint;
    /**
     * 圆弧Paint
     */
    private Paint mArcPaint;
    /**
     * 文字paint
     */
    private Paint mTextPaint;
    /**
     * 背景半径
     */
    private float mRadius;
    /**
     * 圆弧宽度
     */
    private float mArcWidth;
    /**
     * 文字FontMetrics
     */
    private float mTextHeight;

    /**
     * 背景默认是白色
     */
    private int mBgColor;
    /**
     * 圆弧颜色为红色
     */
    private int mArcColor;


    /**
     * 圆弧开始的角度
     */
    private int mStartAngle ;

    /**
     * 圆弧结束的角度
     */
    private int mEndAngle ;

    /**
     * 转过的角度
     */
    private int mSweepAngle;

    /**
     * 开始画的角度
     */
    private int mStartDrawAngle;

    /**
     * 圆弧对应的矩形
     */
    private RectF mArcRectF;

    /**
     * 文字的大小
     */
    private float mTextSize;

    /**
     * 文字颜色
     */
    private int mTextColor;

    /**
     * 显示的数字
     */
    private String mText;


    /**
     * 显示的数字为增加/减少
     * 1s ---  5s
     * 5s --- 1s
     */
    private boolean textIncrease;

    /**
     * 显示的最大时间
     */
    private int mShowMaxTime;

    /**
     * 时间单位
     */
    private String mTimeUnit;

    private ValueAnimator mValueAnimator;

    private OnCountDownViewListener mCountDownListener;


    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initParams();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CountDownView);

        mBgColor = array.getColor(R.styleable.CountDownView_hx_bg_color, Color.WHITE);
        mRadius = array.getDimension(R.styleable.CountDownView_hx_bg_radius, dp2px(30));

        mArcColor = array.getColor(R.styleable.CountDownView_hx_arc_color, Color.RED);
        mArcWidth = array.getDimension(R.styleable.CountDownView_hx_arc_width, dp2px(2));
        textIncrease = array.getInt(R.styleable.CountDownView_hx_text_change_style, 0) == 0;
        mTextSize = array.getDimension(R.styleable.CountDownView_hx_text_size, dp2px(14));
        mTextColor = array.getColor(R.styleable.CountDownView_hx_text_color, Color.RED);
        mShowMaxTime = array.getInt(R.styleable.CountDownView_hx_max_time, 5);
        mTimeUnit = array.getString(R.styleable.CountDownView_hx_time_unit);
        mStartDrawAngle = array.getInt(R.styleable.CountDownView_hx_start_angle, -90);

        if (TextUtils.isEmpty(mTimeUnit)) {
            mTimeUnit = "s";
        }
        array.recycle();
    }

    private void initParams() {
        mbgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbgPaint.setStyle(Paint.Style.FILL);
        mbgPaint.setColor(mBgColor);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setColor(mArcColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeWidth(mArcWidth);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = Math.abs((fontMetrics.descent + fontMetrics.ascent) / 2);
        mText = String.valueOf(mShowMaxTime);

        //增大的方向
        if (textIncrease) {
            mStartAngle = 0;
            mEndAngle = TOTAL_ANGLE;
            mSweepAngle = 0;

        } else {
            mStartAngle = TOTAL_ANGLE;
            mSweepAngle = TOTAL_ANGLE;
            mEndAngle = 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw && h != oldh) {
            mArcRectF = new RectF(mArcWidth / 2, mArcWidth / 2, w - mArcWidth / 2, h - mArcWidth / 2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int targetWidth = (int) (2 * mRadius);
        setMeasuredDimension(targetWidth, targetWidth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //1.画背景
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mRadius, mbgPaint);

        //2.画圆弧
        canvas.drawArc(mArcRectF, mStartDrawAngle, mSweepAngle, false, mArcPaint);

        //3.画文字
        canvas.drawText(mText + mTimeUnit, getWidth() / 2, getHeight() / 2 + mTextHeight, mTextPaint);
    }

    public void start() {
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
            mValueAnimator = null ;
        }

        if (mShowMaxTime <= 0) {
            Log.e("TAG", "max time must be set");
            return;
        }

        mValueAnimator = ValueAnimator.ofInt(mStartAngle, mEndAngle);
        mValueAnimator.setDuration(mShowMaxTime * 1000);
        mValueAnimator.setInterpolator(new LinearInterpolator());

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSweepAngle = (int) animation.getAnimatedValue();
                mText = calculateText() + "";

                invalidate();
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
               if(null != mCountDownListener) {
                   mCountDownListener.onFinished();
               }
            }
        });

        mValueAnimator.start();
    }

    private int calculateText() {
        if (mSweepAngle <= 0) {
            return 0;
        }

        if (mSweepAngle >= TOTAL_ANGLE) {
            return mShowMaxTime;
        }

        if (textIncrease) {
            return (int) (mSweepAngle * 1.0f / 360 * mShowMaxTime);
        } else {
            return (int) (mSweepAngle * 1.0f / 360 * mShowMaxTime) + 1;
        }
    }

    public void setShowMaxTime(int time) {
        if (time <= 0) {
            return;
        }

        mShowMaxTime = time;
    }

    public void setCountDownListener(OnCountDownViewListener mCountDownListener) {
        this.mCountDownListener = mCountDownListener;
    }


    public interface OnCountDownViewListener {
        /**
         * 数据动画已经完成
         */
        void onFinished();
    }

    private static float dp2px(float dp) {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }
}
