package com.jni_text.jyl.ui_jyl_paint.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

public class MyRadioGroup extends RadioGroup {
    public MyRadioGroup(Context context) {
        this(context, null);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = 0;
        int maxLineHeight = 0;
        int lineWidth = 0;
        int totalHeight = 0;

        int oldWidth = 0;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            int childWidth = 0;
            int childHeight = 0;
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            oldWidth = maxWidth;

            childWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            childHeight = child.getMeasuredHeight() + params.bottomMargin + params.topMargin;

            if (childWidth + lineWidth > widthSize) {
                //最大行宽
                totalHeight += maxLineHeight;
                maxWidth = Math.max(lineWidth, oldWidth);
                lineWidth = childWidth;
                maxLineHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;

            } else {
                lineWidth += childWidth;
                maxLineHeight = Math.max(childHeight, maxLineHeight);
            }
            if (i == count - 1) {
                totalHeight += maxLineHeight;
                maxWidth = Math.max(lineWidth, oldWidth);

            }


        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        totalHeight += getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : maxWidth
                , heightMode == MeasureSpec.EXACTLY ? heightSize : totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int preLeft = getPaddingLeft();
        int preTop = getPaddingTop();
        int maxLineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int currentChildHeight = child.getMeasuredHeight() + params.bottomMargin + params.topMargin;
            int currentChildWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            if (r - l < currentChildWidth + preLeft) {
                preTop += maxLineHeight;
                preLeft = getPaddingLeft();
            } else {
                maxLineHeight = Math.max(maxLineHeight, currentChildHeight);
            }
            int left = preLeft;
            int top = preTop;
            int right = left + currentChildWidth;
            int bottom = top + currentChildHeight;
            child.layout(left, top, right, bottom);
            preLeft += currentChildWidth;

        }
    }
}
