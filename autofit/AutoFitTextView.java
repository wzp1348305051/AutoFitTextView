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
        mMaxLength = typedArray.getFloat(R.styleable.AutoFitTextView_maxLength, 0);
        typedArray.recycle();

        addResizeTextSizeListener();
    }

    /**
     * 根据最大显示字符个数调整字体大小
     */
    private void addResizeTextSizeListener() {
        if (mMaxLength <= 0) {
            return;
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                int contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
                if (contentWidth <= 0) {
                    return;
                }
                int maxLengthOneLine = (int) Math.ceil(mMaxLength / mMaxLines);
                int fitSize = AutoFitTextViewUtil.getFitSize(getPaint(), getResources()
                        .getDisplayMetrics(), contentWidth, maxLengthOneLine);
                setTextSize(fitSize);
            }
        });
    }

}
