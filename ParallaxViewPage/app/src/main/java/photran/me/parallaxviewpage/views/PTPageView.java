package photran.me.parallaxviewpage.views;

/**
 * Created by photran on 4/22/16.
 */

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

    /**
     * Các thuộc tính dùng attribute -Số lượng item -màu nomal -màu state -size
     * item width -size item height
     **/

    private int mState = 0;
    private int mSize = 1;
    private Paint mPaintNormalState;
    private Paint mPaintState;
    private RectF mRectF = new RectF();
    /* Kích thước chiều cao tối thiểu của ViewPagerState (mặc định 16dp) */
    public static float HEIGHT_SIZE;
    /*
     * Kích thước chiều rộng(RECT) hay đường kính (CIRCLE) các item state của
     * ViewPagerState (mặc định 10dp)
     */
    public static float WIDTH_STATE_SIZE;
    /* Khoảng cách của các item state ViewPagerState (mặc định 5dp) */
    public static float SPACE;
    /*
     * Kích thước chiều cao (RECT) các item state của ViewPagerState (mặc định
     * 4dp)
     */
    public static float HEIGHT_STATE_SIZE;

    /* ViewPagerState dạng hình chữ nhật */
    public static final int RECT = 1;
    /* ViewPagerState dạng hình tròn (mặc định) */
    public static final int CIRCLE = 0;
    private int mStype = CIRCLE;

    public int getStype() {
        return mStype;
    }

    public void setStype(int stype) {
        if (mStype != stype)
            this.mStype = stype;
        invalidate();
    }

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

    // đọc từ attribute
    private void readFromAttribute(Context context, AttributeSet attrs,
                                   int defStyle) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
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
                    this.mHeightItem = this.mWidthItem;
                    mStype = PTPageView.CIRCLE;

                } else {
                    this.mWidthItem = Math.min(mWidthItem, WIDTH_STATE_SIZE);
                    this.mHeightItem = Math.min(mHeightItem, HEIGHT_STATE_SIZE);
                    mStype = PTPageView.RECT;
                }
            } else {
                mWidthItem = WIDTH_STATE_SIZE;
                mHeightItem = HEIGHT_STATE_SIZE;
            }
            a.recycle();
        }
    }

    private void init() {
        float one_dp = Resources.getSystem().getDisplayMetrics().densityDpi / 160.0f;
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

    /**
     * Trả về chiều rộng của các item Trong trường họp là item hình tròn đây là
     * đường kính của Item
     *
     * @return
     */
    public float getWidthItem() {
        return mWidthItem;
    }

    /**
     * Khởi gán kích thước của các Item. <b>Trong tường hợp mWidthItem ==
     * mHeightItem thì item này sẽ là hình tròn, đồng thời đây cũng là chiều dài
     * đường kính</b>
     *
     * @param mWidthItem  : chiều rộng
     * @param mHeightItem :chiều cao
     */
    public void setSizeItem(float mWidthItem, float mHeightItem) {
        if (mWidthItem > 0 && mHeightItem > 0) {

            if (mWidthItem == mHeightItem) {
                this.mWidthItem = Math.min(mWidthItem, WIDTH_STATE_SIZE);
                this.mHeightItem = this.mWidthItem;
                mStype = PTPageView.CIRCLE;

            } else {
                this.mWidthItem = Math.min(mWidthItem, WIDTH_STATE_SIZE);
                this.mHeightItem = Math.min(mHeightItem, HEIGHT_STATE_SIZE);
                mStype = PTPageView.RECT;
            }
            invalidate();
        }
    }

    /**
     * Trả về chiều cao của các item Trong trường họp là item hình tròn đây là
     * đường kính của Item
     *
     * @return
     */
    public float getHeightItem() {
        return mHeightItem;
    }

    /**
     * gán màu thể hiện trạng/ vị trí Pager, mặc định
     * Color.parseColor("#e0e1e2")
     *
     * @param colorState là mã màu theo integer
     */
    public void setColorState(int colorState) {
        if (mColorState != colorState) {
            mColorState = colorState;
            mPaintState.setColor(mColorState);
            invalidate();
        }
    }

    /**
     * gán màu nền của các trạng thái không thể hiện vị trí Pager, mặc định
     * Color.parseColor("#e14b40")
     *
     * @param colorNormalState là mã màu theo integer
     */
    public void setColorNormalState(int colorNormalState) {
        if (mColorNormalState != colorNormalState) {
            mColorNormalState = colorNormalState;
            mPaintNormalState.setColor(mColorNormalState);
            invalidate();
        }
    }

    /**
     * @return trả về màu thể hiện trạng/ vị trí Pager, mặc định
     * Color.parseColor("#e0e1e2")
     */
    public int getColorState() {
        return mColorState;
    }

    /**
     * @return trả về màu nền của các trạng thái không thể hiện vị trí Pager,
     * mặc định Color.parseColor("#e14b40")
     */
    public int getColorNormalState() {
        return mColorNormalState;
    }

    /**
     * @return trạng thái/vị trí thể hiện của Pager
     */
    public int getState() {
        return mState;
    }

    /**
     * Cài đặt vị trị thể hiện trạng thái
     *
     * @param k (0<=k <= getSize()-1
     */
    public void setState(int k) {
        mState = k;
        if (k < 0 && k >= mSize) {
            mState = 0;
        }

        invalidate();
    }

    /**
     * @return số lượng trạng thái / hay số lượng pages
     */
    public int getSize() {
        return mSize;
    }

    /**
     * Gán số trạng thái khi khởi tạo
     *
     * @param k
     */
    public void setSize(int k) {
        mSize = k;
        if (k <= 0) {
            mSize = 1;
        }
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

        switch (mStype) {
            case RECT:
                drawRectSate(canvas, top, left);
                break;
            default:
                drawRoundSate(canvas, top, left);
                break;
        }
    }

    private void drawRoundSate(Canvas canvas, float top, float left) {
        float r = mWidthItem / 2;
        float cy = top + r;
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