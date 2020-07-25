package photran.me.parallaxviewpage.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import photran.me.parallaxviewpage.R;


public class PTPageView extends View {
    private int mState = 0;
    private int mSize = 1;
    private Paint mPaintNormalState;
    private Paint mPaintState;
    private RectF mRectF = new RectF();
    public static float HEIGHT_SIZE;
    public static float WIDTH_STATE_SIZE;
    public static float SPACE;
    public static float HEIGHT_STATE_SIZE;

    public static final int RECT = 1;
    public static final int CIRCLE = 0;
    private int mType = CIRCLE;

    private static float OVAL;
    private int mColorState = Color.parseColor("#e0e1e2");
    private int mColorNormalState = Color.parseColor("#e14b40");
    private float mWidthItem;
    private float mHeightItem;

    public PTPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        readFromAttribute(context, attrs, defStyle);
    }

    public PTPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PTPageView(Context context) {
        this(context, null, 0);
    }

    public void setSize(final int size) {
        mSize = size;
        if (mSize <= 0) {
            mSize = 1;
        }
    }

    public void setState(int k) {
        mState = k;
        if (k < 0 && k >= mSize) {
            mState = 0;
        }
        invalidate();
    }

    private void readFromAttribute(Context context, AttributeSet attrs,
                                   int defStyle) {
        if (attrs == null) return;
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.PTPageView, defStyle, 0);
        mColorNormalState = a.getColor(
                R.styleable.PTPageView_pageIndicatorTintColor,
                mColorNormalState);
        mColorState = a.getColor(
                R.styleable.PTPageView_currentPageIndicatorTintColor,
                mColorState);
        mPaintNormalState.setColor(mColorNormalState);
        mPaintState.setColor(mColorState);

        mSize = a.getInt(R.styleable.PTPageView_numberOfPages, 1);
        mWidthItem = a.getFloat(R.styleable.PTPageView_widthItem,
                mWidthItem);
        mHeightItem = a.getFloat(R.styleable.PTPageView_heightItem,
                mWidthItem);
        if (mWidthItem > 0 && mHeightItem > 0) {
            if (mWidthItem == mHeightItem) {
                this.mWidthItem = Math.min(mWidthItem, WIDTH_STATE_SIZE);
                mType = PTPageView.CIRCLE;
            } else {
                this.mWidthItem = Math.min(mWidthItem, WIDTH_STATE_SIZE);
                this.mHeightItem = Math.min(mHeightItem, HEIGHT_STATE_SIZE);
                mType = PTPageView.RECT;
            }
        } else {
            mWidthItem = WIDTH_STATE_SIZE;
            mHeightItem = HEIGHT_STATE_SIZE;
        }
        a.recycle();
    }

    private void init() {
        final float one_dp = Resources.getSystem().getDisplayMetrics().densityDpi / 160.0f;
        HEIGHT_SIZE = 16 * one_dp;
        WIDTH_STATE_SIZE = 10 * one_dp;
        SPACE = 10 * one_dp;
        HEIGHT_STATE_SIZE = 4 * one_dp;
        OVAL = one_dp;
        mPaintNormalState = new Paint();
        mPaintNormalState.setAntiAlias(true);
        mPaintState = new Paint();
        mPaintState.setAntiAlias(true);
        mWidthItem = WIDTH_STATE_SIZE;
        mHeightItem = HEIGHT_STATE_SIZE;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, (int) (HEIGHT_SIZE * Resources
                .getSystem().getDisplayMetrics().density));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float left = getWidth() - (getPaddingLeft() + getPaddingRight());
        float top = getHeight() - (getPaddingTop() + getPaddingBottom());
        left = (left - (mSize * mWidthItem + (mSize - 1) * SPACE)) / 2;
        top = (top - mHeightItem) / 2;

        if (mType == RECT) {
            drawRectSate(canvas, top, left);
        } else {
            drawRoundSate(canvas, top, left);
        }
    }

    private void drawRoundSate(Canvas canvas, float top, float left) {
        final float r = mWidthItem / 2;
        final float cy = top + r;
        float cx = left + r;

        for (int i = 0; i < mSize; i++) {
            if (i == mState) {
                canvas.drawCircle(cx, cy, r, mPaintState);
            } else {
                canvas.drawCircle(cx, cy, r, mPaintNormalState);
            }
            cx += mWidthItem + SPACE;
        }
    }

    private void drawRectSate(Canvas canvas, float top, float left) {
        mRectF.left = left;
        mRectF.top = top;
        mRectF.right = left + mWidthItem;
        mRectF.bottom = top + mHeightItem;
        for (int i = 0; i < mSize; i++) {
            if (i == mState) {
                canvas.drawRoundRect(mRectF, OVAL, OVAL, mPaintState);
            } else {
                canvas.drawRoundRect(mRectF, OVAL, OVAL, mPaintNormalState);
            }
            left += mWidthItem + SPACE;
            mRectF.left = left;
            mRectF.right = left + mWidthItem;
        }
    }
}