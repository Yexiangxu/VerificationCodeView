package com.lazyxu.verificationcodelibrary;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Date: 2018/12/13 14:58
 * Author: Xuyexiang
 * Title: 设置光标不能移动到数字前 只能在数字后面
 */
public class LastEditText extends android.support.v7.widget.AppCompatEditText {
    public LastEditText(Context context) {
        super(context);
    }

    public LastEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == selEnd) {
            setSelection(getText().length());
        }
    }
}
