package com.centling.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * SingleSelectLinearLayout
 * Created by Victor on 16/1/25.
 */
public class SingleSelectLinearLayout
        extends LinearLayout {
    public interface OnItemClick {
        void onClick(int pos);
    }

    private OnItemClick onItemClick;
    private int count;

    public SingleSelectLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        onChildClick();
    }

    private void onChildClick() {
        count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).getClass() != View.class) {
                getChildAt(i).setOnClickListener(new OnClick(i));
            }
        }
        if (getChildCount() > 0) {
            getChildAt(0).setSelected(true);
        }
    }

    private class OnClick implements OnClickListener {
        private int pos;

        public OnClick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            setSelectedPos(pos);
            if (onItemClick != null) {
                onItemClick.onClick(pos);
            }
        }
    }

    public void setSelectedPos(int pos) {
        for (int i = 0; i < count; i++) {
            getChildAt(i).setSelected(i == pos);
        }
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
