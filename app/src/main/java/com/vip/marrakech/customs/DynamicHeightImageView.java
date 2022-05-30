package com.vip.marrakech.customs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

@SuppressLint("AppCompatCustomView")
public class DynamicHeightImageView extends SimpleDraweeView {
    private float whRatio = 1.97f;

    public DynamicHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context) {
        super(context);

    }

    public void setRatio(float ratio) {
        whRatio = ratio;
    }

    @Override
    protected AbstractDraweeControllerBuilder getControllerBuilder() {
        return super.getControllerBuilder();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setAspectRatio(heightMeasureSpec/widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("REQE::::",getControllerBuilder().getImageRequest().toString());
      /*  if (whRatio != 0) {
            int width = getMeasuredWidth();
            int height = (int) (whRatio * width);

            setMeasuredDimension(width, height);
        }*/
    }
}