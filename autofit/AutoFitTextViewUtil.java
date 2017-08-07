package com.yitlib.common.widgets.autofit;

import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类或接口的描述信息
 *
 * @author wengzhipeng
 * @version v1.0, 2017/8/7
 */

public class AutoFitTextViewUtil {
    private static final String mMaxString = "酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷酷";
    private static final Map<String, Integer> mFitSizeMap = new ConcurrentHashMap<>();

    private AutoFitTextViewUtil() {
        throw new UnsupportedOperationException("Cannot init");
    }

    public static int getFitSize(TextPaint textPaint, DisplayMetrics displayMetrics, int
            maxWidth, int maxLengthOneLine) {
        String key = String.valueOf(maxWidth) + String.valueOf(maxLengthOneLine);
        if (mFitSizeMap.containsKey(key)) {
            return mFitSizeMap.get(key);
        }

        TextPaint paint = new TextPaint();
        paint.set(textPaint);

        String text = null;
        if (maxLengthOneLine <= 50) {
            text = mMaxString.substring(0, maxLengthOneLine);
        } else {
            StringBuilder builder = new StringBuilder(mMaxString);
            while (text == null) {
                builder.append(mMaxString);
                if (maxLengthOneLine <= builder.length()) {
                    text = builder.substring(0, maxLengthOneLine);
                }
            }
        }

        int lastTextSize = 18;// 初始默认字体大小
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, lastTextSize,
                displayMetrics));
        float lastMeasureWidth = paint.measureText(text);

        if (lastMeasureWidth < maxWidth) {
            while (lastMeasureWidth < maxWidth) {
                lastTextSize++;
                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        lastTextSize, displayMetrics));
                lastMeasureWidth = paint.measureText(text);
            }
            lastTextSize--;
        } else {
            while (lastMeasureWidth >= maxWidth) {
                lastTextSize--;
                paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        lastTextSize, displayMetrics));
                lastMeasureWidth = paint.measureText(text);
            }
        }
        mFitSizeMap.put(key, lastTextSize);
        return lastTextSize;
    }
}
