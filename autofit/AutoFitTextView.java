package com.yitlib.common.widgets.autofit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import com.yitlib.common.R;

/**
 * 根据控件宽度和最大显示字符数自动调整字体大小TextView
 *
 * @author wengzhipeng
 * @version v1.0, 2017/8/7
 */

public class AutoFitTextView extends AppCompatTextView {
    private float mMaxLines;
    private float mMaxLength;
    private float mMaxSize;
    private float mMinSize;
    private float mForecastSize;// 预测字体大小，加快调整速度
    private boolean mHasResize;

    public AutoFitTextView(Context context) {
        this(context, null);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoFitTextView);
        mMaxLines = typedArray.getFloat(R.styleable.AutoFitTextView_maxLines, 1);
        mMaxLength = typedArray.getFloat(R.styleable.AutoFitTextView_maxLength, -1);
        mMaxSize = typedArray.getFloat(R.styleable.AutoFitTextView_maxSize, -1);
        mMinSize = typedArray.getFloat(R.styleable.AutoFitTextView_minSize, -1);
        mForecastSize = typedArray.getFloat(R.styleable.AutoFitTextView_forecastSize, -1);
        typedArray.recycle();

        addResizeTextSizeListener();
    }

    /**
     * 根据最大显示字符个数调整字体大小
     */
    private void addResizeTextSizeListener() {
        if (mMaxLength <= 0 || mMaxSize < mMinSize) {
            return;
        }
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!mHasResize) {
                    mHasResize = true;
                    int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
                    if (contentWidth > 0) {
                        int maxLengthOneLine = (int) Math.ceil(mMaxLength / mMaxLines);
                        float fitSize = AutoFitTextViewUtil.getFitSize(getPaint(), getResources()
                                .getDisplayMetrics(), contentWidth, maxLengthOneLine,
                                mForecastSize);
                        if (mMaxSize != -1 && fitSize > mMaxSize) {
                            fitSize = mMaxSize;
                        } else if (mMinSize != -1 && fitSize < mMinSize) {
                            fitSize = mMinSize;
                        }
                        setTextSize(fitSize);
                        return false;
                    }
                }
                return true;
            }
        });
    }

}
