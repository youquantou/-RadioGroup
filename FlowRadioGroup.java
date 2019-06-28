package com.jni_text.jyl.ailock.uitls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2019/3/28.
 */
public class FlowRadioGroup extends RadioGroup {
    /**
     * 用来保存每行view的列表x
     */
    private List<List<View>> mViewLinesLists = new ArrayList<>();

    private List<Integer> mLineHeights = new ArrayList<>();

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    //    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//
//        return (LayoutParams) new MarginLayoutParams(getContext(), attrs);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //拿到父容器的测量模式
        int iWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int iHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽高的建议值
        int iWidthModeSize = MeasureSpec.getSize(widthMeasureSpec);
        int iHeightModeSize = MeasureSpec.getSize(widthMeasureSpec);


        int measureHeight = 0;
        int measureWidth = 0;
        //当前行的宽高
        int iCurLineW = 0;
        int iCurLineH = 0;
        setMeasuredDimension(measureWidth, measureHeight);
        if (iWidthMode == MeasureSpec.EXACTLY && iHeightMode == MeasureSpec.EXACTLY) {

            measureHeight = iHeightModeSize;
            measureWidth = iWidthModeSize;

        } else {
            int iChildWidth;
            int iChildHeight;

            int childCount = getChildCount();
            List<View> viewList = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                //测量子布局
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //宽度=元素宽度+左右间隔
                iChildWidth = childView.getMeasuredWidth() + layoutParams.leftMargin
                        + layoutParams.rightMargin;
                iChildHeight = childView.getMeasuredHeight() + layoutParams.topMargin
                        + layoutParams.bottomMargin;

                if (iCurLineW + iChildWidth > iWidthModeSize) {
                    //如果子布局多个宽度相加 大于屏幕宽，则换行
                    //1.记录当前行的最大宽度，高度的累加
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight +=iCurLineH;

                    //2,将当前行的viewlist添加至总的mVIewlist，将行高添加至总的行高list
                    mViewLinesLists.add(viewList);
                    mLineHeights.add(iCurLineH);

                    //1.重新赋值新一行的宽、高

                    iCurLineW = iChildWidth;
                    iCurLineH = iChildHeight;

                    //2.新建一行的viewList，添加新一行的view
                    viewList = new ArrayList<View>();
                    viewList.add(childView);


                } else {
                    //记录某行内的消息
                    //1.行内宽度的叠加、高度的比较
                    iCurLineW += iChildWidth;
                    iCurLineH = Math.max(iCurLineH, iChildHeight);
                    //2.添加至当前行的viewlist中
                    viewList.add(childView);

                }
                //3.如果正好是最后一行 需要换行
                if (i == childCount - 1) {
                    //1.记录当前行的最大宽高，高度累加
                    measureWidth = Math.max(measureWidth, iCurLineW);
                    measureHeight += iCurLineH;

                    //2.将当前行的viewlist添加至总的mViewList，将行高添加
                    //至总的行高List
                    mViewLinesLists.add(viewList);
                    mLineHeights.add(iCurLineH);

                }

            }
        }
        //最终目的
        setMeasuredDimension(measureWidth, measureHeight);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left, top, right, bottom;
        int curTop = 0;
        int curLeft = 0;
        int lineCount = mViewLinesLists.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> viewList = mViewLinesLists.get(i);
            int lineSize = viewList.size();
            for (int j = 0; j < lineSize; j++) {
                View childView = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                curLeft += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;

            }
            curLeft = 0;
            curTop += mLineHeights.get(i);
        }
        mViewLinesLists.clear();
        mLineHeights.clear();
    }


}
