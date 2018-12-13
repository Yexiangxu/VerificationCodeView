package com.lazyxu.verificationcodelibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 仿滴滴输入验证码
 */
public class VerificationCodeView extends ViewGroup {
    private final static int TYPE_PHONE = 0;
    private final static int TYPE_NUMBER = 1;
    private final static int TYPE_PASSWORD = 2;
    private final static int TYPE_TEXT = 3;
    private final int textColor;
    private final float textSize;

    private int boxNumber;
    private int boxWidth;
    private int boxHeight;
    private int childHPadding;
    private int childVPadding;
    private int inputType;
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;
    private Listener listener;

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
        boxNumber = a.getInt(R.styleable.VerificationCodeView_box_number, 6);
        textColor = a.getColor(R.styleable.VerificationCodeView_text_color, Color.BLACK);
        textSize = a.getDimension(R.styleable.VerificationCodeView_text_size, sp2px(14));
        childHPadding = (int) a.getDimension(R.styleable.VerificationCodeView_child_h_padding, 0);
        childVPadding = (int) a.getDimension(R.styleable.VerificationCodeView_child_v_padding, 0);
        boxBgFocus = a.getDrawable(R.styleable.VerificationCodeView_box_bg_focus);
        boxBgNormal = a.getDrawable(R.styleable.VerificationCodeView_box_bg_normal);
        inputType = a.getInt(R.styleable.VerificationCodeView_inputType, TYPE_PHONE);
        boxWidth = (int) a.getDimension(R.styleable.VerificationCodeView_child_width, dp2px(60));
        boxHeight = (int) a.getDimension(R.styleable.VerificationCodeView_child_height, dp2px(60));
        a.recycle();
        initViews();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initViews() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    focusToNext(s.toString());
                }
            }
        };
        OnKeyListener onKeyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //防止调用两次
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
                    backFocus();
                }
                return false;

            }
        };
        OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //获得焦点
                currentIndex = v.getId();
                setBg((LastEditText) v, hasFocus);
            }
        };

        for (int i = 0; i < boxNumber; i++) {
            LastEditText editText = new LastEditText(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(boxWidth, boxHeight);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;
            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setId(i);
            editText.setCursorVisible(true);
            editText.setEms(1);
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            if (TYPE_NUMBER == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (TYPE_PASSWORD == inputType) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else if (TYPE_TEXT == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (TYPE_PHONE == inputType) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                editText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

            }
            editText.setPadding(0, 0, 0, 0);
            editText.setOnKeyListener(onKeyListener);
            editText.setOnFocusChangeListener(onFocusChangeListener);
            setBg(editText, false);
            editText.setTextColor(textColor);
            editText.setTextSize(textSize);
            editText.addTextChangedListener(textWatcher);
            addView(editText, i);
        }
    }

    private void backFocus() {
        if (currentIndex >= 0 && currentIndex < boxNumber) {
            currentIndex = currentIndex == 0 ? 0 : currentIndex - 1;
            LastEditText editText = (LastEditText) getChildAt(currentIndex);
            editText.requestFocus();
            //移动光标
            for (int next = currentIndex; next < boxNumber; next++) {
                ((LastEditText) getChildAt(next)).setText("");
            }
            stringBuilder.delete(currentIndex, stringBuilder.length());
            Log.i("VerificationCodeView", "stringBuilder=" + stringBuilder);
        }
    }

    private int currentIndex;

    private void focusToNext(String editshow) {
        int nextfocus = currentIndex + 1;
        stringBuilder.append(editshow);
        Log.i("VerificationCodeView", "stringBuilder=" + stringBuilder);
        if (nextfocus == boxNumber) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
            }
            return;
        }
        getChildAt(nextfocus).requestFocus();
    }


    private void setBg(LastEditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    StringBuilder stringBuilder = new StringBuilder();


    @Override
    public void setEnabled(boolean enabled) {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setEnabled(enabled);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        if (count > 0) {
            View child = getChildAt(0);
            int cHeight = child.getMeasuredHeight();
            int cWidth = child.getMeasuredWidth();
            int maxH = cHeight + 2 * childVPadding;
            int maxW = (cWidth + childHPadding) * boxNumber - childHPadding;
            setMeasuredDimension(resolveSize(maxW, widthMeasureSpec), resolveSize(maxH, heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            int cl = i * (cWidth + childHPadding);
            int cr = cl + cWidth;
            int ct = childVPadding;
            int cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

    public interface Listener {
        void onComplete(String content);
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }
}

