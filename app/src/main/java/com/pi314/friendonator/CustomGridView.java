package com.pi314.friendonator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGridView extends GridView {

    private boolean haveScrollbar = false;

    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setHaveScrollbar(boolean haveScrollbar) {
        this.haveScrollbar = haveScrollbar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!haveScrollbar) {
            // MEASURED_SIZE_MASK represents the largest height possible adjusting the content
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
