package com.utimer.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

import com.google.common.base.Optional;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;

import ahtewlg7.utimer.util.MyRInfo;

import static ahtewlg7.utimer.entity.gtd.DeedSchemeInfo.INVALID_PROGRESS;


public class CalendarMonthView extends MonthView {

    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    /**
     * 进度条
     */
    private Paint mProgressPaint = new Paint();
    private Paint mNoneProgressPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;

    private CalendarSchemeFactory calendarInfoFactory;

    public CalendarMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(MyRInfo.getColorByID(R.color.color_600));
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(Color.WHITE);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));

        mNoneProgressPaint.setAntiAlias(true);
        mNoneProgressPaint.setStyle(Paint.Style.STROKE);
        mNoneProgressPaint.setStrokeWidth(dipToPx(context, 2.2f));
        mNoneProgressPaint.setColor(0xffaaaaaa);

        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

        calendarInfoFactory = new CalendarSchemeFactory();
    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        Optional<DeedSchemeInfo> calendarInfoOptional = calendarInfoFactory.toObject(calendar.getScheme());
        if(!calendarInfoOptional.isPresent() || calendarInfoOptional.get().getProgress() == INVALID_PROGRESS)
            return;

        int cx      = x + mItemWidth / 2;
        int cy      = y + mItemHeight / 2;
        int angle   = getAngle(calendarInfoOptional.get().getProgress());

        mProgressPaint.setColor(calendar.getSchemeColor());
        RectF progressRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(progressRectF, -90, angle, false, mProgressPaint);

        RectF noneRectF = new RectF(cx - mRadius, cy - mRadius, cx + mRadius, cy + mRadius);
        canvas.drawArc(noneRectF, angle - 90, 360 - angle, false, mNoneProgressPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        int top = y - mItemHeight / 6;

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
        }

        Optional<DeedSchemeInfo> calendarInfoOptional = calendarInfoFactory.toObject(calendar.getScheme());
        if (hasScheme && calendarInfoOptional.isPresent()) {
            mTextPaint.setColor(calendar.getSchemeColor());
            canvas.drawText(calendarInfoOptional.get().getTip(), x + mItemWidth - mPadding - mCircleRadius, y + mPadding + mSchemeBaseLine, mTextPaint);
        }

        String today = MyRInfo.getStringByID(R.string.title_calendar_today);
        if (isSelected) {
            canvas.drawText(calendar.isCurrentDay() ? today : String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
            canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
        }else {
            canvas.drawText(calendar.isCurrentDay() ? today : String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
                canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight / 10,
                        calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint  :
                                        mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
        }
    }

    /**
     * 获取角度
     *
     * @param progress 进度
     * @return 获取角度
     */
    private static int getAngle(int progress) {
        return (int) (progress * 3.6);
    }
    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
